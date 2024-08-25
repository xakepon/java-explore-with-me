package ru.practicum.persistence.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.persistence.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRep extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId, PageRequest page);

    Optional<Comment> findByIdAndAuthorId(Long comId, Long userId);

}
