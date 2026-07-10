package com.mascotas.backend.modules.users.service;

import com.mascotas.backend.modules.users.entity.User;
import com.mascotas.backend.modules.users.entity.UserRole;
import com.mascotas.backend.modules.users.exception.EmailAlreadyExistsException;
import com.mascotas.backend.modules.users.exception.UserNotFoundException;
import com.mascotas.backend.modules.users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Crear usuario
    public User create(String email, String passwordHash, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordHash)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    //buscar usuario (para iniciar sesion)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}