package org.example.eurder.feature.item.api;

import org.example.eurder.common.EndToEndTest;
import org.example.eurder.common.TestData;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

class ItemEndToEndTest extends EndToEndTest {

    @Autowired
    private ItemRepository itemRepository;

    @Nested
    @DisplayName("Add an item")
    class AddItem {

        @DisplayName("Using valid data")
        @Test
        void givenAdmin_whenAddingItem_thenReturns201() {
            String name = TestData.uniqueName();

            as(newAdmin()).body(itemJson(name, "19.99", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo(name))
                    .body("price", equalTo(19.99f))
                    .body("amount", equalTo(10));
        }

        @DisplayName("Using name already used")
        @Test
        void givenNameAlreadyUsed_whenAddingItem_thenReturns409() {
            String admin = newAdmin();
            String name = TestData.uniqueName();
            as(admin).body(itemJson(name, "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.CREATED.value());

            as(admin).body(itemJson(name, "5.00", 1)).post("/items")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("detail", equalTo("Item name " + name + " already exists."));
        }

        @DisplayName("Using 3-decimals price")
        @Test
        void givenPriceWithThreeDecimals_whenAddingItem_thenReturns400() {
            as(newAdmin()).body(itemJson(TestData.uniqueName(), "19.999", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.price", hasItem("Price must have at most 8 digits and 2 decimals"));
        }

        @DisplayName("Using 0.00-price")
        @Test
        void givenZeroPrice_whenAddingItem_thenReturns400() {
            as(newAdmin()).body(itemJson(TestData.uniqueName(), "0", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.price", hasItem("Price must be greater than 0"));
        }

        @DisplayName("Using no amount")
        @Test
        void givenMissingAmount_whenAddingItem_thenReturns400() {
            String body = """
                    { "name": "%s", "description": "A description", "price": 1.00 }
                    """.formatted(TestData.uniqueName());

            as(newAdmin()).body(body).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.amount", hasItem("Amount is required"));
        }

        @DisplayName("As a customer")
        @Test
        void givenCustomer_whenAddingItem_thenReturns403() {
            as(newCustomer()).body(itemJson(TestData.uniqueName(), "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }

        @DisplayName("As an anonymous")
        @Test
        void givenNoCredentials_whenAddingItem_thenReturns401() {
            anonymous().body(itemJson(TestData.uniqueName(), "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    @DisplayName("List items")
    class ListItems {

        @DisplayName("Excluding out of stock")
        @Test
        void givenItemInStockAndItemOutOfStock_whenListingItems_thenOnlyTheOneInStockIsListed() {
            String inStock = itemRepository.save(TestData.item("1.00", 3)).getName();
            String outOfStock = itemRepository.save(TestData.item("1.00", 0)).getName();

            as(newCustomer()).get("/items")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", hasItem(inStock))
                    .body("name", not(hasItem(outOfStock)));
        }
    }
}
