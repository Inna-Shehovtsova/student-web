package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
