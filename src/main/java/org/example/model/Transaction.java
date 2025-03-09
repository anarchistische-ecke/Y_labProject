package org.example.model;

import java.time.LocalDate;

public class Transaction {
    public enum Type {INCOME, EXPENSE}

    private final int id;
    private double amount;
    private String category;
    private LocalDate date;
    private String description;
    private Type type;

    public Transaction(int id, double amount, String category, LocalDate date, String description, Type type) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
