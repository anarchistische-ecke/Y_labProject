package org.example.model;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private String name;
    private String email;
    private String password;
    private boolean isBlocked = false;
    private Role role;
    private List<Transaction> transactions = new ArrayList<>();
    private Budget budget;
    private List<Goal> goals = new ArrayList<>();

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public enum Role {
        ADMIN, USER
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }
}
