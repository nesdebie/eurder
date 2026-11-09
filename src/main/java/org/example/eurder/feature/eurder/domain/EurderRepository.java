package org.example.eurder.feature.eurder.domain;

import org.example.eurder.feature.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EurderRepository extends JpaRepository<Eurder, UUID> {
    List<Eurder> findAllByCustomer(Customer customer);
}
