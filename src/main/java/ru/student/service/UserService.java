package ru.student.service;

import ru.student.entity.User;

public interface UserService {
    public User findByUsername(String username);
    void create(String username, String password);

}
