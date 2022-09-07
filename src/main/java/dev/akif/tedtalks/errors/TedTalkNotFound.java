package dev.akif.tedtalks.errors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Value
public class TedTalkNotFound extends RuntimeException {
    public long id;
}
