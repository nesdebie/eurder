package org.example.eurder.feature.customer.service;

import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.api.FullNameMapper;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
import org.example.eurder.feature.customer.api.CustomerMapper;
import org.example.eurder.feature.customer.api.dto.CreatedCustomerDto;
import org.example.eurder.feature.customer.api.request.CreateCustomerRequest;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.customer.domain.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final FullNameMapper fullNameMapper;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AddressMapper addressMapper, FullNameMapper fullNameMapper, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressMapper = addressMapper;
        this.fullNameMapper = fullNameMapper;
        this.customerMapper = customerMapper;
    }

    public CreatedCustomerDto createCustomer(CreateCustomerRequest request) {
        String normalizedEmail = Customer.normalizeEmail(request.email());

        verifyIfEmailHasDuplicate(normalizedEmail);

        Customer customer = Customer.customer(
                normalizedEmail,
                passwordEncoder.encode(request.password()),
                fullNameMapper.mapToEmbeddable(request.fullName()),
                request.phoneNumber(),
                addressMapper.mapToEmbeddable(request.address())
        );

        try {
            return customerMapper.mapToCreatedDto(customerRepository.saveAndFlush(customer));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
    }


    private void verifyIfEmailHasDuplicate(String email) {
        customerRepository.findByEmail(email)
                .ifPresent(customer -> { throw new EmailAlreadyUsedException(email); });
    }
}