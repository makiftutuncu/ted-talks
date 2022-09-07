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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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
        log.info("Creating a new TED talk with title: {}, author: {}, link: {}", title, authorName, link);

        val author = authors
                .findByName(authorName)
                .orElseGet(() -> {
                    log.info("Author with name {} does not exist, creating a new one", authorName);
                    return authors.save(new AuthorEntity(authorName));
                });

        return tedTalks.save(new TedTalkEntity(title, author, link)).toModel();
    }

    public @NonNull List<TedTalk> list() {
        log.info("Listing all TED talks");

        return StreamSupport
                .stream(tedTalks.findAll().spliterator(), false)
                .map(TedTalkEntity::toModel)
                .toList();
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
        log.info("Updating TED talk with id {} with title: {}, link: {}", id, title, link);

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
}
