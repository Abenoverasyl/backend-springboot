package kz.app.tasklist.backendspringboot.controller;

import kz.app.tasklist.backendspringboot.entity.Task;
import kz.app.tasklist.backendspringboot.repository.TaskRepository;
import kz.app.tasklist.backendspringboot.search.TaskSearchValues;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static kz.app.tasklist.backendspringboot.util.MyLogger.showMethodName;

@RestController
@RequestMapping("/task")
public class TaskController {
    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/all")
    public List<Task> getAll() {
        showMethodName("TaskController: getAll() -----------------------------------------");
        return taskRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task){
        showMethodName("TaskController: add() -----------------------------------------");
        // проверка на объязательные параметры
        if (task.getId() != null && task.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поетому его передавать не нужно,
            // иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения title
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        // save работает как на дабавление, так и на обновления
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Task task) {
        showMethodName("TaskController: update() -----------------------------------------");
        // проверка на объязательные параметры
        if (task.getId() == null && task.getId() == 0) {
            // id создается автоматически в БД (autoincrement), поетому его передавать не нужно,
            // иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значения title
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        taskRepository.save(task);
        // save работает как на дабавление, так и на обновления
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        showMethodName("TaskController: findById() -----------------------------------------");
        Task task = null;

        try {
            task = taskRepository.findById(id).get();
        } catch (NoSuchElementException e) { // if object not found
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity deleteById(@PathVariable Long id) {
        showMethodName("TaskController: deleteById() -----------------------------------------");
        try {
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        taskRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Task>> search(@RequestBody TaskSearchValues taskSearchValues) {
        showMethodName("TaskController: search() -----------------------------------------");

        String title = taskSearchValues.getTitle();
        int completed = taskSearchValues.getCompleted();
        Long priority = taskSearchValues.getPriorityId();
        Long category = taskSearchValues.getCategoryId();
        return ResponseEntity.ok(taskRepository.findByParams(title, completed, priority, category));
    }
}
