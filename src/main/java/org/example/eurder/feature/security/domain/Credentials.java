package org.example.eurder.feature.security.domain;

import jakarta.persistence.*;

import java.util.Locale;
import java.util.UUID;

public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected Credentials() {
    }

    public Credentials(String email, String passwordHash, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public UUID getId() {
        return id;
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

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
