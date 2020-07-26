package kz.app.tasklist.backendspringboot.repository;

import kz.app.tasklist.backendspringboot.entity.Category;
import kz.app.tasklist.backendspringboot.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    // IF TITLE == NULL OR =='', THEN WE GET ALL VALUES
    @Query("SELECT c FROM Priority c WHERE " +
            "(:title IS NULL OR :title='' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%') )) " +
            "ORDER BY c.title ASC")
    List<Priority> findByTitle(@Param("title") String title);

    // Получить все значения, сортировка по id
    List<Priority> findAllByOrderByIdAsc();
}
