package org.example.eurder.feature.customer.api;

import org.example.eurder.common.EndToEndTest;
import org.example.eurder.common.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

class CustomerEndToEndTest extends EndToEndTest {

    @Nested
    @DisplayName("Create a customer")
    class Register {

        @DisplayName("Using valid data")
        @Test
        void givenValidCustomer_whenRegisteringWithoutAuthentication_thenReturns201() {
            String email = TestData.uniqueEmail();

            anonymous().body(customerJson(email)).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("email", equalTo(email))
                    .body("fullName.firstName", equalTo("John"))
                    .body("fullName.lastName", equalTo("Doe"))
                    .body("phone", equalTo("+32 470 12 34 56"))
                    .body("address.city", equalTo("Brussels"));
        }

        @DisplayName("Using wrong data (email uppercase)")
        @Test
        void givenEmailWithCapitals_whenRegistering_thenEmailIsStoredInLowerCase() {
            String email = TestData.uniqueEmail();

            anonymous().body(customerJson(email.toUpperCase())).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("email", equalTo(email));
        }

        @DisplayName("Using wrong data (email duplicated)")
        @Test
        void givenEmailAlreadyInUse_whenRegistering_thenReturns409() {
            String email = newCustomer();

            anonymous().body(customerJson(email)).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("detail", equalTo("Email " + email + " is already in use"));
        }

        @DisplayName("Using wrong data (empty field)")
        @Test
        void givenEmptyCity_whenRegistering_thenReturns400WithFieldError() {
            String body = customerJson(TestData.uniqueEmail()).replace("\"city\": \"Brussels\"", "\"city\": \"\"");

            anonymous().body(body).post("/customers")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.'address.city'", hasItem("City is required"));
        }

        @DisplayName("Using wrong data (email format)")
        @Test
        void givenInvalidEmail_whenRegistering_thenReturns400() {
            anonymous().body(customerJson("not-an-email")).post("/customers")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.email", hasItem("Email must be valid"));
        }
    }

    @Nested
    @DisplayName("Authentication")
    class Authentication {

        @DisplayName("Correctly authenticated")
        @Test
        void givenRegisteredCustomer_whenLoggingIn_thenCanViewOwnReport() {
            String email = newCustomer();

            as(email).get("/orders/report")
                    .then().statusCode(HttpStatus.OK.value());
        }

        @DisplayName("Badly authenticated (wrong password)")
        @Test
        void givenWrongPassword_whenLoggingIn_thenReturns401() {
            String email = newCustomer();

            anonymous().auth().preemptive().basic(email, "wrong-password").get("/orders/report")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @DisplayName("Not authenticated")
        @Test
        void givenNoCredentials_whenCallingASecuredEndpoint_thenReturns401() {
            anonymous().get("/customers")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    @DisplayName("View all customers")
    class ListCustomers {

        @DisplayName("As an admin")
        @Test
        void givenAdmin_whenListingCustomers_thenNewCustomerIsListed() {
            String email = newCustomer();

            as(newAdmin()).get("/customers")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("email", hasItem(email));
        }

        @DisplayName("As a customer")
        @Test
        void givenCustomer_whenListingCustomers_thenReturns403() {
            as(newCustomer()).get("/customers")
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

    @Nested
    @DisplayName("View a single customer")
    class ViewCustomer {

        @DisplayName("As an admin")
        @Test
        void givenAdmin_whenViewingCustomer_thenReturnsTheirDetails() {
            String email = newCustomer();
            UUID id = findCustomer(email).getId();

            as(newAdmin()).get("/customers/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("email", equalTo(email))
                    .body("address.streetName", equalTo("Rue de la Loi"));
        }

        @DisplayName("As an admin but random ID")
        @Test
        void givenUnknownId_whenViewingCustomer_thenReturns404() {
            as(newAdmin()).get("/customers/{id}", UUID.randomUUID())
                    .then().statusCode(HttpStatus.NOT_FOUND.value());
        }

        @DisplayName("As an admin but bad format ID")
        @Test
        void givenMalformedId_whenViewingCustomer_thenReturns400() {
            as(newAdmin()).get("/customers/not-a-uuid")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("As a customer")
        @Test
        void givenCustomer_whenViewingAnotherCustomer_thenReturns403() {
            UUID id = findCustomer(newCustomer()).getId();

            as(newCustomer()).get("/customers/{id}", id)
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }
    }
}
