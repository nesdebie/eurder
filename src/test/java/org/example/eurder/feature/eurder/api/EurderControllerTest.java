package org.example.eurder.feature.eurder.api;

import com.jayway.jsonpath.JsonPath;
import org.example.eurder.common.MutableClock;
import org.example.eurder.common.TestData;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.customer.domain.CustomerRepository;
import org.example.eurder.feature.item.domain.Item;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.example.eurder.feature.security.authentication.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(EurderControllerTest.TestClockConfig.class)
class EurderControllerTest {

    private static final ZoneId BRUSSELS = ZoneId.of("Europe/Brussels");
    private static final ZonedDateTime NOW = ZonedDateTime.of(2026, 11, 9, 10, 0, 0, 0, BRUSSELS);

    @TestConfiguration
    static class TestClockConfig {
        @Bean
        @Primary
        MutableClock testClock() {
            return new MutableClock(NOW);
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MutableClock clock;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void resetClock() {
        clock.setTo(NOW);
    }

    private Customer saveCustomer() {
        return customerRepository.save(TestData.customer("hash"));
    }

    private Customer saveCustomerLivingIn(String city) {
        return customerRepository.save(TestData.customer("hash", TestData.address(city)));
    }

    private Item saveItem(String price, int stock) {
        return itemRepository.save(TestData.item(price, stock));
    }

    private ResultActions order(Customer customer, Item item, int amount) throws Exception {
        return mockMvc.perform(post("/orders")
                .with(user(AuthenticatedUser.from(customer)))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "items": [ { "id": "%s", "amount": %d } ] }
                        """.formatted(item.getId(), amount)));
    }

    private String orderAndGetId(Customer customer, Item item, int amount) throws Exception {
        String response = order(customer, item, amount)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(response, "$.id");
    }

    @Nested
    @DisplayName("Story 3 · Order items")
    class CreateEurder {

        @Test
        void givenItemInStock_whenOrdering_thenShipsNextDayAndReturnsTotal() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("19.99", 10);

            // when
            ResultActions result = order(customer, item, 3);

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.items[0].itemId").value(item.getId().toString()))
                    .andExpect(jsonPath("$.items[0].itemName").value(item.getName()))
                    .andExpect(jsonPath("$.items[0].amount").value(3))
                    .andExpect(jsonPath("$.items[0].totalPrice").value(59.97))
                    .andExpect(jsonPath("$.items[0].shippingDate").value("2026-11-10"))
                    .andExpect(jsonPath("$.totalPrice").value(59.97));
        }

        @Test
        void givenItemNotEnoughInStock_whenOrdering_thenShipsInSevenDays() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("19.99", 2);

            // when
            ResultActions result = order(customer, item, 3);

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.items[0].shippingDate").value("2026-11-16"));
        }

        @Test
        void givenTheClockMovedForward_whenOrdering_thenShippingDateFollowsTheClock() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("19.99", 10);
            clock.setTo(NOW.plusDays(30));

            // when
            ResultActions result = order(customer, item, 1);

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.items[0].shippingDate").value("2026-12-10"));
        }

        @Test
        void givenItemInStock_whenOrdering_thenStockIsReduced() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("19.99", 10);

            // when
            order(customer, item, 3).andExpect(status().isCreated());

            // then
            assertThat(itemRepository.findById(item.getId()).orElseThrow().getAmount()).isEqualTo(7);
        }

        @Test
        void givenTwoItems_whenOrdering_thenTotalIsTheSumOfBothItemGroups() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item fridge = saveItem("19.99", 10);
            Item lamp = saveItem("0.10", 10);

            // when
            ResultActions result = mockMvc.perform(post("/orders")
                    .with(user(AuthenticatedUser.from(customer)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            { "items": [ { "id": "%s", "amount": 3 }, { "id": "%s", "amount": 2 } ] }
                            """.formatted(fridge.getId(), lamp.getId())));

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.items.length()").value(2))
                    .andExpect(jsonPath("$.totalPrice").value(60.17));
        }

        @Test
        void givenUnknownItemAfterAValidOne_whenOrdering_thenReturns404AndStockIsUnchanged() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("19.99", 10);

            // when
            ResultActions result = mockMvc.perform(post("/orders")
                    .with(user(AuthenticatedUser.from(customer)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            { "items": [ { "id": "%s", "amount": 3 }, { "id": "%s", "amount": 1 } ] }
                            """.formatted(item.getId(), UUID.randomUUID())));

            // then: the transaction is rolled back, the first item keeps its stock
            result.andExpect(status().isNotFound());
            assertThat(itemRepository.findById(item.getId()).orElseThrow().getAmount()).isEqualTo(10);
        }

        @Test
        void givenNoItems_whenOrdering_thenReturns400() throws Exception {
            // when
            ResultActions result = mockMvc.perform(post("/orders")
                    .with(user(AuthenticatedUser.from(saveCustomer())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            { "items": [] }
                            """));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors.items[0]").value("An eurder must contain at least one item"));
        }

        @Test
        void givenAdmin_whenOrdering_thenReturns403() throws Exception {
            // given
            Customer admin = customerRepository.save(TestData.admin("hash"));

            // when
            ResultActions result = order(admin, saveItem("19.99", 10), 1);

            // then
            result.andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Story 5 · View report of orders")
    class Report {

        @Test
        void givenTwoEurders_whenViewingReport_thenReturnsBothWithGrandTotal() throws Exception {
            // given
            Customer customer = saveCustomer();
            orderAndGetId(customer, saveItem("10.00", 10), 2);   // 20.00
            orderAndGetId(customer, saveItem("2.50", 10), 3);    //  7.50

            // when
            ResultActions result = mockMvc.perform(get("/orders/report")
                    .with(user(AuthenticatedUser.from(customer))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.eurders.length()").value(2))
                    .andExpect(jsonPath("$.totalPrice").value(27.5));
        }

        @Test
        void givenNoEurders_whenViewingReport_thenReturnsEmptyReport() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/orders/report")
                    .with(user(AuthenticatedUser.from(saveCustomer()))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.eurders", empty()))
                    .andExpect(jsonPath("$.totalPrice").value(0));
        }
    }

    @Nested
    @DisplayName("Story 6 · Reorder an existing order")
    class Reorder {

        @Test
        void givenOwnEurder_whenReordering_thenReturns201WithCurrentPrice() throws Exception {
            // given
            Customer customer = saveCustomer();
            Item item = saveItem("10.00", 10);
            String eurderId = orderAndGetId(customer, item, 1);
            Item changed = itemRepository.findById(item.getId()).orElseThrow();
            changed.update(new Item(changed.getName(), changed.getDescription(), new java.math.BigDecimal("12.00"), 9));
            itemRepository.save(changed);

            // when
            ResultActions result = mockMvc.perform(post("/orders/{id}/reorder", eurderId)
                    .with(user(AuthenticatedUser.from(customer))));

            // then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(org.hamcrest.Matchers.not(eurderId)))
                    .andExpect(jsonPath("$.totalPrice").value(12.0));
        }

        @Test
        void givenEurderOfAnotherCustomer_whenReordering_thenReturns403() throws Exception {
            // given
            String eurderId = orderAndGetId(saveCustomer(), saveItem("19.99", 10), 1);

            // when
            ResultActions result = mockMvc.perform(post("/orders/{id}/reorder", eurderId)
                    .with(user(AuthenticatedUser.from(saveCustomer()))));

            // then
            result.andExpect(status().isForbidden());
        }

        @Test
        void givenUnknownEurder_whenReordering_thenReturns404() throws Exception {
            // when
            ResultActions result = mockMvc.perform(post("/orders/{id}/reorder", UUID.randomUUID())
                    .with(user(AuthenticatedUser.from(saveCustomer()))));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Story 9 · Items shipping today")
    class ShippedToday {

        private static final ZonedDateTime ORDER_DAY = ZonedDateTime.of(2031, 3, 3, 10, 0, 0, 0, BRUSSELS);

        @Test
        void givenItemGroupShippingToday_whenViewingShippedToday_thenListsItWithTheCustomerAddress() throws Exception {
            // given: ordered yesterday with enough stock, so it ships today
            String city = "City-" + UUID.randomUUID();
            Customer customer = saveCustomerLivingIn(city);
            Item item = saveItem("19.99", 10);
            clock.setTo(ORDER_DAY);
            order(customer, item, 2).andExpect(status().isCreated());
            clock.setTo(ORDER_DAY.plusDays(1));
            Customer admin = customerRepository.save(TestData.admin("hash"));

            // when
            String response = mockMvc.perform(get("/orders/shipped-today")
                            .with(user(AuthenticatedUser.from(admin))))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            // then
            String shipment = "$.itemsShippedToday[?(@.addressDto.city == '" + city + "')]";
            List<String> itemNames = JsonPath.read(response, shipment + ".itemgroups[*].itemName");
            List<Integer> amounts = JsonPath.read(response, shipment + ".itemgroups[*].amount");
            assertThat(itemNames).containsExactly(item.getName());
            assertThat(amounts).containsExactly(2);
        }

        @Test
        void givenEurderShippingNextWeek_whenViewingShippedToday_thenItIsNotListed() throws Exception {
            // given: not enough stock, so it ships in 7 days and not today
            String city = "City-" + UUID.randomUUID();
            Customer customer = saveCustomerLivingIn(city);
            clock.setTo(ORDER_DAY);
            order(customer, saveItem("19.99", 1), 2).andExpect(status().isCreated());
            clock.setTo(ORDER_DAY.plusDays(1));
            Customer admin = customerRepository.save(TestData.admin("hash"));

            // when
            ResultActions result = mockMvc.perform(get("/orders/shipped-today")
                    .with(user(AuthenticatedUser.from(admin))));

            // then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.itemsShippedToday[?(@.addressDto.city == '" + city + "')]", empty()));
        }

        @Test
        void givenCustomer_whenViewingShippedToday_thenReturns403() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/orders/shipped-today")
                    .with(user(AuthenticatedUser.from(saveCustomer()))));

            // then
            result.andExpect(status().isForbidden());
        }
    }
}
