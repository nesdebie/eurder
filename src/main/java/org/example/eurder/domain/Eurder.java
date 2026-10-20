package org.example.eurder.domain;

import org.example.eurder.domain.user.User;

import java.util.List;
import java.util.UUID;

public class Eurder {
    private final UUID id;
    private final List<ItemGroup> items;
    private final User user;

    public Eurder(List<ItemGroup> items, User user) {
        this.id = UUID.randomUUID();
        this.items = items;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public List<ItemGroup> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }
}
