package org.example.eurder.feature.item.api;

import com.jayway.jsonpath.JsonPath;
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
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Customer admin;

    @BeforeEach
    void setUp() {
        admin = customerRepository.save(TestData.admin("hash"));
    }

    private static String itemJson(String name, String price, int amount) {
        return """
                { "name": "%s", "description": "A description", "price": %s, "amount": %d }
                """.formatted(name, price, amount);
    }

    @Nested
    @DisplayName("Story 4 · Update an item")
    class UpdateItem {

        @Test
        void givenNewValues_whenUpdating_thenReturns200WithNewValues() throws Exception {
            Item item = itemRepository.save(TestData.item("10.00", 5));
            String newName = TestData.uniqueName();

            ResultActions result = mockMvc.perform(put("/items/{id}/update", item.getId())
                    .with(user(AuthenticatedUser.from(admin)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemJson(newName, "12.50", 8)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(item.getId().toString()))
                    .andExpect(jsonPath("$.name").value(newName))
                    .andExpect(jsonPath("$.price").value(12.5))
                    .andExpect(jsonPath("$.amount").value(8));
        }

        @Test
        void givenSameName_whenUpdatingOnlyThePrice_thenReturns200() throws Exception {
            Item item = itemRepository.save(TestData.item("10.00", 5));

            ResultActions result = mockMvc.perform(put("/items/{id}/update", item.getId())
                    .with(user(AuthenticatedUser.from(admin)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemJson(item.getName(), "11.00", 5)));

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.price").value(11.0));
        }

        @Test
        void givenNameOfAnotherItem_whenUpdating_thenReturns409() throws Exception {
            Item item = itemRepository.save(TestData.item("10.00", 5));
            Item other = itemRepository.save(TestData.item("10.00", 5));

            ResultActions result = mockMvc.perform(put("/items/{id}/update", item.getId())
                    .with(user(AuthenticatedUser.from(admin)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemJson(other.getName(), "10.00", 5)));

            result.andExpect(status().isConflict());
        }

        @Test
        void givenUnknownId_whenUpdating_thenReturns404() throws Exception {
            ResultActions result = mockMvc.perform(put("/items/{id}/update", UUID.randomUUID())
                    .with(user(AuthenticatedUser.from(admin)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemJson(TestData.uniqueName(), "10.00", 5)));

            result.andExpect(status().isNotFound());
        }

        @Test
        void givenCustomer_whenUpdating_thenReturns403() throws Exception {
            Item item = itemRepository.save(TestData.item("10.00", 5));
            Customer customer = customerRepository.save(TestData.customer("hash"));

            ResultActions result = mockMvc.perform(put("/items/{id}/update", item.getId())
                    .with(user(AuthenticatedUser.from(customer)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(itemJson(TestData.uniqueName(), "10.00", 5)));

            result.andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Story 10 · Item overview")
    class Inventory {

        @Test
        void givenItemsOfEachLevel_whenFilteringOnStockLow_thenReturnsOnlyItemsBelowFive() throws Exception {
            Item low = itemRepository.save(TestData.item("1.00", 2));
            Item medium = itemRepository.save(TestData.item("1.00", 7));
            Item high = itemRepository.save(TestData.item("1.00", 12));

            String response = mockMvc.perform(get("/items/inventory").param("urgency", "STOCK_LOW")
                            .with(user(AuthenticatedUser.from(admin))))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            List<String> names = JsonPath.read(response, "$[*].name");
            List<Integer> amounts = JsonPath.read(response, "$[*].amount");
            assertThat(names).contains(low.getName()).doesNotContain(medium.getName(), high.getName());
            assertThat(amounts).allMatch(amount -> amount < 5);
        }

        @Test
        void givenNoFilter_whenViewingInventory_thenItemsAreSortedFromMostToLeastUrgent() throws Exception {
            itemRepository.save(TestData.item("1.00", 12));
            itemRepository.save(TestData.item("1.00", 2));
            itemRepository.save(TestData.item("1.00", 7));

            String response = mockMvc.perform(get("/items/inventory")
                            .with(user(AuthenticatedUser.from(admin))))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            List<Integer> amounts = JsonPath.read(response, "$[*].amount");
            assertThat(amounts).isSorted();
        }

        @Test
        void givenUnknownLevel_whenFiltering_thenReturns400() throws Exception {
            ResultActions result = mockMvc.perform(get("/items/inventory").param("urgency", "WRONG")
                    .with(user(AuthenticatedUser.from(admin))));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Invalid value 'WRONG' for parameter 'urgency'"));
        }

        @Test
        void givenCustomer_whenViewingInventory_thenReturns403() throws Exception {
            Customer customer = customerRepository.save(TestData.customer("hash"));

            ResultActions result = mockMvc.perform(get("/items/inventory")
                    .with(user(AuthenticatedUser.from(customer))));

            result.andExpect(status().isForbidden());
        }
    }
}
