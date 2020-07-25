package kz.app.tasklist.backendspringboot.repository;

import kz.app.tasklist.backendspringboot.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    // Получить все значения, сортировка по id
    List<Priority> findAllByOrderByIdAsc();
}
