package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.data.model.PaymentVerificationCode;
import org.harry.trustmonie.data.repository.PaymentVerificationCodeRepository;
import org.harry.trustmonie.exceptions.IllegalPaymentVerifierException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentVerificationCodeServicesImpl implements PaymentVerificationCodeServices {

    private final PaymentVerificationCodeRepository verificationCodeRepository;

    @Override
    public String generatePaymentVerificationCode() {
        return "TRXT" + (int) (Math.random() * 1000) + (int) (Math.random() * 10000);

    }
    @Override
    public PaymentVerificationCode findPaymentVerificationCode(String verificationCode) throws IllegalPaymentVerifierException {
        Optional<PaymentVerificationCode> paymentVerifier = verificationCodeRepository.findPaymentByVerificationCode(verificationCode);
        if (paymentVerifier.isEmpty()) throw new IllegalPaymentVerifierException("Invalid payment verification code");
        return paymentVerifier.get();

    }
    @Override
    public void senderVerificationCodeSave(PaymentVerificationCode verificationCode) {
        verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void transferToEscrowSave(PaymentVerificationCode paymentVerifier) {
        verificationCodeRepository.save(paymentVerifier);
    }

}

