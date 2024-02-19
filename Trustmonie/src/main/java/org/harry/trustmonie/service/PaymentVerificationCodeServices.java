package org.harry.trustmonie.service;


import org.harry.trustmonie.data.model.PaymentVerificationCode;

public interface PaymentVerificationCodeServices {
    String generatePaymentVerificationCode();
    PaymentVerificationCode findPaymentVerificationCode(String verifier);

    void senderVerificationCodeSave(PaymentVerificationCode verificationCode);

    void transferToEscrowSave(PaymentVerificationCode paymentVerifier);

}

