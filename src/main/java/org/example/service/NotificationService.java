package org.example.service;

import org.example.model.Budget;
import org.example.model.User;

public class NotificationService {

    public void checkBudget(User user, TransactionService transactionService) {
        Budget budget = user.getBudget();
        if(budget != null) {
            double balance = transactionService.calculateBalance(user);
            if(balance > budget.getMonthlyLimit()) {
                System.out.println("Внимание! Вы превысили установленный месячный бюджет.");
            }
        }
    }

}
