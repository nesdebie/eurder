package org.example.eurder.feature.security.config;

import org.example.eurder.feature.security.domain.Permission;
import org.example.eurder.feature.security.domain.Role;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class SecurityOpenApiConfig {

    @Bean
    public OperationCustomizer requiredPermissionDescription() {
        return (operation, handlerMethod) -> {
            Secured secured = handlerMethod.getMethodAnnotation(Secured.class);
            String access = secured == null
                    ? "Public: no authentication needed."
                    : Arrays.stream(secured.value()).map(SecurityOpenApiConfig::requirement).collect(Collectors.joining(" "));
            operation.setDescription(operation.getDescription() == null ? access : operation.getDescription() + "\n\n" + access);
            return operation;
        };
    }

    private static String requirement(String permission) {
        String roles = Arrays.stream(Role.values())
                .filter(role -> role.getPermissions().contains(Permission.valueOf(permission)))
                .map(Role::name)
                .collect(Collectors.joining(", "));
        return "Requires the " + permission + " permission (role " + roles + ").";
    }
}
