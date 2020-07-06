package kz.app.tasklist.backendspringboot.controller;

import kz.app.tasklist.backendspringboot.entity.Priority;
import kz.app.tasklist.backendspringboot.repository.PriorityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityRepository priorityRepository;

    public PriorityController(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @GetMapping("/all")
    public List<Priority> getAll() {
        return priorityRepository.findAll();
    }

    @PostMapping("/add")
    public Priority add(@RequestBody Priority priority){
        return priorityRepository.save(priority);
    }
}
