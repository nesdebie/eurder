package org.example.eurder.service;

import org.example.eurder.domain.user.Permission;
import org.example.eurder.domain.user.User;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final UserRepository repository;

    public SecurityService(UserRepository repository) {
        this.repository = repository;
    }

    public void validateAuthorization(String email, Permission permission) {
        if (email == null) throw UnauthorizedException.invalidCredentials();

        User user = repository.getUserWithEmail(email)
                .orElseThrow(UnauthorizedException::invalidCredentials);

        if (!user.hasPermission(permission)) throw permission.getException();
    }
}