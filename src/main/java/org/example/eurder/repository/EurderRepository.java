package org.example.eurder.repository;

import org.example.eurder.domain.Eurder;
import org.springframework.stereotype.Repository;

@Repository
public class EurderRepository extends GenericRepository<Eurder> {
    public EurderRepository() { super(Eurder.class); }

    public Eurder save(Eurder eurder) { return saveWithId(eurder.getId(), eurder); }
}
