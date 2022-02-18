package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.entity.Roles;

import java.util.Optional;

public interface RolesRepository  extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(String roleName);
}
