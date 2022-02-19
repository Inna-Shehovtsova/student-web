package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
