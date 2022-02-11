package ru.student.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.entity.User;
import ru.student.repository.UserRepository;
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getPostList().size();
        return user;
    }

}
