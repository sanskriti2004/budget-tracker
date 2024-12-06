import java.util.Scanner;

// Interface defining the basic budget operations.
// Interfaces provide a blueprint for methods that must be implemented.
interface BudgetOperations {
    void addIncome(double amount) throws InvalidAmountException;  // Adds income to the balance.
    void addExpense(double amount, String category) throws InvalidAmountException;  // Adds an expense.
    void setBudgetGoal(double goal) throws InvalidAmountException;  // Sets a budget goal.
    double getBalance();  // Returns the current balance.
    void viewTransactionHistory();  // Displays transaction history.
    void generateMonthlyReport();  // Generates a monthly financial report.
}

// Custom exception for invalid monetary amounts.
// Helps handle invalid input cases (e.g., negative amounts).
class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);  // Passes the custom message to the Exception superclass.
    }
}

// class implementing shared budget tracking functionality.
// Used to centralize and reuse common logic.
 class BudgetManager implements BudgetOperations {
    protected double balance;  // Tracks current balance.
    protected double budgetGoal;  // Stores the set budget goal.
    protected String[] transactionHistory;  // Stores transaction records.
    protected int transactionCount;  // Tracks number of recorded transactions.

    // Constructor initializes balance, goal, and transaction array.
    public BudgetManager() {
        balance = 0.0;
        budgetGoal = 0.0;
        transactionHistory = new String[100];  // Transaction history limit set to 100.
        transactionCount = 0;
    }

    // Returns the current balance.
    public double getBalance() {
        return balance;
    }

    // Adds income if valid, updates balance, and logs the transaction.
    public void addIncome(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Income cannot be negative.");
        }
        balance += amount;
        recordTransaction("Income: Rs " + amount);  // Record the income in the history.
    }

    // Deducts expense if valid, updates balance, and logs it with a category.
    public void addExpense(double amount, String category) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Expense cannot be negative.");
        }
        balance -= amount;
        recordTransaction("Expense (" + category + "): Rs " + amount);
        checkBudgetWarning();  // Check if expenses exceed the goal.
    }

    // Sets the budget goal, ensuring it's not negative.
    public void setBudgetGoal(double goal) throws InvalidAmountException {
        if (goal < 0) {
            throw new InvalidAmountException("Budget goal cannot be negative.");
        }
        budgetGoal = goal;
        System.out.println("Monthly Budget Goal set to: Rs " + goal);
    }

    // Records a transaction in the history if there's space.
    private void recordTransaction(String transaction) {
        if (transactionCount < transactionHistory.length) {
            transactionHistory[transactionCount++] = transaction;
        } else {
            System.out.println("Transaction history limit reached.");
        }
    }

    // Checks if total expenses exceed the set budget goal.
    private void checkBudgetWarning() {
        double totalExpenses = getTotalExpenses();
        if (totalExpenses > budgetGoal) {
            System.out.println("Warning: You have exceeded your budget goal!");
        }
    }

    // Calculates total expenses from transaction history.
    private double getTotalExpenses() {
        double totalExpenses = 0.0;
        for (int i = 0; i < transactionCount; i++) {
            if (transactionHistory[i].startsWith("Expense")) { //check if transaction is expense
                String[] parts = transactionHistory[i].split(": Rs ");
                totalExpenses += Double.parseDouble(parts[1]); //Extract and Parse the Expense Amount
            }
        }
        return totalExpenses;
    }

    // Displays all recorded transactions.
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

    // Generates and prints a monthly financial report.
    public void generateMonthlyReport() {
        System.out.println("------ Monthly Report ------");
        System.out.println("Total Income: Rs " + (balance + getTotalExpenses()));
        System.out.println("Total Expenses: Rs " + getTotalExpenses());
        System.out.println("Current Balance: Rs " + balance);
        System.out.println("-----------------------------");
    }
}

// Main class inheriting from BudgetManager and providing user interaction.
public class BudgetTracker extends BudgetManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Used for user input.
        BudgetTracker tracker = new BudgetTracker();  // Initializes budget tracker.

        while (true) {
            // Menu options for budget management.
            System.out.println("\n1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Balance");
            System.out.println("4. Set Budget Goal");
            System.out.println("5. View Transaction History");
            System.out.println("6. Generate Monthly Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();  // Takes user choice.

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
                        scanner.nextLine();  // Clears newline.
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
                        scanner.close();  // Close scanner to avoid resource leaks.
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InvalidAmountException e) {
                System.out.println(e.getMessage());  // Print error message for invalid inputs.
            }
        }
    }
}
