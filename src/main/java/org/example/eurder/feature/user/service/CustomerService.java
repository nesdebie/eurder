package org.example.eurder.feature.user.service;

import jakarta.transaction.Transactional;
import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
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

    public CustomerService(CustomerRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer createCustomer(String email, String rawPassword,
                                   FullName fullName, String phoneNumber, Address address) {
        String normalizedEmail = Customer.normalizeEmail(email);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
        Customer customer = Customer.customer(normalizedEmail, passwordEncoder.encode(rawPassword),
                fullName, phoneNumber, address);
        try {
            return userRepository.saveAndFlush(customer);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
    }
}