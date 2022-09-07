package dev.akif.tedtalks.services;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.repositories.InMemoryAuthorRepository;
import dev.akif.tedtalks.repositories.InMemoryTedTalkRepository;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVServiceTest {
    @DisplayName("importing TED talks from CSV file")
    @Test
    void testImportTedTalks() {
        val authors = new InMemoryAuthorRepository();
        val tedTalks = new InMemoryTedTalkRepository(authors);
        val service = new CSVService(authors, tedTalks);

        service.importTedTalks();

        assertTrue(authors.findByName("Ozawa Bineshi Albert").isPresent());

        assertEquals(20, tedTalks.findAllSearching("leadership", PageRequest.of(0, 20)).getSize());
    }

    @DisplayName("processing line")
    @Test
    void testProcessingLine() {
        val authors = new ArrayList<AuthorEntity>();
        val tedTalks = new ArrayList<TedTalkEntity>();

        // Invalid CSV line
        CSVService.processLine("foo,bar", authors, tedTalks);
        assertTrue(authors.isEmpty());
        assertTrue(tedTalks.isEmpty());

        // Cannot parse stuff
        CSVService.processLine("title,author,date,views,likes,link", authors, tedTalks);
        assertTrue(authors.isEmpty());
        assertTrue(tedTalks.isEmpty());

        // Success
        CSVService.processLine("title,author,September 2022,0,0,link", authors, tedTalks);
        assertEquals(1, authors.size());
        assertEquals(1, tedTalks.size());

        val author = authors.get(0);
        assertNull(author.getId());
        assertEquals("author", author.getName());

        val tedTalk = tedTalks.get(0);
        assertNull(tedTalk.getId());
        assertEquals("title", tedTalk.getTitle());
        assertEquals(author, tedTalk.getAuthor());
        assertEquals(LocalDate.of(2022, 9, 1), tedTalk.getDate());
        assertEquals(0, tedTalk.getLikes());
        assertEquals(0, tedTalk.getViews());
        assertEquals("link", tedTalk.getLink());
    }
}
