package org.example.eurder.feature.user.domain;

import jakarta.persistence.*;
import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.domain.Credentials;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Embedded
    private FullName fullName;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credentials_id")
    private Credentials credentials;
    
    private String telephone;
    
    @Embedded
    private Address address;

    public User(FullName fullName, Credentials credentials, String telephone, Address address) {
        this.fullName = fullName;
        this.credentials = Objects.requireNonNull(credentials);
        this.telephone = telephone;
        this.address = address;
    }

    protected User() {
    }
}