package app;

import exception.InsufficientBalanceException;
import exception.InvalidPinException;
import model.Transaction;
import model.User;
import service.AIService;
import service.WalletService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Entry point for the Digital Wallet Simulator CLI application.
 */
public class Main {
    private static final WalletService walletService = new WalletService();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   WELCOME TO DIGITAL WALLET SIMULATOR   ");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            try {
                if (currentUser == null) {
                    running = showBeforeLoginMenu();
                } else {
                    running = showAfterLoginMenu();
                }
            } catch (java.util.NoSuchElementException e) {
                System.out.println("\nInput stream closed. Saving data and exiting...");
                walletService.exitAndSave();
                break;
            }
        }
        scanner.close();
    }

    /**
     * Menu displayed when no user is logged in.
     *
     * @return true to continue running, false to exit
     */
    private static boolean showBeforeLoginMenu() {
        System.out.println("\n===== DIGITAL WALLET =====");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleRegister();
                break;
            case "2":
                handleLogin();
                break;
            case "3":
                System.out.println("\nSaving data and exiting... Goodbye!");
                walletService.exitAndSave();
                return false;
            default:
                System.out.println("[ERROR] Invalid choice. Please enter 1, 2, or 3.");
        }
        return true;
    }

    /**
     * Menu displayed when a user is successfully logged in.
     *
     * @return true to continue running, false to exit
     */
    private static boolean showAfterLoginMenu() {
        System.out.println("\n===== WALLET MENU =====");
        System.out.println("User: " + currentUser.getName() + " (" + currentUser.getPhoneNumber() + ")");
        System.out.println("---------------------------------");
        System.out.println("1. Add Money");
        System.out.println("2. Send Money");
        System.out.println("3. Check Balance");
        System.out.println("4. Transaction History");
        System.out.println("5. AI Financial Insights");
        System.out.println("6. Change PIN");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleAddMoney();
                break;
            case "2":
                handleSendMoney();
                break;
            case "3":
                handleCheckBalance();
                break;
            case "4":
                handleTransactionHistory();
                break;
            case "5":
                handleAiFinancialInsights();
                break;
            case "6":
                handleChangePin();
                break;
            case "7":
                System.out.println("\nLogging out... Goodbye, " + currentUser.getName() + "!");
                currentUser = null;
                break;
            default:
                System.out.println("[ERROR] Invalid choice. Please select between 1 and 7.");
        }
        return true;
    }

    /**
     * Logic for registering a new user.
     */
    private static void handleRegister() {
        System.out.println("\n--- Register New Wallet ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter 10-digit Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter 4-digit PIN: ");
        String pin = scanner.nextLine().trim();

        try {
            walletService.register(name, phone, pin);
            System.out.println("[SUCCESS] Registration successful! You can now login.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Logic for logging in an existing user.
     */
    private static void handleLogin() {
        System.out.println("\n--- Wallet Login ---");
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine().trim();

        try {
            currentUser = walletService.login(phone, pin);
            System.out.println("[SUCCESS] Login successful! Welcome back, " + currentUser.getName() + ".");
        } catch (InvalidPinException e) {
            System.out.println("[ERROR] PIN Verification Failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Login Failed: " + e.getMessage());
        }
    }

    /**
     * Logic for adding money to the logged-in user's wallet.
     */
    private static void handleAddMoney() {
        System.out.println("\n--- Add Money ---");
        System.out.print("Enter Amount to Add: ");
        String amountStr = scanner.nextLine().trim();

        try {
            double amount = Double.parseDouble(amountStr);
            walletService.addMoney(currentUser, amount);
            System.out.printf(Locale.US, "[SUCCESS] Added Rs. %.2f successfully! New Balance: Rs. %.2f\n", amount, currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid amount format. Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Logic for transferring money to another user.
     */
    private static void handleSendMoney() {
        System.out.println("\n--- Send Money ---");
        System.out.print("Enter Receiver's 10-digit Phone Number: ");
        String receiverPhone = scanner.nextLine().trim();
        System.out.print("Enter Amount to Send: ");
        String amountStr = scanner.nextLine().trim();

        try {
            double amount = Double.parseDouble(amountStr);
            walletService.sendMoney(currentUser, receiverPhone, amount);
            System.out.printf(Locale.US, "[SUCCESS] Transferred Rs. %.2f to receiver (%s) successfully!\n", amount, receiverPhone);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid amount format. Please enter a valid number.");
        } catch (InsufficientBalanceException e) {
            System.out.println("[ERROR] Insufficient Balance: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Transfer Failed: " + e.getMessage());
        }
    }

    /**
     * Displays the current balance.
     */
    private static void handleCheckBalance() {
        System.out.println("\n--- Wallet Balance ---");
        System.out.printf(Locale.US, "Current Balance: Rs. %.2f\n", currentUser.getBalance());
    }

    /**
     * Displays the transaction history of the current user.
     */
    private static void handleTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        List<Transaction> transactions = currentUser.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction tx : transactions) {
                System.out.println(tx);
            }
        }
    }

    /**
     * Logic for changing the user's PIN.
     */
    private static void handleChangePin() {
        System.out.println("\n--- Change PIN ---");
        System.out.print("Enter Current 4-digit PIN: ");
        String oldPin = scanner.nextLine().trim();
        System.out.print("Enter New 4-digit PIN: ");
        String newPin = scanner.nextLine().trim();
        System.out.print("Confirm New 4-digit PIN: ");
        String confirmPin = scanner.nextLine().trim();

        if (!newPin.equals(confirmPin)) {
            System.out.println("[ERROR] PIN confirmation does not match.");
            return;
        }

        try {
            walletService.changePin(currentUser, oldPin, newPin);
            System.out.println("[SUCCESS] PIN changed successfully!");
        } catch (InvalidPinException e) {
            System.out.println("[ERROR] Change PIN Failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Handles displaying the AI Financial Insights for the logged-in user.
     */
    private static void handleAiFinancialInsights() {
        List<Transaction> transactions = currentUser.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions available for analysis.");
            return;
        }

        System.out.print("Connecting to AI service to analyze transaction history... ");
        try {
            String insights = AIService.getFinancialInsights(transactions);
            System.out.println("Done.\n");
            System.out.println("==================================");
            System.out.println("AI Financial Insights");
            System.out.println("==================================");
            System.out.println();
            System.out.println(insights);
        } catch (IllegalStateException e) {
            System.out.println("Failed.");
            System.out.println("[ERROR] " + e.getMessage());
        } catch (java.net.http.HttpTimeoutException e) {
            System.out.println("Failed.");
            System.out.println("[ERROR] API timeout: The request to Gemini API timed out. Please check your internet connection.");
        } catch (java.io.IOException e) {
            System.out.println("Failed.");
            System.out.println("[ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed.");
            System.out.println("[ERROR] " + (e.getMessage() != null ? e.getMessage() : "Failed to fetch or parse financial insights."));
        }
    }
}
