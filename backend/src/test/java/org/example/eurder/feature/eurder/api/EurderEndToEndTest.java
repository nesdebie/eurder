package org.example.eurder.feature.eurder.api;

import org.example.eurder.common.EndToEndTest;
import org.example.eurder.common.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class EurderEndToEndTest extends EndToEndTest {

    private String addItem(String admin, String price, int amount) {
        return as(admin).body(itemJson(TestData.uniqueName(), price, amount)).post("/items")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().path("id");
    }

    private static String orderJson(String itemId, int amount) {
        return """
                { "items": [ { "id": "%s", "amount": %d } ] }
                """.formatted(itemId, amount);
    }

    @DisplayName("Adding item as an admin")
    @Test
    void givenItemAddedByAdmin_whenNewCustomerOrdersIt_thenEurderAndReportAreCorrect() {
        String fridge = addItem(newAdmin(), "499.99", 5);
        String customer = newCustomer();

        as(customer).body(orderJson(fridge, 2)).post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("items[0].shippingDate", equalTo("2026-11-10"))
                .body("totalPrice", equalTo(999.98f));

        as(customer).get("/orders/report")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("eurders", hasSize(1))
                .body("eurders[0].items[0].amount", equalTo(2))
                .body("totalPrice", equalTo(999.98f));
    }

    @DisplayName("Testing reorder after a price change")
    @Test
    void givenOwnEurder_whenReorderingAfterAPriceChange_thenNewPriceIsUsed() {
        String admin = newAdmin();
        String itemId = addItem(admin, "10.00", 10);
        String customer = newCustomer();
        String eurderId = as(customer).body(orderJson(itemId, 1)).post("/orders")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().path("id");
        as(admin).body(itemJson(TestData.uniqueName(), "15.00", 10)).put("/items/{id}/update", itemId)
                .then().statusCode(HttpStatus.OK.value());

        as(customer).post("/orders/{id}/reorder", eurderId)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("totalPrice", equalTo(15.0f));
    }

    @DisplayName("Ordering as an anonymous")
    @Test
    void givenNoCredentials_whenOrdering_thenReturns401() {
        anonymous().body(orderJson(addItem(newAdmin(), "1.00", 1), 1)).post("/orders")
                .then().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Ordering with a missing amount")
    @Test
    void givenMissingAmount_whenOrdering_thenReturns400WithIndexedFieldError() {
        String body = """
                { "items": [ { "id": "%s" } ] }
                """.formatted(addItem(newAdmin(), "1.00", 1));

        as(newCustomer()).body(body).post("/orders")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors.'items[0].amount'[0]", equalTo("Amount is required"));
    }
}
