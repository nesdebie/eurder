package org.example.eurder.repository;

import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.ItemGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class EurderRepository extends GenericRepository<Eurder> {
    public EurderRepository() { super(Eurder.class); }

    public Eurder save(Eurder eurder) { return saveWithId(eurder.getId(), eurder); }

    public List<ItemGroup> getItemGroupsByUserId(UUID id) {
        return getAll().stream()
                .filter(eurder -> eurder.getUser().getId().equals(id))
                .flatMap(eurder -> eurder.getItems().stream())
                .toList();
    }
}
