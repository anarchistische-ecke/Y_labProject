package org.example;

import org.example.model.Budget;
import org.example.model.Goal;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.service.*;

import java.time.LocalDate;
import java.util.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static AuthService authService = new AuthService();
    private static AdminService adminService = new AdminService(authService);
    private static TransactionService transactionService = new TransactionService();
    private static NotificationService notificationService = new NotificationService();
    private static ReportService reportService = new ReportService(transactionService);
    private static int transactionIdSequence = 1;
    private static int goalIdSequence = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;
        while (true) {
            if (currentUser == null) {
                currentUser = handleAuthentication(scanner);
            } else {
                currentUser = handleMainMenu(scanner, currentUser);
            }
        }
    }

    private static User handleAuthentication(Scanner scanner) {
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("0. Выход");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Введите имя:");
                String name = scanner.nextLine();
                System.out.println("Введите email:");
                String email = scanner.nextLine();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine();
                try {
                    User user = authService.register(name, email, password);
                    System.out.println("Регистрация прошла успешно.");
                    return user;
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                    return null;
                }
            case "2":
                System.out.println("Введите email:");
                email = scanner.nextLine();
                System.out.println("Введите пароль:");
                password = scanner.nextLine();
                Optional<User> userOpt = authService.login(email, password);
                if (userOpt.isPresent()) {
                    System.out.println("Вход выполнен успешно. Добро пожаловать, " + userOpt.get().getName());
                    return userOpt.get();
                } else {
                    System.out.println("Неверный email или пароль.");
                    return null;
                }
            case "0":
                System.exit(0);
            default:
                System.out.println("Неверный выбор.");
                return null;
        }
    }

    private static User handleMainMenu(Scanner scanner, User currentUser) {
        System.out.println("\n--- Главное меню ---");
        System.out.println("1. Управление пользователями");
        System.out.println("2. Управление финансами (CRUD-операции)");
        System.out.println("3. Управление бюджетом");
        System.out.println("4. Управление целями");
        System.out.println("5. Статистика и аналитика");
        System.out.println("6. Выход из аккаунта");
        System.out.println("0. SUPERUSER");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleUserManagement(scanner, currentUser);
                break;
            case "2":
                handleTransactionManagement(scanner, currentUser);
                break;
            case "3":
                handleBudgetManagement(scanner, currentUser);
                break;
            case "4":
                handleGoalManagement(scanner, currentUser);
                break;
            case "5":
                handleAnalyticsManagement(scanner, currentUser);
                break;
            case "6":
                currentUser = null;
                System.out.println("Вы вышли из аккаунта.");
                break;
            case "0":
                handleSuperUserMenu(scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
        return currentUser;
    }

    private static void handleUserManagement(Scanner scanner, User currentUser) {
        System.out.println("1. Редактировать имя");
        System.out.println("2. Редактировать email");
        System.out.println("3. Изменить пароль");
        System.out.println("4. Удалить аккаунт");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Введите новое имя:");
                String newName = scanner.nextLine();
                currentUser.setName(newName);
                break;
            case "2":
                System.out.println("Введите новый email:");
                String newEmail = scanner.nextLine();
                currentUser.setEmail(newEmail);
                break;
            case "3":
                System.out.println("Введите новый пароль:");
                String newPassword = scanner.nextLine();
                currentUser.setPassword(newPassword);
                break;
            case "4":
                System.out.println("Для удаления аккаунта введите ваш email:");
                String email = scanner.nextLine();
                authService.deleteUser(email);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void handleTransactionManagement(Scanner scanner, User currentUser) {
        System.out.println("1. Новая транзакция");
        System.out.println("2. Редактировать транзакцию");
        System.out.println("3. Удаление транзакции");
        System.out.println("4. Просмотр транзакций");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Введите сумму:");
                double amount = Double.parseDouble(scanner.nextLine());
                System.out.println("Введите категорию:");
                String category = scanner.nextLine();
                System.out.println("Введите дату (YYYY-MM-DD):");
                LocalDate date = LocalDate.parse(scanner.nextLine());
                System.out.println("Введите описание:");
                String description = scanner.nextLine();
                System.out.println("Выберите тип: 1 - Доход, 2 - Расход");
                String typeChoice = scanner.nextLine();
                Transaction.Type type = typeChoice.equals("1") ? Transaction.Type.INCOME : Transaction.Type.EXPENSE;
                transactionService.createTransaction(currentUser, amount, category, date, description, type, transactionIdSequence++);
                System.out.println("Транзакция добавлена.");
                notificationService.checkBudget(currentUser, transactionService);
                break;
            case "2":
                System.out.println("Введите ID транзакции для редактирования:");
                int transactionId = Integer.parseInt(scanner.nextLine());
                Optional<Transaction> transactionOpt = currentUser.getTransactions().stream()
                        .filter(t -> t.getId() == transactionId).findFirst();
                if (transactionOpt.isPresent()) {
                    Transaction transaction = transactionOpt.get();
                    System.out.println("Введите новую сумму:");
                    double newAmount = Double.parseDouble(scanner.nextLine());
                    System.out.println("Введите новую категорию:");
                    String newCategory = scanner.nextLine();
                    System.out.println("Введите новое описание:");
                    String newDescription = scanner.nextLine();
                    transactionService.updateTransaction(transaction, newAmount, newCategory, newDescription);
                    System.out.println("Транзакция обновлена.");
                } else {
                    System.out.println("Транзакция с указанным ID не найдена.");
                }
                break;
            case "3":
                System.out.println("Введите Id транзакции:");
                int id = Integer.parseInt(scanner.nextLine());
                transactionService.deleteTransaction(currentUser, id);
                System.out.println("Транзакция удалена");
                break;
            case "4":
                System.out.println("Ваши транзакции:");
                currentUser.getTransactions().forEach(t -> {
                    System.out.println("ID: " + t.getId() + ", " + t.getType() + ", " + t.getAmount() + ", " +
                            t.getCategory() + ", " + t.getDate() + ", " + t.getDescription());
                });
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void handleBudgetManagement(Scanner scanner, User currentUser) {
        System.out.println("1. Установить месячный бюджет");
        System.out.println("2. Обновить месячный бюджет");
        System.out.println("0. Назад");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Введите ваш месячный бюджет:");
                double limit = Double.parseDouble(scanner.nextLine());
                Budget budget = new Budget(limit);
                currentUser.setBudget(budget);
                break;
            case "2":
                System.out.println("Введите новый месячный бюджет:");
                double newBudgetLimit = Double.parseDouble(scanner.nextLine());
                if (currentUser.getBudget() == null) {
                    currentUser.setBudget(new Budget(newBudgetLimit));
                } else {
                    currentUser.getBudget().setMonthlyLimit(newBudgetLimit);
                }
                System.out.println("Бюджет успешно обновлен.");
                break;
            case "0":
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void handleGoalManagement(Scanner scanner, User currentUser) {
        System.out.println("1. Просмотр всех целей");
        System.out.println("2. Установить новую цель");
        System.out.println("3. Изменить существующую цель");
        System.out.println("4. Удалить цель");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                for (Goal goal : currentUser.getGoals()) {
                    System.out.println("ID: " + goal.getId() + ", Целевая сумма: " + goal.getTargetAmount() +
                            ", Текущий прогресс: " + goal.getCurrentProgress());
                }
                break;
            case "2":
                System.out.println("Введите название цели:");
                String goalName = scanner.nextLine();
                System.out.println("Введите цель:");
                double targetAmount = Double.parseDouble(scanner.nextLine());
                Goal goal = new Goal(goalIdSequence++, targetAmount, goalName);
                currentUser.addGoal(goal);
                System.out.println("Цель добавлена!");
                break;
            case "3":
                System.out.println("Введите Id цели, которую желаете изменить:");
                int goalId = Integer.parseInt(scanner.nextLine());
                Optional<Goal> goalOpt = currentUser.getGoals().stream().filter(g -> g.getId() == goalId).findFirst();
                if (goalOpt.isPresent()) {
                    Goal foundGoal = goalOpt.get();
                    System.out.println("Найденная цель: ");
                    System.out.println("ID: " + foundGoal.getId());
                    System.out.println("Целевая сумма: " + foundGoal.getTargetAmount());
                    System.out.println("Текущий прогресс: " + foundGoal.getCurrentProgress());
                } else {
                    System.out.println("Цель с ID " + goalId + " не найдена.");
                }
                break;
            case "4":
                System.out.println("Введите Id цели, которую хотите удалить:");
                int goalDeleteId = Integer.parseInt(scanner.nextLine());
                boolean removed = currentUser.getGoals().removeIf(g -> g.getId() == goalDeleteId);
                if (removed) {
                    System.out.println("Цель с ID " + goalDeleteId + " успешно удалена.");
                } else {
                    System.out.println("Цель с ID " + goalDeleteId + " не найдена.");
                }
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void handleAnalyticsManagement(Scanner scanner, User currentUser) {
        System.out.println("1. Подсчет текущего баланса");
        System.out.println("2. Расчет суммарного дохода и расхода за период");
        System.out.println("3. Анализ расходов по категориям");
        System.out.println("4. Отчет по финансовому состоянию");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                double balance = transactionService.calculateBalance(currentUser);
                System.out.println("Ваш текущий баланс: " + balance);
                break;
            case "2":
                System.out.println("Введите начало периода:");
                LocalDate start = LocalDate.parse(scanner.nextLine());
                System.out.println("Введите конец периода:");
                LocalDate end = LocalDate.parse(scanner.nextLine());
                double totalEarned = transactionService.calculateTotalIncome(currentUser, start, end);
                double totalSpent = transactionService.calculateTotalExpense(currentUser, start, end);
                System.out.println("Доходы за указанный период: " + totalEarned + ", расходы: " + totalSpent);
                break;
            case "3":
                Map<String, List<Transaction>> groupedTransactions = transactionService.getTransactionsByCategory(currentUser);
                groupedTransactions.forEach((category, transactions) -> {
                    System.out.println("Category: " + category);
                    transactions.forEach(t -> System.out.println("  Transaction ID: " + t.getId() + ", Amount: " + t.getAmount() +
                            ", Date: " + t.getDate() + ", Description: " + t.getDescription()));
                });
                break;
            case "4":
                System.out.println("Введите начальную дату (YYYY-MM-DD):");
                LocalDate startDate = LocalDate.parse(scanner.nextLine());
                System.out.println("Введите конечную дату (YYYY-MM-DD):");
                LocalDate endDate = LocalDate.parse(scanner.nextLine());
                reportService.generateFinancialReport(currentUser, startDate, endDate);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void handleSuperUserMenu(Scanner scanner) {
        System.out.println("Вы суперюзер?");
        System.out.println("1. Да");
        System.out.println("2. Нет");
        String answer = scanner.nextLine();
        while (answer.equals("1")) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. List all users");
            System.out.println("2. View user transactions");
            System.out.println("3. Block a user");
            System.out.println("4. Delete a user");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    authService.getAllUsers();
                    break;

                case "2":
                    System.out.print("Enter user email: ");
                    String email = scanner.nextLine();
                    adminService.viewUserTransactions(email);
                    break;

                case "3":
                    System.out.print("Enter user email to block: ");
                    email = scanner.nextLine();
                    adminService.blockUser(email);
                    break;

                case "4":
                    System.out.print("Enter user email to delete: ");
                    email = scanner.nextLine();
                    adminService.deleteUser(email);
                    break;

                case "5":
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
