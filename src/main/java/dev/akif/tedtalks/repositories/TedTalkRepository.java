package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.TedTalkEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TedTalkRepository {
    @NonNull TedTalkEntity save(@NonNull TedTalkEntity tedTalk);

    @NonNull List<TedTalkEntity> saveAll(@NonNull List<TedTalkEntity> tedTalks);

    @NonNull Page<TedTalkEntity> findAll(@NonNull Pageable pageable);

    @NonNull Page<TedTalkEntity> findAllSearching(@NonNull String search, @NonNull Pageable pageable);

    @NonNull Optional<TedTalkEntity> findById(long id);

    void deleteById(long id);
}
