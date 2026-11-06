package org.example.eurder.feature.security.authentication;

import org.example.eurder.feature.customer.domain.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public record AuthenticatedUser(UUID id,
                                String email,
                                String passwordHash,
                                List<GrantedAuthority> authorities) implements UserDetails {

    public static AuthenticatedUser from(Customer customer) {
        List<GrantedAuthority> authorities = customer.getRole().getPermissions().stream()
                .<GrantedAuthority>map(feature -> new SimpleGrantedAuthority(feature.name()))
                .toList();
        return new AuthenticatedUser(customer.getId(), customer.getEmail(), customer.getPasswordHash(), authorities);
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}