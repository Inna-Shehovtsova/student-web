package ru.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.student.entity.Roles;
import ru.student.entity.User;
import ru.student.repository.RolesRepository;
import ru.student.repository.UserRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RolesRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getPostList().size();
        user.getRolesList().size();
        return user;
    }

    @Override
    public void create(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()){
            throw new EntityExistsException();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRolesList(List.of(roleRepository.findByRoleName(Roles.USER).orElseThrow()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);

        userRepository.save(user);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getRolesList().size();
        return  user;
    }
}
