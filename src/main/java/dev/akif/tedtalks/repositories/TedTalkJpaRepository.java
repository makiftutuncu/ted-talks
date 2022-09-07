package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import lombok.NonNull;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TedTalkJpaRepository extends PagingAndSortingRepository<TedTalkEntity, Long>, JpaSpecificationExecutor<TedTalkEntity>, TedTalkRepository {
    @Override
    default @NonNull List<TedTalkEntity> saveAll(@NonNull List<TedTalkEntity> tedTalks) {
        // Delegate to Spring
        return (List<TedTalkEntity>) saveAll((Iterable<TedTalkEntity>) tedTalks);
    }

    @Override
    default @NonNull Page<TedTalkEntity> findAllSearching(@NonNull String search, @NonNull Pageable pageable) {
        return findAll(searchSpecification(search), pageable);
    }

    private @NonNull Specification<TedTalkEntity> searchSpecification(@NonNull String search) {
        val lowerCase = search.toLowerCase();
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(TedTalkEntity.TITLE)), "%" + lowerCase + "%"),
                builder.like(builder.lower(root.join(TedTalkEntity.AUTHOR).get(AuthorEntity.NAME)), "%" + lowerCase + "%")
        );
    }
}
