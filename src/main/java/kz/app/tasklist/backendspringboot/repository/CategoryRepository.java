package kz.app.tasklist.backendspringboot.repository;

import kz.app.tasklist.backendspringboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // IF TITLE == NULL OR =='', THEN WE GET ALL VALUES
    @Query("SELECT c FROM Category c WHERE " +
            "(:title IS NULL OR :title='' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%') )) " +
            "ORDER BY c.title ASC")
    List<Category> findByTitle(@Param("title") String title);

    // получить все значения, сортировка по найменованию.
    List<Category> findAllByOrderByTitleAsc();

}
