package dev.akif.tedtalks.dtos;

import dev.akif.tedtalks.models.TedTalk;
import lombok.NonNull;

import java.time.LocalDate;

public record TedTalkDTO(long id,
                         @NonNull String title,
                         @NonNull String author,
                         @NonNull LocalDate date,
                         long views,
                         long likes,
                         @NonNull String link) {
    public static @NonNull TedTalkDTO from(@NonNull TedTalk model) {
        return new TedTalkDTO(
                model.id(),
                model.title(),
                model.author().name(),
                model.date(),
                model.views(),
                model.likes(),
                model.link()
        );
    }
}
