package org.harry.trustmonie.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String adminId;
    private String adminName;
    private String adminPassword;
    private String bank;
    public Admin(String adminId, String adminName, String adminPassword, String bank){
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.bank = bank;
    }

    public Admin(){

    }

    public Admin(String adminName, String adminId) {
        this.adminName = adminName;
        this.adminId = adminId;
    }
}

