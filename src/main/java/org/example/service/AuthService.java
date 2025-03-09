package org.example.service;

import org.example.model.User;

import java.util.*;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    private int userIdSequence = 1;

    public User register(String name, String email, String password) {
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User(userIdSequence++, name, email, password);
        users.put(email, user);
        return user;
    }

    public Optional<User> login(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void updateProfile(User user, String newName, String newEmail, String newPassword) {
        user.setName(newName);
        user.setEmail(newEmail);
        user.setPassword(newPassword);
    }

    public void deleteUser(String email) {
        users.remove(email);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }

}

