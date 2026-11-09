package org.example.eurder.feature.customer.service;

import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.api.FullNameMapper;
import org.example.eurder.feature.customer.exception.CustomerNotFoundException;
import org.example.eurder.feature.eurder.service.EurderService;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
import org.example.eurder.feature.customer.api.CustomerMapper;
import org.example.eurder.feature.customer.api.dto.CustomerDto;
import org.example.eurder.feature.customer.api.request.CreateCustomerRequest;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.customer.domain.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final FullNameMapper fullNameMapper;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AddressMapper addressMapper, FullNameMapper fullNameMapper, CustomerMapper customerMapper, EurderService eurderService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressMapper = addressMapper;
        this.fullNameMapper = fullNameMapper;
        this.customerMapper = customerMapper;
    }

    public CustomerDto createCustomer(CreateCustomerRequest request) {
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
            return customerMapper.mapToDto(customerRepository.saveAndFlush(customer));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
    }

    public List<CustomerDto> getAllCustomers() {
        return customerMapper.mapToDto(customerRepository.findAllBy());
    }

    public CustomerDto getCustomerDetails(UUID customerId) {
        return customerMapper.mapToDto(
                getCustomerById(customerId)
        );
    }


    private void verifyIfEmailHasDuplicate(String email) {
        customerRepository.findByEmail(email)
                .ifPresent(customer -> { throw new EmailAlreadyUsedException(email); });
    }

    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> CustomerNotFoundException.customerIdNotFound(id));
    }
}