package kz.app.tasklist.backendspringboot.repository;

import kz.app.tasklist.backendspringboot.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT c FROM Task c WHERE " +
            "(:title IS NULL OR :title='' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%') )) and" +
            "(:completed is null or c.completed=:completed) and " +
            "(:priorityId is null or c.priority.id=:priorityId) and " +
            "(:categoryId is null or c.category.id=:categoryId)"
    )

    // искать по всем переданным параметрам (пустые параметры учитыватся не будет)
    List<Task> findByParams(@Param("title") String title, @Param("completed") Integer completed, @Param("priority") Long priority, @Param("priority") Long category);
}
