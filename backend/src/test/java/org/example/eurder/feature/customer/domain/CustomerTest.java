package org.example.eurder.feature.customer.domain;

import org.example.eurder.common.TestData;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerTest {

    @DisplayName("Testing email normalization")
    @Test
    void givenEmailWithSpacesAndCapitals_whenCreatingCustomer_thenEmailIsNormalized() {
        Customer customer = Customer.customer("  John.Doe@Gmail.COM ", "hash",
                new FullName("John", "Doe"), "0470123456", TestData.address());

        assertThat(customer.getEmail()).isEqualTo("john.doe@gmail.com");
        assertThat(customer.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @DisplayName("Testing exception")
    @Test
    void givenNoAddress_whenCreatingCustomer_thenThrows() {
        assertThatThrownBy(() -> Customer.customer("john@doe.com", "hash",
                new FullName("John", "Doe"), "0470123456", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Address is required");
    }

    @DisplayName("Testing admin creation")
    @Test
    void whenCreatingAdmin_thenRoleIsAdminAndProfileIsEmpty() {
        Customer admin = Customer.admin("admin@eurder.com", "hash");

        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(admin.getFullName()).isNull();
        assertThat(admin.getAddress()).isNull();
    }
}
