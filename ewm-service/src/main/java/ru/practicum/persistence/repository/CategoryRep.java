package ru.practicum.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.persistence.models.Category;

@Repository
public interface CategoryRep extends JpaRepository<Category, Long> {
}
