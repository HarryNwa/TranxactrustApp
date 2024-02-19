package org.harry.trustmonie.data.repository;

import org.harry.trustmonie.data.model.PaymentVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentVerificationCodeRepository extends JpaRepository<PaymentVerificationCode, Long> {
    Optional<PaymentVerificationCode> findPaymentByVerificationCode(String verificationCode);
}

