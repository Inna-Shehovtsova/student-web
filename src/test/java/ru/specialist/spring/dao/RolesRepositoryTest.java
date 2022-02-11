package ru.specialist.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.student.config.DataConfig;
import ru.student.repository.RolesRepository;
import ru.student.repository.UserRepository;
import ru.student.entity.Roles;
import ru.student.entity.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataConfig.class)
@Sql(scripts = "classpath:schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class RolesRepositoryTest {
    private final RolesRepository rolesRepository;

    @Autowired
    public RolesRepositoryTest(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    @Test
    void create(){
        Roles roles = new Roles();
        roles.setRoleName("Moderator");
        rolesRepository.save(roles);

        assertEquals("Moderator", rolesRepository.findById(3L).get().getRoleName());
    }


    @Test
    void update(){
        Roles role = rolesRepository.findById(1L).get();
        role.setRoleName("Big Admin");

        rolesRepository.save(role);
        assertEquals("Big Admin", rolesRepository.findById(1L).get().getRoleName());
    }

    @Test
    void delete() {
        rolesRepository.deleteById(1L);
        assertEquals(1, rolesRepository.count());
    }
}
