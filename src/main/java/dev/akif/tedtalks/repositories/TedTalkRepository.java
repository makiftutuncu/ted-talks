package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.TedTalkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TedTalkRepository extends CrudRepository<TedTalkEntity, Long> {
}
