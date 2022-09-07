package dev.akif.tedtalks.services;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.errors.DuplicateTedTalk;
import dev.akif.tedtalks.errors.Errors;
import dev.akif.tedtalks.errors.TedTalkNotFound;
import dev.akif.tedtalks.models.TedTalk;
import dev.akif.tedtalks.repositories.AuthorRepository;
import dev.akif.tedtalks.repositories.TedTalkRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        try {
            val author = authors
                    .findByName(authorName)
                    .orElseGet(() -> {
                        log.info("Author with name '{}' does not exist, creating a new one", authorName);
                        return authors.save(new AuthorEntity(authorName));
                    });

            return tedTalks.save(new TedTalkEntity(title, author, link)).toModel();
        } catch (Exception e) {
            log.error("Error while creating a new TED talk with title '{}', author name '{}' and link '{}'", title, authorName, link, e);

            if (Errors.isDuplicateTedTalkError(e)) {
                throw new DuplicateTedTalk(title, authorName);
            }

            throw e;
        }
    }

    public @NonNull Page<TedTalk> list(@NonNull Pageable pageable, String search) {
        log.info("Listing all TED talks for page {} and search '{}'", pageable, search);

        val page = search == null ? tedTalks.findAll(pageable) : tedTalks.findAllSearching(search, pageable);

        return page.map(TedTalkEntity::toModel);
    }

    public @NonNull TedTalk get(long id) {
        log.info("Getting TED talk with id {}", id);

        return tedTalks
                .findById(id)
                .orElseThrow(() -> {
                    log.error("TED talk with id {} does not exist", id);
                    return new TedTalkNotFound(id);
                })
                .toModel();
    }

    @Transactional
    public @NonNull TedTalk update(long id,
                                   @NonNull String title,
                                   @NonNull String link) {
        log.info("Updating TED talk with id {} with title '{}' and link '{}'", id, title, link);

        val tedTalk = tedTalks
                .findById(id)
                .orElseThrow(() -> {
                    log.error("TED talk with id {} does not exist", id);
                    return new TedTalkNotFound(id);
                });

        val updatedEntity = tedTalk.toBuilder().title(title).link(link).build();

        try {
            return tedTalks.save(updatedEntity).toModel();
        } catch (Exception e) {
            log.error("Error while updating TED talk with id {} with title '{}' and link '{}'", id, title, link, e);

            if (Errors.isDuplicateTedTalkError(e)) {
                throw new DuplicateTedTalk(title, tedTalk.getAuthor().getName());
            }

            throw e;
        }
    }

    public void delete(long id) {
        log.info("Deleting TED talk with id {}", id);

        tedTalks
                .findById(id)
                .orElseThrow(() -> {
                    log.error("TED talk with id {} does not exist", id);
                    return new TedTalkNotFound(id);
                });

        tedTalks.deleteById(id);
    }
}
