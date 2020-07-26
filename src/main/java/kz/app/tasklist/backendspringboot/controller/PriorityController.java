package kz.app.tasklist.backendspringboot.controller;

import kz.app.tasklist.backendspringboot.entity.Priority;
import kz.app.tasklist.backendspringboot.repository.PriorityRepository;
import kz.app.tasklist.backendspringboot.search.PrioritySearchValues;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static kz.app.tasklist.backendspringboot.util.MyLogger.showMethodName;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityRepository priorityRepository;

    public PriorityController(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @GetMapping("/all")
    public List<Priority> getAll() {
        showMethodName("PriorityController: getAll() -----------------------------------------");
        return priorityRepository.findAllByOrderByIdAsc();
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority){
        showMethodName("PriorityController: add() -----------------------------------------");
        // проверка на объязательные параметры
        if (priority.getId() != null && priority.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поетому его передавать не нужно,
            // иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения color
        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        // save работает как на дабавление, так и на обновления
        return ResponseEntity.ok(priorityRepository.save(priority));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        showMethodName("PriorityController: update() -----------------------------------------");
        // проверка на объязательные параметры
        if (priority.getId() == null && priority.getId() == 0) {
            // id создается автоматически в БД (autoincrement), поетому его передавать не нужно,
            // иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения color
        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }

        // save работает как на дабавление, так и на обновления
        return ResponseEntity.ok(priorityRepository.save(priority));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Priority> findById(@PathVariable Long id) {
        showMethodName("PriorityController: findById() -----------------------------------------");
        Priority priority = null;

        try {
            priority = priorityRepository.findById(id).get();
        } catch (NoSuchElementException e) { // if object not found
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity deleteById(@PathVariable Long id) {
        showMethodName("PriorityController: deleteById() -----------------------------------------");
        try {
            priorityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {
        showMethodName("PriorityController: search() -----------------------------------------");
        // если вместо текста будет пусто или null - вернутся все категории
        return ResponseEntity.ok(priorityRepository.findByTitle(prioritySearchValues.getText()));
    }
}
