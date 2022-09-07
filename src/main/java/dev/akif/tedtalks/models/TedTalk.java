package dev.akif.tedtalks.models;

import lombok.NonNull;

import java.time.LocalDate;

public record TedTalk(long id,
                      @NonNull String title,
                      @NonNull Author author,
                      @NonNull LocalDate date,
                      long views,
                      long likes,
                      @NonNull String link) {
}
