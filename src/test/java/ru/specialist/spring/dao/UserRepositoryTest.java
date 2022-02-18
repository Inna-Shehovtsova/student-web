package ru.specialist.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.student.config.DataConfig;
import ru.student.repository.UserRepository;
import ru.student.entity.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataConfig.class)
@Sql(scripts = "classpath:schema.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserRepositoryTest {
    private final UserRepository usersRepository;

    @Autowired
    public UserRepositoryTest(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Test
    void create(){
        User users = new User();
        users.setUsername("User3");
        users.setFirstName("Carrie");
        users.setLastName("Cat");
        users.setIsActive(false);
        users.setCreatedAt(LocalDateTime.now());

        usersRepository.save(users);

        assertEquals("User3", usersRepository.findById(3L).get().getUsername());
    }


    @Test
    void update(){
        User post = usersRepository.findById(1L).get();
        post.setUsername("UpdatedUsername");

        usersRepository.save(post);
        assertEquals("UpdatedUsername", usersRepository.findById(1L).get().getUsername());
    }

    @Test
    void delete() {
        usersRepository.deleteById(1L);
        assertEquals(1, usersRepository.count());
    }


    @Test
    void findByUsernme(){
        User post = usersRepository.findByUsername("test_user1").get();
        assertEquals("Angy", post.getFirstName());
    }

    @Test
    void findByCreatedAtBetween(){
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        assertEquals(2,
                usersRepository.findByCreatedAtBetween(from, to).size());
    }

    @Test
    void findAdminUsers(){
        List<Long> ids = usersRepository
                .findAdminUsers()
                .stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        Assertions.assertIterableEquals(List.of(1L), ids);
    }

}
