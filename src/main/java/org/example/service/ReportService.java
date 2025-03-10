package org.example.service;

import org.example.model.User;

import java.time.LocalDate;
import java.util.Map;

public class ReportService {

    private TransactionService transactionService;

    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void generateFinancialReport(User user, LocalDate start, LocalDate end) {
        double balance = transactionService.calculateBalance(user);
        double totalIncome = transactionService.calculateTotalIncome(user, start, end);
        double totalExpense = transactionService.calculateTotalExpense(user, start, end);
        Map<String, Double> expensesByCategory = transactionService.calculateTransactionsByCategory(user);

        System.out.println("----- Финансовый отчет -----");
        System.out.println("Текущий баланс: " + balance);
        System.out.println("Общий доход с " + start + " до " + end + ": " + totalIncome);
        System.out.println("Общие расходы с " + start + " до " + end + ": " + totalExpense);
        System.out.println("Анализ расходов по категориям:");
        expensesByCategory.forEach((category, amount) ->
                System.out.println("  " + category + ": " + amount)
        );
        System.out.println("-----------------------------");
    }
}

