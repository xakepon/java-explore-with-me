package ru.practicum.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.persistence.models.Location;

@Repository
public interface LocationRep extends JpaRepository<Location, Long> {
}
