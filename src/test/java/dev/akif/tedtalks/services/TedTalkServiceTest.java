package dev.akif.tedtalks.services;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.errors.DuplicateTedTalk;
import dev.akif.tedtalks.errors.TedTalkNotFound;
import dev.akif.tedtalks.models.Author;
import dev.akif.tedtalks.models.TedTalk;
import dev.akif.tedtalks.repositories.AuthorRepository;
import dev.akif.tedtalks.repositories.InMemoryAuthorRepository;
import dev.akif.tedtalks.repositories.InMemoryTedTalkRepository;
import dev.akif.tedtalks.repositories.TedTalkRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TedTalkServiceTest {
    private final Pageable defaultPageRequest = PageRequest.of(0, 20);
    private AuthorRepository authors = new InMemoryAuthorRepository();
    private TedTalkRepository tedTalks = new InMemoryTedTalkRepository(authors);
    private TedTalkService service = new TedTalkService(authors, tedTalks);

    @BeforeEach
    void beforeEach() {
        authors = new InMemoryAuthorRepository();
        tedTalks = new InMemoryTedTalkRepository(authors);
        service = new TedTalkService(authors, tedTalks);
    }

    @DisplayName("creating a new TED talk")
    @Test
    void testCreate() throws DuplicateTedTalk {
        val before = tedTalks.findById(1);
        assertTrue(before.isEmpty());

        val created = service.create("title", "author", "link");
        val expected = new TedTalk(1, "title", new Author(1, "author"), LocalDate.now(), 0, 0, "link");

        assertEquals(expected, created);

        val after = tedTalks.findById(1);
        assertTrue(after.isPresent());
        assertEquals(expected, after.get().toModel());

        val error = assertThrows(DuplicateTedTalk.class, () -> service.create("title", "author", "link"));
        assertEquals(new DuplicateTedTalk("title", "author"), error);
    }

    @DisplayName("listing all TED talks")
    @Test
    void testList() {
        val authorEntity1 = authors.save(new AuthorEntity("author 1 test"));
        val authorEntity2 = authors.save(new AuthorEntity("author 2"));

        val tedTalkEntity1 = tedTalks.save(new TedTalkEntity("title 1", authorEntity1, "link1"));
        val tedTalkEntity2 = tedTalks.save(new TedTalkEntity("title 2", authorEntity2, "link2"));
        val tedTalkEntity3 = tedTalks.save(new TedTalkEntity("title 3 test", authorEntity1, "link3"));

        // No filter, no paging
        assertEquals(
                List.of(tedTalkEntity1.toModel(), tedTalkEntity2.toModel(), tedTalkEntity3.toModel()),
                service.list(defaultPageRequest, null).getContent()
        );

        // Filter by title or author name
        assertEquals(
                List.of(tedTalkEntity1.toModel(), tedTalkEntity3.toModel()),
                service.list(defaultPageRequest, "test").getContent()
        );

        // Paginate
        assertEquals(
                List.of(tedTalkEntity3.toModel()),
                service.list(PageRequest.of(1, 2), null).getContent()
        );
    }

    @DisplayName("getting a TED talk")
    @Test
    void testGet() {
        val tedTalkEntity = tedTalks.save(new TedTalkEntity("title", authors.save(new AuthorEntity("author")), "link"));

        val error = assertThrows(TedTalkNotFound.class, () -> service.get(-1));
        assertEquals(new TedTalkNotFound(-1), error);
    }

    @DisplayName("updating a TED talk")
    @Test
    void testUpdate() {
        val tedTalkEntity = tedTalks.save(new TedTalkEntity("title", authors.save(new AuthorEntity("author")), "link"));

        val error = assertThrows(TedTalkNotFound.class, () -> service.update(-1, "title updated", "link updated"));
        assertEquals(new TedTalkNotFound(-1), error);

        val updated = service.update(tedTalkEntity.getId(), "title updated", "link updated");
        assertEquals("title updated", updated.title());
        assertEquals("link updated", updated.link());
    }

    @DisplayName("deleting a TED talk")
    @Test
    void testDelete() {
        val tedTalkEntity = tedTalks.save(new TedTalkEntity("title", authors.save(new AuthorEntity("author")), "link"));
        val id = tedTalkEntity.getId();

        assertDoesNotThrow(() -> service.delete(id));

        val error = assertThrows(TedTalkNotFound.class, () -> service.delete(id));
        assertEquals(new TedTalkNotFound(id), error);
    }
}
