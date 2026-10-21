package org.example.eurder.repository;

import org.example.eurder.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemRepository extends GenericRepository<Item> {
    public ItemRepository() {
        super(Item.class);
        this.save(new Item("Warhammer 40K", "Starter Set", 199.99, 1));
        this.save(new Item("Warhammer", "[DEPRECATED]", 99.99, 8));
        this.save(new Item("Warhammer AoS", "Ultimate Starter Set", 99.99, 15));
    }

    public Item save(Item item) { return saveWithId(item.getId(), item); }

    public Optional<Item> getOptionalByName(String name) {
        return getOptional(item -> item.getName().equals(name));
    }

    public Optional<Item> getOptionalById(UUID id) { return getOptional(item -> item.getId().equals(id)); }
}
