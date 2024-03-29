package dev.akif.tedtalks.services;

import dev.akif.tedtalks.entities.AuthorEntity;
import dev.akif.tedtalks.entities.TedTalkEntity;
import dev.akif.tedtalks.repositories.AuthorRepository;
import dev.akif.tedtalks.repositories.TedTalkRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CSVService {
    private final AuthorRepository authors;
    private final TedTalkRepository tedTalks;

    static void processLine(@NonNull String line, @NonNull ArrayList<AuthorEntity> authorEntities, @NonNull ArrayList<TedTalkEntity> tedTalkEntities) {
        try {
            val parts = line.split(",");

            if (parts.length != 6) {
                log.warn("Invalid CSV line: {}", line);
                return;
            }

            val title = parts[0].trim();
            val authorName = parts[1].trim();
            val dateString = parts[2].trim();
            val viewsString = parts[3].trim();
            val likesString = parts[4].trim();
            val link = parts[5].trim();

            val date = YearMonth.parse(dateString, DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)).atDay(1);
            val views = Long.parseLong(viewsString);
            val likes = Long.parseLong(likesString);

            val authorEntity = new AuthorEntity(authorName);
            log.debug("Found author {}", authorEntity);
            authorEntities.add(authorEntity);

            val tedTalkEntity = new TedTalkEntity(title, authorEntity, date, views, likes, link);
            log.debug("Found TED talk {}", tedTalkEntity);
            tedTalkEntities.add(tedTalkEntity);
        } catch (Exception e) {
            log.error("Error while processing line '{}'", line, e);
        }
    }

    @Transactional
    public void importTedTalks() {
        log.info("Importing TED talks from CSV file");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("data.csv").getInputStream()))) {
            val authorEntities = new ArrayList<AuthorEntity>();
            val tedTalkEntities = new ArrayList<TedTalkEntity>();

            reader.lines().skip(1).forEach(line -> processLine(line, authorEntities, tedTalkEntities));

            val authorEntitiesToSave = authorEntities.stream().distinct().toList();

            val authorNamesToAuthorEntities = authors
                    .saveAll(authorEntitiesToSave)
                    .stream()
                    .collect(Collectors.toMap(AuthorEntity::getName, author -> author));

            val tedTalkEntitiesToSave = tedTalkEntities
                    .stream()
                    .peek(tedTalk -> tedTalk.setAuthor(authorNamesToAuthorEntities.get(tedTalk.getAuthor().getName())))
                    .distinct()
                    .toList();

            tedTalks.saveAll(tedTalkEntitiesToSave);

            log.info("Imported {} TED talks by {} authors", tedTalkEntitiesToSave.size(), authorEntitiesToSave.size());
        } catch (Exception e) {
            log.error("Error while importing CSV file", e);
        }
    }
}
