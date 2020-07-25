package kz.app.tasklist.backendspringboot.repository;

import kz.app.tasklist.backendspringboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // получить все значения, сортировка по найменованию.
    List<Category> findAllByOrderByTitleAsc();

}
