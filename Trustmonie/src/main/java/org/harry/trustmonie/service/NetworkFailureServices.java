package org.harry.trustmonie.service;


public interface NetworkFailureServices {
    boolean makePaymentRequest();
    boolean simulateNetworkRequest();
    boolean getBalanceRequest();
    boolean getAnyRequest();

}

