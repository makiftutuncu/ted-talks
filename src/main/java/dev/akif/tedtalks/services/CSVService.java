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

import java.nio.file.Files;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
@Slf4j
public class CSVService {
    private final AuthorRepository authors;
    private final TedTalkRepository tedTalks;

    @Transactional
    public void importTedTalks() {
        try {
            log.info("Importing TED talks from CSV file");

            val resource = new ClassPathResource("data.csv");
            val fileContent = Files.readString(resource.getFile().toPath());

            val authorEntities = new ArrayList<AuthorEntity>();
            val tedTalkEntities = new ArrayList<TedTalkEntity>();

            Arrays.stream(fileContent.split("\n")).skip(1).forEach(line -> processLine(line, authorEntities, tedTalkEntities));

            val authorEntitiesToSave = authorEntities.stream().distinct().toList();

            val authorNamesToAuthorEntities = StreamSupport
                    .stream(authors.saveAll(authorEntitiesToSave).spliterator(), true)
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

    private void processLine(@NonNull String line, @NonNull ArrayList<AuthorEntity> authorEntities, @NonNull ArrayList<TedTalkEntity> tedTalkEntities) {
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
}
