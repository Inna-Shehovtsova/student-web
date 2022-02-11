package ru.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.student.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByUsername(String title);

        List<User> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

        @Query(value = """
            select u.*
                from users u
            join users_roles ur
                on u.user_id = ur.user_id
            join roles r
               on ur.role_id = r.role_id
            where r.role_name = 'admin'
            """, nativeQuery = true)
        List<User> findAdminUsers();


}

