package org.example.eurder.domain;

import java.util.UUID;

public class Customer {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;

    public Customer(String lastName, String firstName, String email, String address, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
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

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
