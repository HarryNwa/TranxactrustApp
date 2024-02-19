package org.harry.trustmonie.service;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.exceptions.NetworkConnectionFailureException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Service
@AllArgsConstructor
public class NetworkFailureServicesImpl implements NetworkFailureServices {

    // Maximum number of retry attempts for network failures
    private static final int MAX_RETRY_ATTEMPTS = 3;

//        public static void main(String[] args) {
//            // Simulate a payment request
//            boolean paymentSuccess = makePaymentRequest();
//            if (paymentSuccess) {
//                System.out.println("Payment was successful.");
//            } else {
//                System.out.println("Payment failed. Please try again later or contact customer support.");
//            }
//        }

    public boolean makePaymentRequest() {
        int retryCount = 0;
        boolean paymentSuccess = false;

        while (retryCount < MAX_RETRY_ATTEMPTS) {
            try {
                // Simulate making a network request (e.g., to a payment gateway)
                boolean networkResponse = simulateNetworkRequest();

                // Check if the network request was successful
                if (networkResponse) {
                    paymentSuccess = true;
                    break;  // Payment successful, exit the loop
                } else {
                    // Handle other payment-related logic (e.g., insufficient funds)
                    // ...
                }
            } catch (NetworkConnectionFailureException e) {
                // Network failure| occurred
                System.out.println("Network failure: " + e.getMessage());
                retryCount++;

                if (retryCount < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Retrying payment request in 5 seconds...");
                    try {
                        TimeUnit.SECONDS.sleep(5); // Wait before retrying
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Max retry attempts reached. Payment failed.");
                }
            }
        }

        return paymentSuccess;
    }
    public boolean getBalanceRequest() {
        int retryCount = 0;
        boolean getBalanceSuccess = false;

        while (retryCount < MAX_RETRY_ATTEMPTS) {
            try {
                // Simulate making a network request (e.g., to a payment gateway)
                boolean networkResponse = simulateNetworkRequest();

                // Check if the network request was successful
                if (networkResponse) {
                    getBalanceSuccess = true;
                    break;  // Payment successful, exit the loop
                } else {
                    // Handle other payment-related logic (e.g., insufficient funds)
                    // ...
                }
            } catch (NetworkConnectionFailureException e) {
                // Network failure| occurred
                System.out.println("Network failure: " + e.getMessage());
                retryCount++;

                if (retryCount < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Requesting for balance again in 2 seconds...");
                    try {
                        TimeUnit.SECONDS.sleep(2); // Wait before retrying
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Max retry attempts reached. Request denied.");
                }
            }
        }

        return getBalanceSuccess;
    }
    public boolean getAnyRequest() {
        int retryCount = 0;
        boolean requestSuccess = false;

        while (retryCount < MAX_RETRY_ATTEMPTS) {
            try {
                // Simulate making a network request (e.g., to a payment gateway)
                boolean networkResponse = simulateNetworkRequest();

                // Check if the network request was successful
                if (networkResponse) {
                    requestSuccess = true;
                    break;  // Payment successful, exit the loop
                } else {
                    // Handle other payment-related logic (e.g., insufficient funds)
                    // ...
                }
            } catch (NetworkConnectionFailureException e) {
                // Network failure| occurred
                System.out.println("Network failure: " + e.getMessage());
                retryCount++;

                if (retryCount < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Requesting for balance again in 2 seconds...");
                    try {
                        TimeUnit.SECONDS.sleep(2); // Wait before retrying
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Max retry attempts reached. Request denied.");
                }
            }
        }

        return requestSuccess;
    }

    public boolean simulateNetworkRequest() throws NetworkConnectionFailureException {
        // Simulate a network request here (e.g., making an HTTP request to a payment gateway)
        // In this example, we simulate a network failure after the third attempt.
        if (Math.random() < 0.33) {
            throw new NetworkConnectionFailureException("Network connection refused");
        }
        return true;  // Simulated success
    }
}

