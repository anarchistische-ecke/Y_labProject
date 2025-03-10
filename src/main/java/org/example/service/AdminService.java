package org.example.service;

import org.example.model.Transaction;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private AuthService authService;
    private UserService userService;

    public AdminService(AuthService authService) {
        this.authService = authService;
    }

    public void listAllUsers() {
        authService.getAllUsers().values().forEach(user -> {
            System.out.println("ID: " + user.getId() + ", Имя: " + user.getName() + ", Email: " + user.getEmail());
        });
    }

    public void deleteUser(String email) {
        authService.deleteUser(email);
        System.out.println("Пользователь с email " + email + " удалён.");
    }

    public void viewUserTransactions(String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty()) {
            System.out.println("User not found.");
            return;
        }

        List<Transaction> transactions = user.get().getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this user.");
            return;
        }

        System.out.println("Transactions for " + user.get().getName() + ":");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public void blockUser(String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            user.get().setBlocked(true);
            System.out.println(user.get().getName() + " has been blocked.");
        } else {
            System.out.println("User not found.");
        }


    }
}
