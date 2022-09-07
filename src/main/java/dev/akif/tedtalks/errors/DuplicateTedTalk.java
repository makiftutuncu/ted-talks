package dev.akif.tedtalks.errors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Value
public class DuplicateTedTalk extends RuntimeException {
    @NonNull
    public String title;
    @NonNull
    public String author;
}
