package org.example.eurder.feature.security.api;

import io.swagger.v3.oas.annotations.Operation;
import org.example.eurder.feature.security.api.dto.CurrentUserDto;
import org.example.eurder.feature.security.authentication.AuthenticatedUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    @Operation(summary = "Check the credentials and return the logged-in user")
    public CurrentUserDto me(@AuthenticationPrincipal AuthenticatedUser user) {
        return new CurrentUserDto(
                user.email(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
