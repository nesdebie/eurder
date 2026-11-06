package org.example.eurder.feature.user.domain;

import jakarta.persistence.*;
import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.domain.Role;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    private Customer(String email, String passwordHash, Role role,
                     FullName fullName, String phoneNumber, Address address) {
        this.email = normalizeEmail(Objects.requireNonNull(email));
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.role = Objects.requireNonNull(role);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    protected Customer() {
    }

    public static Customer customer(String email, String passwordHash,
                                    FullName fullName, String phoneNumber, Address address) {
        return new Customer(email, passwordHash, Role.CUSTOMER,
                Objects.requireNonNull(fullName, "Full name is required"),
                Objects.requireNonNull(phoneNumber, "Phone number is required"),
                Objects.requireNonNull(address, "Address is required"));
    }

    public static Customer admin(String email, String passwordHash) {
        return new Customer(email, passwordHash, Role.ADMIN, null, null, null);
    }

    public UUID getId() {
        return id;
    }

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public FullName getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }
}