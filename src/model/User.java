package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered user of the digital wallet.
 */
public class User {
    private String name;
    private final String phoneNumber; // Unique identifier for the user
    private String pin; // 4-digit PIN
    private double balance;
    private final List<Transaction> transactions;

    /**
     * Constructor used for registering a new user.
     * Starts with a default balance of 0.0.
     *
     * @param name        the name of the user
     * @param phoneNumber the 10-digit phone number
     * @param pin         the 4-digit PIN
     */
    public User(String name, String phoneNumber, String pin) {
        this(name, phoneNumber, pin, 0.0);
    }

    /**
     * Constructor used when reloading data from storage.
     *
     * @param name        the name of the user
     * @param phoneNumber the 10-digit phone number
     * @param pin         the 4-digit PIN
     * @param balance     the loaded balance
     */
    public User(String name, String phoneNumber, String pin, double balance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
        this.balance = Math.round(balance * 100.0) / 100.0;
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = Math.round(balance * 100.0) / 100.0;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the user's history.
     *
     * @param transaction the transaction to add
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
