package org.example.eurder.feature.eurder.domain;

import org.example.eurder.feature.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EurderRepository extends JpaRepository<Eurder, UUID> {
    @Query("""
            select distinct e from Eurder e
            join fetch e.customer
            join e.itemGroups ig
            where ig.shippingDate = :date
            """)
    List<Eurder> findAllWithItemGroupsShippingOn(LocalDate date);

    List<Eurder> findAllByCustomer(Customer customer);
}
