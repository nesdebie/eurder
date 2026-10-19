package org.example.eurder.repository;

import org.example.eurder.domain.customer.Customer;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository extends GenericRepository<Customer> {
    public CustomerRepository() {
        super(Customer.class);
    }

    public Customer save(Customer customer) { return saveWithId(customer.getId(), customer); }
}
