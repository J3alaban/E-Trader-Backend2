package com.ecommerce.backend.entities;



public enum PaymentProvider {

    IYZICO(
            "Iyzico",
            true,   // supports 3D Secure
            true,   // supports refund
            true    // supports installment
    ),

    STRIPE(
            "Stripe",
            true,
            true,
            false
    ),

    PAYTR(
            "PayTR",
            true,
            true,
            true
    ),

    PAYPAL(
            "PayPal",
            false,
            true,
            false
    ),

    MANUAL_TRANSFER(
            "Manual Bank Transfer",
            false,
            true,
            false
    );

    private final String displayName;
    private final boolean supports3DSecure;
    private final boolean supportsRefund;
    private final boolean supportsInstallment;

    PaymentProvider(
            String displayName,
            boolean supports3DSecure,
            boolean supportsRefund,
            boolean supportsInstallment
    ) {
        this.displayName = displayName;
        this.supports3DSecure = supports3DSecure;
        this.supportsRefund = supportsRefund;
        this.supportsInstallment = supportsInstallment;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean supports3DSecure() {
        return supports3DSecure;
    }

    public boolean supportsRefund() {
        return supportsRefund;
    }

    public boolean supportsInstallment() {
        return supportsInstallment;
    }
}