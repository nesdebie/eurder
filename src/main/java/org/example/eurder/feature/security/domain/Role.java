package org.example.eurder.feature.security.domain;

import java.util.Set;

public enum Role {
    CUSTOMER(
            Permission.EURDER_ITEMS,
            Permission.REEURDER_ITEMS,
            Permission.VIEW_EURDERS_REPORT),
    ADMIN(
            Permission.ADD_ITEM,
            Permission.UPDATE_ITEM,
            Permission.VIEW_CUSTOMERS,
            Permission.VIEW_CUSTOMER);

    private final Set<Permission> permissions;

    Role(Permission... permissions) {
        this.permissions = Set.of(permissions);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
