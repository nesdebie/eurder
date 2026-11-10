package org.example.eurder.common;

import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.item.domain.Item;

import java.math.BigDecimal;
import java.util.UUID;

public final class TestData {

    public static final String PASSWORD = "secret123";

    private TestData() {}

    public static String uniqueEmail() {
        return "user-" + UUID.randomUUID() + "@eurder.com";
    }

    public static String uniqueName() {
        return "Item-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static Address address() {
        return address("Brussels");
    }

    public static Address address(String city) {
        return new Address("Rue de la Loi", "16", "1000", city);
    }

    public static Customer customer(String passwordHash) {
        return customer(passwordHash, address());
    }

    public static Customer customer(String passwordHash, Address address) {
        return Customer.customer(uniqueEmail(), passwordHash, new FullName("John", "Doe"), "0470123456", address);
    }

    public static Customer admin(String passwordHash) {
        return Customer.admin(uniqueEmail(), passwordHash);
    }

    public static Item item(String price, int stock) {
        return new Item(uniqueName(), "A description", new BigDecimal(price), stock);
    }
}
