package dev.akif.tedtalks.models;

import lombok.NonNull;

public record Author(long id, @NonNull String name) {
}
