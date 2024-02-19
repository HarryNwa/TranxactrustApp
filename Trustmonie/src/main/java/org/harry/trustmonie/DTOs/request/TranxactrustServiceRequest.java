package org.harry.trustmonie.DTOs.request;

import lombok.Data;

import java.util.Date;

@Data
public class TranxactrustServiceRequest {
    private String accountNumber;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private String occupation;
    private String phoneNumber;
    private String email;
    private String transactionId;
}

