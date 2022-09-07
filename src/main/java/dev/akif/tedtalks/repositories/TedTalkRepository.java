package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.TedTalkEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TedTalkRepository extends PagingAndSortingRepository<TedTalkEntity, Long> {
    @NonNull Page<TedTalkEntity> findAll(@NonNull Pageable pageable);
}
