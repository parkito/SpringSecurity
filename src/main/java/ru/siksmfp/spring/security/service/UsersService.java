package ru.siksmfp.spring.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siksmfp.spring.security.entity.Role;
import ru.siksmfp.spring.security.entity.UserEntity;
import ru.siksmfp.spring.security.repository.impl.UserRepository;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void deleteUser(String email) {
        userRepository.deleteUserByEmail(email);
    }

    public void createUser(String email, String firstName, String secondName, String password) {
        UserEntity newUser = new UserEntity(email, firstName, secondName, password, Role.USER);
        userRepository.save(newUser);
    }
}
