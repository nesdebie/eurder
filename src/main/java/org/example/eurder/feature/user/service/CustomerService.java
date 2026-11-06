package org.example.eurder.feature.user.service;

import jakarta.transaction.Transactional;
import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.api.FullNameMapper;
import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
import org.example.eurder.feature.user.api.CustomerMapper;
import org.example.eurder.feature.user.api.dto.CreatedCustomerDto;
import org.example.eurder.feature.user.api.request.CreateCustomerRequest;
import org.example.eurder.feature.user.domain.Customer;
import org.example.eurder.feature.user.domain.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final FullNameMapper fullNameMapper;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository userRepository, PasswordEncoder passwordEncoder, AddressMapper addressMapper, FullNameMapper fullNameMapper, CustomerMapper customerMapper) {
        this.userRepository = userRepository;
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
            return customerMapper.mapToCreatedDto(userRepository.saveAndFlush(customer));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
    }


    private void verifyIfEmailHasDuplicate(String email) {
        userRepository.findByEmail(email).
                orElseThrow(() -> new EmailAlreadyUsedException(email));
    }
}