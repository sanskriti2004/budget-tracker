import java.util.ArrayList;
import java.util.Scanner;

public class BudgetTracker {
    private double balance;
    private double budgetGoal;
    private ArrayList<String> transactionHistory;

    public BudgetTracker() {
        balance = 0.0;
        budgetGoal = 0.0;
        transactionHistory = new ArrayList<>();
    }

    // Adds income to the balance
    public void addIncome(double amount) {
        balance += amount;
        transactionHistory.add("Income: Rs " + amount);
    }

    // Subtracts expense from the balance and categorizes it
    public void addExpense(double amount, String category) {
        balance -= amount;
        transactionHistory.add("Expense (" + category + "): Rs " + amount);
        checkBudgetWarning();
    }

    // Sets a monthly budget goal
    public void setBudgetGoal(double goal) {
        budgetGoal = goal;
        System.out.println("Monthly Budget Goal set to: Rs " + goal);
    }

    // Check if expenses exceed the budget goal
    private void checkBudgetWarning() {
        if (-getExpenses() > budgetGoal) {
            System.out.println("Warning: You have exceeded your budget goal!");
        }
    }

    // Gets the current balance
    public double getBalance() {
        return balance;
    }

    // Gets the total expenses from transaction history
    public double getExpenses() {
        double totalExpenses = 0.0;
        for (String transaction : transactionHistory) {
            if (transaction.startsWith("Expense")) {
                String[] parts = transaction.split(": Rs ");
                totalExpenses += Double.parseDouble(parts[1]);
            }
        }
        return totalExpenses;
    }

    // Prints the transaction history
    public void viewTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            System.out.println("Transaction History:");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    // Generates a simple monthly report
    public void generateMonthlyReport() {
        System.out.println("------ Monthly Report ------");
        System.out.println("Total Income: Rs " + (balance + getExpenses()));
        System.out.println("Total Expenses: Rs " + -getExpenses());
        System.out.println("Current Balance: Rs " + balance);
        System.out.println("-----------------------------");
    }

    // Main application loop
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BudgetTracker tracker = new BudgetTracker();

        while (true) {
            System.out.println("\n1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Balance");
            System.out.println("4. Set Budget Goal");
            System.out.println("5. View Transaction History");
            System.out.println("6. Generate Monthly Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter income amount: ");
                    double income = scanner.nextDouble();
                    if (income < 0) {
                        System.out.println("Income cannot be negative.");
                    } else {
                        tracker.addIncome(income);
                        System.out.println("Income added successfully.");
                    }
                    break;
                case 2:
                    System.out.print("Enter expense amount: ");
                    double expense = scanner.nextDouble();
                    if (expense < 0) {
                        System.out.println("Expense cannot be negative.");
                    } else {
                        System.out.print("Enter expense category (e.g., Food, Transport, etc.): ");
                        scanner.nextLine();  // Consume the newline character
                        String category = scanner.nextLine();
                        tracker.addExpense(expense, category);
                        System.out.println("Expense added successfully.");
                    }
                    break;
                case 3:
                    System.out.println("Current Balance: Rs " + tracker.getBalance());
                    break;
                case 4:
                    System.out.print("Set monthly budget goal: ");
                    double goal = scanner.nextDouble();
                    if (goal < 0) {
                        System.out.println("Budget goal cannot be negative.");
                    } else {
                        tracker.setBudgetGoal(goal);
                    }
                    break;
                case 5:
                    tracker.viewTransactionHistory();
                    break;
                case 6:
                    tracker.generateMonthlyReport();
                    break;
                case 7:
                    System.out.println("Exiting... Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
