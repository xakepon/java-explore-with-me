package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.Category;

@Repository
public interface CategoryRep extends JpaRepository<Category, Long> {
}
