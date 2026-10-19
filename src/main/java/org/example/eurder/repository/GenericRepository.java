package org.example.eurder.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class GenericRepository<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<UUID, T> storedItems = new ConcurrentHashMap<>();
    private final String typeName;

    public GenericRepository(Class<?> type) {
        this.typeName = type.getSimpleName();
    }
    public List<T> getAll() {
        logger.trace(
                "Fetching all items ({} {} stored)",
                storedItems.size(),
                typeName
        );
        return storedItems.values().stream().toList();
    }

    public Optional<T> get(UUID id) {
        logger.trace("Fetching item {} of the class {}", id, typeName);
        return Optional.ofNullable(storedItems.get(id));
    }

    protected T saveWithId(UUID id, T item) {
        logger.debug("Saving item {} in {} repository", id, typeName);
        storedItems.put(id, item);
        return item;
    }

    protected Optional<T> getOptional(Predicate<T> predicate) {
        return storedItems.values().stream().filter(predicate).findAny();
    }
}
