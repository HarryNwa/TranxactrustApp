package org.harry.trustmonie.service;

import org.harry.trustmonie.DTOs.request.TranxactrustServiceRequest;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.WalletTransactions;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;

import java.math.BigDecimal;

public interface AdminServices {
    String registerAdmin(TranxactrustServiceRequest tranxactrustServiceRequest);

    String generateAdminId();

    EscrowTransactions findEscrowTransactionId(String transactionId);
    WalletTransactions findWalletTransactionId(String transactionId);

}

