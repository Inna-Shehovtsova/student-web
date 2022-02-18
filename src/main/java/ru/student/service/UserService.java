package ru.student.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.entity.User;

import java.util.List;

@Service
@Transactional
public interface UserService {
    public User findByUsername(String username);
    void create(String username, String password);

}
