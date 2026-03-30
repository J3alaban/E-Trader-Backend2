package com.ecommerce.backend.services.concretes;

import com.ecommerce.backend.config.IyzicoConfig;
import com.ecommerce.backend.entities.*;
import com.ecommerce.backend.entities.OrderItem;
import com.ecommerce.backend.entities.Payment;
import com.ecommerce.backend.services.abstracts.PaymentGateway;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.model.Address;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

        Options options = new Options();
        options.setApiKey(iyzicoConfig.getApiKey());
        options.setSecretKey(iyzicoConfig.getSecretKey());
        options.setBaseUrl(iyzicoConfig.getBaseUrl());

        CreateCheckoutFormInitializeRequest request =
                new CreateCheckoutFormInitializeRequest();

        request.setLocale(Locale.TR.getValue());
        request.setConversationId(payment.getId().toString());
        request.setPrice(payment.getAmount());
        request.setPaidPrice(payment.getAmount());
        request.setCurrency(Currency.TRY.name());
        request.setBasketId("B" + payment.getOrder().getId());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());

        request.setCallbackUrl(
                iyzicoConfig.getCallbackUrl()
        );

        // BUYER
        User user = payment.getOrder().getUser();
        Buyer buyer = new Buyer();
        if (user != null) {
            buyer.setId(user.getId().toString());
            buyer.setName(user.getFirstName());
            buyer.setSurname(user.getLastName());
            buyer.setEmail(user.getEmail());
            buyer.setGsmNumber(user.getPhone());
            buyer.setIdentityNumber(user.getTcNo());
            buyer.setRegistrationAddress(
                    payment.getAddress() != null ? payment.getAddress().getStreet() : "N/A"
            );
            buyer.setCity(payment.getAddress() != null ? payment.getAddress().getCity() : "N/A");
            buyer.setCountry(payment.getAddress() != null ? payment.getAddress().getCountry() : "N/A");
        } else {
            // Misafir kullanıcı
            buyer.setId("0");
            buyer.setName("Misafir");
            buyer.setSurname("Kullanıcı");
            buyer.setEmail("guest@example.com");
            buyer.setGsmNumber("0000000000");
            buyer.setIdentityNumber("00000000000");
            buyer.setRegistrationAddress(payment.getAddress() != null ? payment.getAddress().getStreet() : "N/A");
            buyer.setCity(payment.getAddress() != null ? payment.getAddress().getCity() : "N/A");
            buyer.setCountry(payment.getAddress() != null ? payment.getAddress().getCountry() : "N/A");
        }
        buyer.setIp("127.0.0.1");
        request.setBuyer(buyer);

        // ADDRESS
        Address address = new Address();
        if (user != null) {
            address.setContactName(user.getFirstName() + " " + user.getLastName());
        } else {
            address.setContactName("Misafir Kullanıcı");
        }
        address.setCity(payment.getAddress() != null ? payment.getAddress().getCity() : "N/A");
        address.setCountry(payment.getAddress() != null ? payment.getAddress().getCountry() : "N/A");
        address.setAddress(payment.getAddress() != null ? payment.getAddress().getStreet() : "N/A");
        address.setZipCode(payment.getAddress() != null ? payment.getAddress().getZipCode() : "00000");

        request.setBillingAddress(address);
        request.setShippingAddress(address);

        // BASKET ITEMS
        List<BasketItem> basketItems = new ArrayList<>();
        for (OrderItem item : payment.getOrder().getOrderItems()) {
            BasketItem basketItem = new BasketItem();
            basketItem.setId(item.getProduct().getId().toString());
            basketItem.setName(item.getProduct().getTitle());
            basketItem.setCategory1(item.getProduct().getCategory().getName());
            basketItem.setItemType("PHYSICAL");
            basketItem.setPrice(BigDecimal.valueOf(item.getPrice() * item.getQuantity()));
            basketItems.add(basketItem);
        }
        request.setBasketItems(basketItems);

        CheckoutFormInitialize result =
                CheckoutFormInitialize.create(request, options);

        if (!"success".equals(result.getStatus())) {
            throw new RuntimeException(
                    "Iyzico error: " + result.getErrorMessage()
            );
        }

        payment.setTransactionId(result.getToken());
        return result.getCheckoutFormContent();
    }






    @Override
    public Payment pay(Payment payment) {
        return payment;
    }
}