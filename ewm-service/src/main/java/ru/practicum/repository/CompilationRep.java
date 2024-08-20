package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.Compilation;

import java.util.List;

@Repository
public interface CompilationRep extends JpaRepository<Compilation, Long> {
    List<Compilation> findByPinned(boolean pinned, Pageable page);
}
