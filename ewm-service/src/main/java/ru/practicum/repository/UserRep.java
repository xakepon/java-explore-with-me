package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.User;

import java.util.List;

@Repository
public interface UserRep extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids, Pageable page);
}
