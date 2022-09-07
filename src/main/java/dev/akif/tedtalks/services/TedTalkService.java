package dev.akif.tedtalks.services;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.models.TedTalk;
import dev.akif.tedtalks.repositories.AuthorRepository;
import dev.akif.tedtalks.repositories.TedTalkRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class TedTalkService {
    private final AuthorRepository authors;
    private final TedTalkRepository tedTalks;

    @Transactional
    public @NonNull TedTalk create(@NonNull String title,
                                   @NonNull String authorName,
                                   @NonNull String link) {
        log.info("Creating a new TED talk with title '{}', author name '{}' and link '{}'", title, authorName, link);

        val author = authors
                .findByName(authorName)
                .orElseGet(() -> {
                    log.info("Author with name '{}' does not exist, creating a new one", authorName);
                    return authors.save(new AuthorEntity(authorName));
                });

        return tedTalks.save(new TedTalkEntity(title, author, link)).toModel();
    }

    public @NonNull Page<TedTalk> list(@NonNull Pageable pageable, String search) {
        log.info("Listing all TED talks for page {} and search '{}'", pageable, search);

        val page = search == null
                ? tedTalks.findAll(pageable)
                : tedTalks.findAll(searchSpecification(search), pageable);

        return page.map(TedTalkEntity::toModel);
    }

    public @NonNull Optional<TedTalk> get(long id) {
        log.info("Getting TED talk with id {}", id);

        return tedTalks
                .findById(id)
                .map(TedTalkEntity::toModel);
    }

    @Transactional
    public @NonNull Optional<TedTalk> update(long id,
                                             @NonNull String title,
                                             @NonNull String link) {
        log.info("Updating TED talk with id {} with title '{}' and link '{}'", id, title, link);

        return tedTalks
                .findById(id)
                .map(tedTalk -> {
                    tedTalk.setTitle(title);
                    tedTalk.setLink(link);
                    val updated = tedTalks.save(tedTalk);
                    return updated.toModel();
                });
    }

    public void delete(long id) {
        log.info("Deleting TED talk with id {}", id);

        tedTalks.deleteById(id);
    }

    private @NonNull Specification<TedTalkEntity> searchSpecification(@NonNull String search) {
        val lowerCase = search.toLowerCase();
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(TedTalkEntity.TITLE)), "%" + lowerCase + "%"),
                builder.like(builder.lower(root.join(TedTalkEntity.AUTHOR).get(AuthorEntity.NAME)), "%" + lowerCase + "%")
        );
    }
}
