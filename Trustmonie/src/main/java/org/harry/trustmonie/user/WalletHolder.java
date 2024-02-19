package org.harry.trustmonie.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.harry.trustmonie.data.model.Wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
public class WalletHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nationality;
    @JsonIgnore
    private Integer password;
    private String userName;
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wallet> listOfWallet = new ArrayList<>();
    private Date dateOfBirth;
    private String address;
    private String businessName;
    private String phoneNumber;
    private String email;
    private boolean isEnabled;



}

