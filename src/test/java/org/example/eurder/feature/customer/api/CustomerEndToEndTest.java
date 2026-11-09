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
    @DisplayName("Story 1 · Create a customer account")
    class Register {

        @Test
        void givenValidCustomer_whenRegisteringWithoutAuthentication_thenReturns201() {
            // given
            String email = TestData.uniqueEmail();

            // when / then
            anonymous().body(customerJson(email)).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("email", equalTo(email))
                    .body("fullName.firstName", equalTo("John"))
                    .body("fullName.lastName", equalTo("Doe"))
                    .body("phone", equalTo("+32 470 12 34 56"))
                    .body("address.city", equalTo("Brussels"));
        }

        @Test
        void givenEmailWithCapitals_whenRegistering_thenEmailIsStoredInLowerCase() {
            // given
            String email = TestData.uniqueEmail();

            // when / then
            anonymous().body(customerJson(email.toUpperCase())).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("email", equalTo(email));
        }

        @Test
        void givenEmailAlreadyInUse_whenRegistering_thenReturns409() {
            // given
            String email = newCustomer();

            // when / then
            anonymous().body(customerJson(email)).post("/customers")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("detail", equalTo("Email " + email + " is already in use"));
        }

        @Test
        void givenEmptyCity_whenRegistering_thenReturns400WithFieldError() {
            // given
            String body = customerJson(TestData.uniqueEmail()).replace("\"city\": \"Brussels\"", "\"city\": \"\"");

            // when / then
            anonymous().body(body).post("/customers")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.'address.city'", hasItem("City is required"));
        }

        @Test
        void givenInvalidEmail_whenRegistering_thenReturns400() {
            // when / then
            anonymous().body(customerJson("not-an-email")).post("/customers")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.email", hasItem("Email must be valid"));
        }
    }

    @Nested
    @DisplayName("Authentication")
    class Authentication {

        @Test
        void givenRegisteredCustomer_whenLoggingIn_thenCanViewOwnReport() {
            // given
            String email = newCustomer();

            // when / then
            as(email).get("/orders/report")
                    .then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void givenWrongPassword_whenLoggingIn_thenReturns401() {
            // given
            String email = newCustomer();

            // when / then
            anonymous().auth().preemptive().basic(email, "wrong-password").get("/orders/report")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void givenNoCredentials_whenCallingASecuredEndpoint_thenReturns401() {
            // when / then
            anonymous().get("/customers")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    @DisplayName("Story 7 · View all customers")
    class ListCustomers {

        @Test
        void givenAdmin_whenListingCustomers_thenNewCustomerIsListed() {
            // given
            String email = newCustomer();

            // when / then
            as(newAdmin()).get("/customers")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("email", hasItem(email));
        }

        @Test
        void givenCustomer_whenListingCustomers_thenReturns403() {
            // when / then
            as(newCustomer()).get("/customers")
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

    @Nested
    @DisplayName("Story 8 · View a single customer")
    class ViewCustomer {

        @Test
        void givenAdmin_whenViewingCustomer_thenReturnsTheirDetails() {
            // given
            String email = newCustomer();
            UUID id = findCustomer(email).getId();

            // when / then
            as(newAdmin()).get("/customers/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("email", equalTo(email))
                    .body("address.streetName", equalTo("Rue de la Loi"));
        }

        @Test
        void givenUnknownId_whenViewingCustomer_thenReturns404() {
            // when / then
            as(newAdmin()).get("/customers/{id}", UUID.randomUUID())
                    .then().statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void givenMalformedId_whenViewingCustomer_thenReturns400() {
            // when / then
            as(newAdmin()).get("/customers/not-a-uuid")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void givenCustomer_whenViewingAnotherCustomer_thenReturns403() {
            // given
            UUID id = findCustomer(newCustomer()).getId();

            // when / then
            as(newCustomer()).get("/customers/{id}", id)
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }
    }
}
