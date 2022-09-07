package dev.akif.tedtalks.repositories;

import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.errors.DuplicateTedTalk;
import lombok.NonNull;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryTedTalkRepository implements TedTalkRepository {
    private final AuthorRepository authors;

    private final Map<Long, TedTalkEntity> db = new HashMap<>();

    private long nextId = 1;

    public InMemoryTedTalkRepository(AuthorRepository authors) {
        this.authors = authors;
    }

    @Override
    public @NonNull TedTalkEntity save(@NonNull TedTalkEntity tedTalk) {
        db
                .values()
                .stream()
                .filter(t -> t.getTitle().equals(tedTalk.getTitle()) && Objects.equals(t.getAuthor().getId(), tedTalk.getAuthor().getId()))
                .findAny()
                .ifPresent(t -> {
                    throw new DuplicateTedTalk(tedTalk.getTitle(), tedTalk.getAuthor().getName());
                });

        if (tedTalk.getId() == null) {
            tedTalk.setId(nextId);
            nextId++;
        }
        authors.save(tedTalk.getAuthor());
        db.put(tedTalk.getId(), tedTalk);
        return tedTalk;
    }

    @Override
    public @NonNull List<TedTalkEntity> saveAll(@NonNull List<TedTalkEntity> tedTalks) {
        return tedTalks
                .stream()
                .map(this::save)
                .toList();
    }

    @Override
    public @NonNull Page<TedTalkEntity> findAll(@NonNull Pageable pageable) {
        val tedTalks = db
                .values()
                .stream()
                .sorted(Comparator.comparing(TedTalkEntity::getTitle))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(tedTalks, pageable, db.size());
    }

    @Override
    public @NonNull Page<TedTalkEntity> findAllSearching(@NonNull String search, @NonNull Pageable pageable) {
        val tedTalks = db
                .values()
                .stream()
                .filter(t -> t.getTitle().toLowerCase().contains(search) || t.getAuthor().getName().toLowerCase().contains(search))
                .sorted(Comparator.comparing(TedTalkEntity::getTitle))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(tedTalks, pageable, db.size());
    }

    @Override
    public @NonNull Optional<TedTalkEntity> findById(long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void deleteById(long id) {
        db.remove(id);
    }
}
