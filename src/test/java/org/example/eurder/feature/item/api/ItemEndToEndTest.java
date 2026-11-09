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
    @DisplayName("Story 2 · Add an item")
    class AddItem {

        @Test
        void givenAdmin_whenAddingItem_thenReturns201() {
            // given
            String name = TestData.uniqueName();

            // when / then
            as(newAdmin()).body(itemJson(name, "19.99", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo(name))
                    .body("price", equalTo(19.99f))
                    .body("amount", equalTo(10));
        }

        @Test
        void givenNameAlreadyUsed_whenAddingItem_thenReturns409() {
            // given
            String admin = newAdmin();
            String name = TestData.uniqueName();
            as(admin).body(itemJson(name, "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.CREATED.value());

            // when / then
            as(admin).body(itemJson(name, "5.00", 1)).post("/items")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value())
                    .body("detail", equalTo("Item name " + name + " already exists."));
        }

        @Test
        void givenPriceWithThreeDecimals_whenAddingItem_thenReturns400() {
            // when / then
            as(newAdmin()).body(itemJson(TestData.uniqueName(), "19.999", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.price", hasItem("Price must have at most 8 digits and 2 decimals"));
        }

        @Test
        void givenZeroPrice_whenAddingItem_thenReturns400() {
            // when / then
            as(newAdmin()).body(itemJson(TestData.uniqueName(), "0", 10)).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.price", hasItem("Price must be greater than 0"));
        }

        @Test
        void givenMissingAmount_whenAddingItem_thenReturns400() {
            // given
            String body = """
                    { "name": "%s", "description": "A description", "price": 1.00 }
                    """.formatted(TestData.uniqueName());

            // when / then
            as(newAdmin()).body(body).post("/items")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.amount", hasItem("Amount is required"));
        }

        @Test
        void givenCustomer_whenAddingItem_thenReturns403() {
            // when / then
            as(newCustomer()).body(itemJson(TestData.uniqueName(), "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.FORBIDDEN.value());
        }

        @Test
        void givenNoCredentials_whenAddingItem_thenReturns401() {
            // when / then
            anonymous().body(itemJson(TestData.uniqueName(), "19.99", 10)).post("/items")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    @DisplayName("List items")
    class ListItems {

        @Test
        void givenItemInStockAndItemOutOfStock_whenListingItems_thenOnlyTheOneInStockIsListed() {
            // given
            String inStock = itemRepository.save(TestData.item("1.00", 3)).getName();
            String outOfStock = itemRepository.save(TestData.item("1.00", 0)).getName();

            // when / then
            as(newCustomer()).get("/items")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", hasItem(inStock))
                    .body("name", not(hasItem(outOfStock)));
        }
    }
}
