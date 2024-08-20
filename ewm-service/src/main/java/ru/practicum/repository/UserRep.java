package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.User;

import java.util.List;

public interface UserRep extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids, Pageable page);
}
