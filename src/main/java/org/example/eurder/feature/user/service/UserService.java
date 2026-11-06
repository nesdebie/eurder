package org.example.eurder.feature.user.service;

import jakarta.transaction.Transactional;
import org.example.eurder.common.domain.Address;
import org.example.eurder.common.domain.FullName;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
import org.example.eurder.feature.user.domain.User;
import org.example.eurder.feature.user.domain.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createCustomer(String email, String rawPassword,
                               FullName fullName, String phoneNumber, Address address) {
        String normalizedEmail = User.normalizeEmail(email);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
        User customer = User.customer(normalizedEmail, passwordEncoder.encode(rawPassword),
                fullName, phoneNumber, address);
        try {
            return userRepository.saveAndFlush(customer);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
    }
}