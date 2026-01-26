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
        buyer.setId(user.getId().toString());
        buyer.setName(user.getFirstName());
        buyer.setSurname(user.getLastName());
        buyer.setEmail(user.getEmail());
        buyer.setGsmNumber(user.getPhone());
        buyer.setIdentityNumber(user.getTcNo());
        buyer.setRegistrationAddress(payment.getAddress().getStreet());
        buyer.setCity(payment.getAddress().getCity());
        buyer.setCountry(payment.getAddress().getCountry());
        buyer.setIp("127.0.0.1");
        request.setBuyer(buyer);

        // ADDRESS
        com.iyzipay.model.Address address =
                new com.iyzipay.model.Address();
        address.setContactName(
                user.getFirstName() + " " + user.getLastName()
        );
        address.setCity(payment.getAddress().getCity());
        address.setCountry(payment.getAddress().getCountry());
        address.setAddress(payment.getAddress().getStreet());
        address.setZipCode(payment.getAddress().getZipCode());

        request.setBillingAddress(address);
        request.setShippingAddress(address);

        // BASKET ITEMS
        List<BasketItem> basketItems = new ArrayList<>();
        for (OrderItem item : payment.getOrder().getOrderItems()) {

            BasketItem basketItem = new BasketItem();
            basketItem.setId(item.getProduct().getId().toString());
            basketItem.setName(item.getProduct().getTitle());
            basketItem.setCategory1(
                    item.getProduct().getCategory().getName()
            );
            basketItem.setItemType("PHYSICAL");
            basketItem.setPrice(
                    BigDecimal.valueOf(
                            item.getPrice() * item.getQuantity()
                    )
            );

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