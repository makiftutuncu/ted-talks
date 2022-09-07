package dev.akif.tedtalks.entities;

import dev.akif.tedtalks.models.Author;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "author")
@NoArgsConstructor
@Table(name = "authors")
public class AuthorEntity {
    public static final String NAME = "name";

    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private @NonNull String name;

    public AuthorEntity(@NonNull String name) {
        this();
        setName(name);
    }

    public @NonNull Author toModel() {
        return new Author(id, name);
    }
}
