package org.harry.trustmonie.service;

import org.harry.trustmonie.user.WalletHolder;

public interface UserService {
    WalletHolder saveUser(WalletHolder user);
    Boolean verifyToken(String token);
    void userSave(WalletHolder walletHolder);
}



