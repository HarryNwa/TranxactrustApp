package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.DTOs.request.TranxactrustServiceRequest;
import org.harry.trustmonie.data.model.Admin;
import org.harry.trustmonie.data.model.EscrowTransactions;
import org.harry.trustmonie.data.model.WalletTransactions;
import org.harry.trustmonie.data.repository.AdminRepository;
import org.harry.trustmonie.exceptions.IllegalTransactionIdException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class AdminServicesImpl implements AdminServices{
    private final AdminRepository adminRepository;
    private final EscrowTransactionsServices escrowTransactionsServices;
    private final WalletTransactionsServices walletTransactionsServices;

    @Override
    public String registerAdmin(TranxactrustServiceRequest tranxactrustServiceRequest){
        String adminName = tranxactrustServiceRequest.getFirstName() + " " + tranxactrustServiceRequest.getLastName();
        String adminId = generateAdminId();
        Admin admin = new Admin(adminName, adminId);
        adminRepository.save(admin);
        return adminId;
    }

    @Override
    public String generateAdminId() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder adminIDBuilder = new StringBuilder(6);

        while (adminIDBuilder.length() < 6) {
            int adminID = 100_000_000 + secureRandom.nextInt(900_000_000);
            adminIDBuilder.append(adminID);
        }
        if (adminIDBuilder.length() > 6) {
            adminIDBuilder.delete(6, adminIDBuilder.length());
        }
        return adminIDBuilder.toString();
    }

    @Override
    public EscrowTransactions findEscrowTransactionId(String transactionId) throws IllegalTransactionIdException {
        return escrowTransactionsServices.findEscrowReceiverAccountNumberByTransactionId(transactionId);
    }

    @Override
    public WalletTransactions findWalletTransactionId(String transactionId) throws IllegalTransactionIdException {
        return walletTransactionsServices.findEscrowWalletTransactionID(transactionId);
    }

}

