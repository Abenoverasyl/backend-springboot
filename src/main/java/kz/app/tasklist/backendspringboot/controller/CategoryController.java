package kz.app.tasklist.backendspringboot.controller;

import kz.app.tasklist.backendspringboot.entity.Category;
import kz.app.tasklist.backendspringboot.repository.CategoryRepository;
import kz.app.tasklist.backendspringboot.search.CategorySearchValues;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static kz.app.tasklist.backendspringboot.util.MyLogger.showMethodName;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/all")
    public List<Category> findAll() {
        showMethodName("CategoryController: findAll() -----------------------------------------");
        return categoryRepository.findAllByOrderByTitleAsc();
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Category category) {
        showMethodName("CategoryController: add() -----------------------------------------");
        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (category.getTitle() != null && category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryRepository.save(category);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {
        showMethodName("CategoryController: update() -----------------------------------------");
        // проверка на объязательные параметры
        if (category.getId() == 0)
            if (category.getId() == null) {
                // id создается автоматически в БД (autoincrement), поетому его передавать не нужно,
                // иначе может быть конфликт уникальности значения
                return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
            }

        // если передали пустое значения title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryRepository.save(category);
        // save работает как на дабавление, так и на обновления
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity findById(@PathVariable Long id) {

        showMethodName("CategoryController: findById() -----------------------------------------");
        Category category;

        try {
            category = categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) { // if object not found
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity deleteById(@PathVariable Long id) {
        showMethodName("CategoryController: deleteById() -----------------------------------------");
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id=" + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    // пойск по любым параметром CategorySearchValues
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {
        showMethodName("CategoryController: search() -----------------------------------------");
        // если вместо текста будет пусто или null - вернутся все категории
        return ResponseEntity.ok(categoryRepository.findByTitle(categorySearchValues.getText()));
    }



}
