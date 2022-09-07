package dev.akif.tedtalks.dtos;

import lombok.NonNull;

public record UpdateTedTalkDTO(@NonNull String title,
                               @NonNull String link) {
}
