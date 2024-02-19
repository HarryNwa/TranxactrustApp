package org.harry.email.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "email-users")
public class EmailUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nationality;
    @JsonIgnore
    private Integer password;
    private String userName;
    private String businessName;
    private String phoneNumber;
    private String email;
    private boolean isEnabled;
    private Integer walletHolderId;

}
