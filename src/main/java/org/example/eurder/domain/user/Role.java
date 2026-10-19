package org.example.eurder.domain.user;

import java.util.List;

public enum Role {
    CUSTOMER(
            List.of(Permission.ORDER_ITEM)
    ),
    ADMIN(
            List.of(Permission.ADD_ITEM,
                    Permission.UPDATE_ITEM)
    );

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> permissions() { return permissions; }
}
