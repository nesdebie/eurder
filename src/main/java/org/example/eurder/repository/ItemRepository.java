package org.example.eurder.repository;

import org.example.eurder.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ItemRepository extends GenericRepository<Item> {
    public ItemRepository() { super(Item.class); }

    public Item save(Item item) { return saveWithId(item.getId(), item); }

    public Optional<Item> getOptional(String name) {
        return getOptional(item -> item.getName().equals(name));
    }
}
