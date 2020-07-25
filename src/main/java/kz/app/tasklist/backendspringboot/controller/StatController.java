package kz.app.tasklist.backendspringboot.controller;

import kz.app.tasklist.backendspringboot.entity.Stat;
import kz.app.tasklist.backendspringboot.repository.StatRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class StatController {

    private StatRepository statRepository;

    private final Long defaultId = 1L;

    public StatController(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @GetMapping("/stat")
    public ResponseEntity<Stat> getAll() {
        return ResponseEntity.ok(statRepository.findById(defaultId).get());
    }
}
