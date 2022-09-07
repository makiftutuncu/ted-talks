package dev.akif.tedtalks.controllers;

import dev.akif.tedtalks.dtos.CreateTedTalkDTO;
import dev.akif.tedtalks.dtos.TedTalkDTO;
import dev.akif.tedtalks.dtos.UpdateTedTalkDTO;
import dev.akif.tedtalks.services.TedTalkService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/ted-talks")
@RestController
public class TedTalkController {
    private final TedTalkService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @NonNull TedTalkDTO create(@NonNull @RequestBody CreateTedTalkDTO dto) {
        return TedTalkDTO.from(service.create(dto.title(), dto.author(), dto.link()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @NonNull List<TedTalkDTO> list() {
        return service
                .list()
                .stream()
                .map(TedTalkDTO::from)
                .toList();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @NonNull ResponseEntity<TedTalkDTO> get(@PathVariable long id) {
        return service
                .get(id)
                .map(tedTalk -> ResponseEntity.ok(TedTalkDTO.from(tedTalk)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @NonNull ResponseEntity<TedTalkDTO> update(@PathVariable long id, @NonNull @RequestBody UpdateTedTalkDTO dto) {
        return service
                .update(id, dto.title(), dto.link())
                .map(tedTalk -> ResponseEntity.ok(TedTalkDTO.from(tedTalk)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable long id) {
        service.delete(id);
    }
}
