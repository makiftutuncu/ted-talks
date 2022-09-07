package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.AuthorEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<AuthorEntity> findByName(@NonNull String name);

    AuthorEntity save(@NonNull AuthorEntity author);

    List<AuthorEntity> saveAll(@NonNull List<AuthorEntity> authors);
}
