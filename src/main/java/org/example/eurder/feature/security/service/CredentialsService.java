package org.example.eurder.feature.security.service;


import org.example.eurder.feature.security.domain.Credentials;
import org.example.eurder.feature.security.domain.CredentialsRepository;
import org.example.eurder.feature.security.domain.Role;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CredentialsService {

    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialsService(CredentialsRepository credentialsRepository, PasswordEncoder passwordEncoder) {
        this.credentialsRepository = credentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Credentials create(String email, String rawPassword, Role role) {
        String normalizedEmail = Credentials.normalizeEmail(email);
        if (credentialsRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyUsedException(normalizedEmail);
        }
        String passwordHash = passwordEncoder.encode(rawPassword);
        try {
            return credentialsRepository.saveAndFlush(new Credentials(normalizedEmail, passwordHash, role));
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyUsedException(normalizedEmail, e);
        }
    }
}

