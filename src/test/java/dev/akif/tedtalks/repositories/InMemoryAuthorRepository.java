package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.AuthorEntity;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAuthorRepository implements AuthorRepository {
    private final Map<Long, AuthorEntity> db = new HashMap<>();

    private long nextId = 1;

    @Override
    public Optional<AuthorEntity> findByName(@NonNull String name) {
        return db
                .values()
                .stream()
                .filter(a -> a.getName().equals(name))
                .findFirst();
    }

    @Override
    public AuthorEntity save(@NonNull AuthorEntity author) {
        if (author.getId() == null) {
            author.setId(nextId);
            nextId++;
        }
        db.put(author.getId(), author);
        return author;
    }

    @Override
    public List<AuthorEntity> saveAll(@NonNull List<AuthorEntity> authors) {
        return authors
                .stream()
                .map(this::save)
                .toList();
    }
}
