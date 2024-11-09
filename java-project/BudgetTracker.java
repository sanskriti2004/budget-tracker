import java.util.Scanner;

// Interface defining the methods for BudgetTracker
interface BudgetOperations {
    void addIncome(double amount) throws InvalidAmountException;
    void addExpense(double amount, String category) throws InvalidAmountException;
    void setBudgetGoal(double goal) throws InvalidAmountException;
    double getBalance();
    void viewTransactionHistory();
    void generateMonthlyReport();
}

// Custom Exception for handling invalid amounts
class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

// Abstract class to implement common methods for BudgetTracker
abstract class BudgetManager implements BudgetOperations {
    protected double balance;
    protected double budgetGoal;
    protected String[] transactionHistory;
    protected int transactionCount;

    public BudgetManager() {
        balance = 0.0;
        budgetGoal = 0.0;
        transactionHistory = new String[100]; // Limiting transaction history size to 100
        transactionCount = 0;
    }

    public double getBalance() {
        return balance;
    }

    // Adds income to the balance and records the transaction
    public void addIncome(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Income cannot be negative.");
        }
        balance += amount;
        recordTransaction("Income: Rs " + amount);
    }

    // Subtracts expense from balance and records it
    public void addExpense(double amount, String category) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Expense cannot be negative.");
        }
        balance -= amount;
        recordTransaction("Expense (" + category + "): Rs " + amount);
        checkBudgetWarning();
    }

    // Sets the budget goal
    public void setBudgetGoal(double goal) throws InvalidAmountException {
        if (goal < 0) {
            throw new InvalidAmountException("Budget goal cannot be negative.");
        }
        budgetGoal = goal;
        System.out.println("Monthly Budget Goal set to: Rs " + goal);
    }

    // Record a transaction in the transaction history
    private void recordTransaction(String transaction) {
        if (transactionCount < transactionHistory.length) {
            transactionHistory[transactionCount++] = transaction;
        } else {
            System.out.println("Transaction history limit reached.");
        }
    }

    // Check if expenses exceed the budget goal
    private void checkBudgetWarning() {
        double totalExpenses = getTotalExpenses();
        if (totalExpenses > budgetGoal) {
            System.out.println("Warning: You have exceeded your budget goal!");
        }
    }

    // Get the total expenses from transaction history
    private double getTotalExpenses() {
        double totalExpenses = 0.0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactionHistory[i].startsWith("Expense")) {
                String[] parts = transactionHistory[i].split(": Rs ");
                totalExpenses += Double.parseDouble(parts[1]);
            }
        }
        return totalExpenses;
    }

    // Print transaction history
    public void viewTransactionHistory() {
        if (transactionCount == 0) {
            System.out.println("No transactions recorded yet.");
        } else {
            System.out.println("Transaction History:");
            for (int i = 0; i < transactionCount; i++) {
                System.out.println(transactionHistory[i]);
            }
        }
    }

    // Generate monthly report
    public void generateMonthlyReport() {
        System.out.println("------ Monthly Report ------");
        System.out.println("Total Income: Rs " + (balance + getTotalExpenses()));
        System.out.println("Total Expenses: Rs " + getTotalExpenses());
        System.out.println("Current Balance: Rs " + balance);
        System.out.println("-----------------------------");
    }
}

// Concrete class implementing BudgetManager
public class BudgetTracker extends BudgetManager {
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

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter income amount: ");
                        double income = scanner.nextDouble();
                        tracker.addIncome(income);
                        System.out.println("Income added successfully.");
                        break;
                    case 2:
                        System.out.print("Enter expense amount: ");
                        double expense = scanner.nextDouble();
                        System.out.print("Enter expense category (e.g., Food, Transport, etc.): ");
                        scanner.nextLine();  // Consume the newline character
                        String category = scanner.nextLine();
                        tracker.addExpense(expense, category);
                        System.out.println("Expense added successfully.");
                        break;
                    case 3:
                        System.out.println("Current Balance: Rs " + tracker.getBalance());
                        break;
                    case 4:
                        System.out.print("Set monthly budget goal: ");
                        double goal = scanner.nextDouble();
                        tracker.setBudgetGoal(goal);
                        break;
                    case 5:
                        tracker.viewTransactionHistory();
                        break;
                    case 6:
                        tracker.generateMonthlyReport();
                        break;
                    case 7:
                        System.out.println("Exiting... Thank you!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InvalidAmountException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
