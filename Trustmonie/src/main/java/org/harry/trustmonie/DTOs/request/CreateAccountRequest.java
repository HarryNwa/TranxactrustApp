package org.harry.trustmonie.DTOs.request;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private int password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String generalAccountNumber;
    private String escrowAccountNumber;
    private String email;
}

