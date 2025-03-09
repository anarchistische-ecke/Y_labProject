package org.example.service;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }


}
