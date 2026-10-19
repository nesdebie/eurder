package org.example.eurder.domain.user;

import java.util.UUID;

public class User {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
    private String phoneNumber;
    private Role role;

    public User(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Address address

    ) {
        this.id = UUID.randomUUID();
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = Role.CUSTOMER;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean hasPermission(Permission permission) {
        return role.permissions().contains(permission);
    }
}
