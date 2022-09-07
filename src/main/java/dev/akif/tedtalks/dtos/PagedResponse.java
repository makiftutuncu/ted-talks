package dev.akif.tedtalks.dtos;

import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<A>(@NonNull List<A> data,
                               int page,
                               int perPage,
                               int totalPages) {
    public PagedResponse(@NonNull Page<A> page) {
        this(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages()
        );
    }
}
