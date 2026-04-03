package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.config.IyzicoConfig;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.entities.OrderItem;
import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.services.abstracts.PaymentGateway;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IyzicoPaymentGateway implements PaymentGateway {

    private final IyzicoConfig iyzicoConfig;

    @Override
    public boolean supports(Payment payment) {
        return payment.getProvider() == PaymentProvider.IYZICO;
    }

    @Override
    public String initializeForm(Payment payment) {

        Order order = payment.getOrder();
        if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Sipariş boş.");
        }

        Options options = new Options();
        options.setApiKey(iyzicoConfig.getApiKey());
        options.setSecretKey(iyzicoConfig.getSecretKey());
        options.setBaseUrl(iyzicoConfig.getBaseUrl());

        CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(payment.getId().toString());
        request.setPrice(payment.getAmount());
        request.setPaidPrice(payment.getAmount());
        request.setCurrency(Currency.TRY.name());
        request.setBasketId("B" + order.getId());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());
        request.setCallbackUrl(iyzicoConfig.getCallbackUrl());

        // ── BUYER ──────────────────────────────────────────────
        // Kayıtlı kullanıcı → User entity'sinden oku
        // Guest             → Order.guestName / guestEmail / guestPhone alanlarından oku
        User user = order.getUser();

        String name;
        String surname;
        String email;
        String gsm;
        String buyerId;

        boolean isRegisteredUser = (user != null
                && user.getEmail() != null
                && !user.getEmail().isBlank());

        if (isRegisteredUser) {
            name    = user.getFirstName() != null ? user.getFirstName() : "Kullanıcı";
            surname = user.getLastName()  != null ? user.getLastName()  : "Kullanıcı";
            email   = user.getEmail().trim();
            gsm     = user.getPhone() != null ? user.getPhone() : "5555555555";
            buyerId = user.getId().toString();
        } else {
            // Guest bilgileri — Order entity üzerindeki alanlar (önceki adımda eklendi)
            String rawName = order.getGuestName() != null ? order.getGuestName().trim() : "Misafir Kullanici";
            String[] parts = rawName.split(" ", 2);
            name    = parts[0];
            surname = parts.length > 1 ? parts[1] : "Kullanici";

            email = (order.getGuestEmail() != null && !order.getGuestEmail().isBlank())
                    ? order.getGuestEmail().trim()
                    : "guest" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

            gsm     = order.getGuestPhone() != null ? order.getGuestPhone() : "5555555555";
            buyerId = "G" + order.getId();
        }

        // GSM normalize — Iyzico +905XXXXXXXXX ister
        gsm = gsm.replaceAll("[^0-9]", "");
        if (gsm.startsWith("90")) gsm = gsm.substring(2);
        if (gsm.startsWith("0"))  gsm = gsm.substring(1);
        if (gsm.length() > 10)   gsm = gsm.substring(gsm.length() - 10);
        if (gsm.length() < 10)   gsm = "5555555555";
        gsm = "+90" + gsm;

        String street = payment.getAddress() != null ? payment.getAddress().getStreet() : "Merkez Mah.";
        String city   = payment.getAddress() != null ? payment.getAddress().getCity()   : "Ankara";

        Buyer buyer = new Buyer();
        buyer.setId(buyerId);
        buyer.setName(name);
        buyer.setSurname(surname);
        buyer.setEmail(email);
        buyer.setGsmNumber(gsm);
        buyer.setIdentityNumber(
                isRegisteredUser && user.getTcNo() != null ? user.getTcNo() : "11111111111"
        );
        buyer.setRegistrationAddress(street);
        buyer.setCity(city);
        buyer.setCountry("Turkey");
        buyer.setIp("85.34.78.12");

        request.setBuyer(buyer);

        // ── ADDRESS ───────────────────────────────────────────
        com.iyzipay.model.Address addr = new com.iyzipay.model.Address();
        addr.setContactName(name + " " + surname);
        addr.setCity(city);
        addr.setCountry("Turkey");
        addr.setAddress(street);
        addr.setZipCode("06000");

        request.setBillingAddress(addr);
        request.setShippingAddress(addr);

        // ── BASKET ────────────────────────────────────────────
        List<BasketItem> basketItems = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct() == null) continue;

            BasketItem bi = new BasketItem();
            bi.setId(item.getProduct().getId().toString());
            bi.setName(item.getProduct().getTitle());
            bi.setCategory1(
                    item.getProduct().getCategory() != null
                            ? item.getProduct().getCategory().getName()
                            : "Genel"
            );
            bi.setItemType(BasketItemType.PHYSICAL.name());

            BigDecimal price = BigDecimal.valueOf(item.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            bi.setPrice(price);
            basketItems.add(bi);
        }

        if (basketItems.isEmpty()) throw new RuntimeException("Sepet boş");

        request.setBasketItems(basketItems);

        // ── CALL ──────────────────────────────────────────────
        CheckoutFormInitialize result = CheckoutFormInitialize.create(request, options);

        if (!"success".equals(result.getStatus())) {
            System.err.println("IYZICO HATA | email=" + email + " gsm=" + gsm);
            System.err.println("IYZICO HATA | " + result.getErrorMessage());
            throw new RuntimeException("Iyzico: " + result.getErrorMessage());
        }

        payment.setTransactionId(result.getToken());
        return result.getCheckoutFormContent();
    }

    @Override
    public Payment pay(Payment payment) {
        return payment;
    }
}