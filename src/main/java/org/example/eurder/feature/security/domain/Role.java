package org.example.eurder.feature.security.domain;

import java.util.Set;

public enum Role {
    CUSTOMER(Permission.EURDER_ITEMS),
    ADMIN(Permission.ADD_ITEM, Permission.UPDATE_ITEM);

    private final Set<Permission> permissions;

    Role(Permission... permissions) {
        this.permissions = Set.of(permissions);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
