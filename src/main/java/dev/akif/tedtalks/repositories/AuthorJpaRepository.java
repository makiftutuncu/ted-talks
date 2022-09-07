package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.AuthorEntity;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorJpaRepository extends CrudRepository<AuthorEntity, Long>, AuthorRepository {
    @Override
    default List<AuthorEntity> saveAll(@NonNull List<AuthorEntity> authors) {
        // Delegate to Spring
        return (List<AuthorEntity>) saveAll((Iterable<AuthorEntity>) authors);
    }
}
