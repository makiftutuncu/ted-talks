package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.TedTalkEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TedTalkRepository extends PagingAndSortingRepository<TedTalkEntity, Long>, JpaSpecificationExecutor<TedTalkEntity> {
}
