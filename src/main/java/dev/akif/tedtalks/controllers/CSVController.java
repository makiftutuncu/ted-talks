package dev.akif.tedtalks.controllers;

import dev.akif.tedtalks.services.CSVService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/csv")
@RestController
public class CSVController {
    private final CSVService csv;

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    void importTedTalks() {
        csv.importTedTalks();
    }
}
