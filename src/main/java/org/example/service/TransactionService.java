package org.example.service;

import org.example.model.Goal;
import org.example.model.Transaction;
import org.example.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionService {

    public Transaction createTransaction(User user, double amount, String category, LocalDate date, String description, Transaction.Type type, int transactionId) {
        Transaction transaction = new Transaction(transactionId, amount, category, date, description, type);
        user.addTransaction(transaction);

        if (type == Transaction.Type.INCOME && !user.getGoals().isEmpty()) { //код для обновления целей если транзакцмя доход
            double remainingAmount = amount;
            for (Goal goal : user.getGoals()) {
                double needed = goal.getTargetAmount() - goal.getCurrentProgress();
                if (needed > 0) {
                    if (remainingAmount <= needed) {
                        goal.addProgress(remainingAmount);
                        remainingAmount = 0;
                    } else {
                        goal.addProgress(needed);
                        remainingAmount -= needed;
                    }
                    System.out.println("Обновление цели ID " + goal.getId() + ": текущий прогресс " + goal.getCurrentProgress() + "/" + goal.getTargetAmount());
                }
                if (remainingAmount <= 0) {
                    break;
                }
            }
            if (remainingAmount > 0) {
                System.out.println("Излишек " + remainingAmount + " не был распределён по целям.");
            }
        }
        return transaction;
    }

    public void updateTransaction(Transaction transaction, double newAmount, String newCategory, String newDescription) {
        transaction.setAmount(newAmount);
        transaction.setCategory(newCategory);
        transaction.setDescription(newDescription);
    }

    public void deleteTransaction(User user, int transactionId) {
        user.getTransactions().removeIf(t -> t.getId() == transactionId);
    }

    public List<Transaction> getTransactionsByType(User user, Transaction.Type type) {
        return user.getTransactions().stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public Map<String, List<Transaction>> getTransactionsByCategory(User user) {
        return user.getTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));
    }

    public double calculateBalance(User user) {
        return user.getTransactions().stream().mapToDouble(t ->
                t.getType() == Transaction.Type.INCOME ? t.getAmount() : -t.getAmount()
        ).sum();
    }

    public Map<String, Double> calculateTransactionsByCategory(User user) {
        return user.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)));
    }

    public List<Transaction> getTransactionsByDate(User user, LocalDate start, LocalDate end) {
        return user.getTransactions().stream()
                .filter(t -> (t.getDate().isEqual(start) || t.getDate().isAfter(start)) &&
                        (t.getDate().isEqual(end) || t.getDate().isBefore(end)))
                .collect(Collectors.toList());
    }

    public double calculateTotalIncome(User user, LocalDate start, LocalDate end) {
        return user.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.INCOME)
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateTotalExpense(User user, LocalDate start, LocalDate end) {
        return user.getTransactions().stream()
                .filter(t -> t.getType() == Transaction.Type.EXPENSE)
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


}


