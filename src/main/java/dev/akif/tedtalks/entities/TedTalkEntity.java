package dev.akif.tedtalks.entities;

import dev.akif.tedtalks.models.TedTalk;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity(name = "ted_talk")
@NoArgsConstructor
@Table(name = "ted_talks")
public class TedTalkEntity {
    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private @NonNull String title;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private @NonNull AuthorEntity author;

    @Column(name = "date", nullable = false)
    private @NonNull LocalDate date;

    @Column(name = "views", nullable = false)
    private long views;

    @Column(name = "likes", nullable = false)
    private long likes;

    @Column(name = "link", nullable = false)
    private @NonNull String link;

    public TedTalkEntity(@NonNull String title,
                         @NonNull AuthorEntity author,
                         @NonNull String link) {
        this();
        setTitle(title);
        setAuthor(author);
        setDate(LocalDate.now());
        setViews(0);
        setLikes(0);
        setLink(link);
    }

    public @NonNull TedTalk toModel() {
        return new TedTalk(id, title, author.toModel(), date, views, likes, link);
    }
}