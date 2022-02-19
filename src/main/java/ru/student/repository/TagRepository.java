package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.entity.Post;
import ru.student.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

}
