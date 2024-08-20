package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.Location;

@Repository
public interface LocationRep extends JpaRepository<Location, Long> {
}
