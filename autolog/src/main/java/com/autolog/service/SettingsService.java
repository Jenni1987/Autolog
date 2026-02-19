package com.autolog.service;

import com.autolog.model.User;
import com.autolog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsService {

    private final UserRepository UserAutologRepository;
    private final PasswordEncoder passwordEncoder;

    public SettingsService(UserRepository UserAutologRepository, PasswordEncoder passwordEncoder) {
        this.UserAutologRepository = UserAutologRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser(String email) {
        return UserAutologRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public User updateProfile(User currentUser, String newUsername, String newEmail) {
        Optional<User> userByUsername = UserAutologRepository.findByUsername(newUsername);
        if (userByUsername.isPresent() && !userByUsername.get().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        Optional<User> userByEmail = UserAutologRepository.findByEmail(newEmail);
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);

        return UserAutologRepository.save(currentUser);
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual no es correcta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        UserAutologRepository.save(user);
    }
}
