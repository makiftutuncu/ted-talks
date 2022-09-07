package dev.akif.tedtalks.dtos;

import lombok.NonNull;

public record CreateTedTalkDTO(@NonNull String title,
                               @NonNull String author,
                               @NonNull String link) {
}
