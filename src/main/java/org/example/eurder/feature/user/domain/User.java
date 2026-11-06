package org.example.eurder.feature.user.domain;

import jakarta.persistence.*;
import org.example.eurder.feature.security.domain.Role;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;
    private String email;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private FullName fullName;
    private String phoneNumber;
    @Embedded
    private Address address;

    private User(String email, String passwordHash, Role role,
                 FullName fullName, String phoneNumber, Address address) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    protected User() {
        // for JPA
    }

    public static User customer(String email, String passwordHash,
                                FullName fullName, String phoneNumber, Address address) {
        // vérifier que fullName, phoneNumber et address ne sont pas null
        return new User(email, passwordHash, Role.CUSTOMER, fullName, phoneNumber, address);
    }

    public static User admin(String email, String passwordHash) {
        return new User(email, passwordHash, Role.ADMIN, null, null, null);
    }

    // getters, aucun setter public
}