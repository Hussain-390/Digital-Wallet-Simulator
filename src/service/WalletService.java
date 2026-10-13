package service;

import exception.InsufficientBalanceException;
import exception.InvalidPinException;
import model.Transaction;
import model.User;
import util.Validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service class handling all core digital wallet operations and persistence.
 */
public class WalletService {
    private final Map<String, User> users;
    private final String dataDir;
    private final String usersFile;
    private final String transactionsFile;

    public WalletService() {
        this.users = new HashMap<>();
        this.dataDir = resolveDataDir();
        this.usersFile = this.dataDir + File.separator + "users.txt";
        this.transactionsFile = this.dataDir + File.separator + "transactions.txt";
        loadData();
        // Register shutdown hook to guarantee data is saved even on abrupt exit
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData));
    }

    /**
     * Resolves the directory where user and transaction data files reside.
     * Checks multiple common paths (e.g. app/data, data, src/app/data, src/data)
     * so that the application works seamlessly regardless of the working directory.
     *
     * @return the resolved data directory path
     */
    private static String resolveDataDir() {
        String[] candidateDirs = {
                "app/data",
                "data",
                "src/app/data",
                "src/data"
        };
        for (String dir : candidateDirs) {
            File f = new File(dir, "users.txt");
            if (f.exists()) {
                return dir;
            }
        }
        if (new File("app").isDirectory()) {
            return "app/data";
        }
        return "data";
    }

    /**
     * Registers a new user in the system.
     *
     * @param name        the user's name
     * @param phoneNumber the 10-digit phone number
     * @param pin         the 4-digit PIN
     * @return the newly registered User
     * @throws IllegalArgumentException if validation checks fail or user already exists
     */
    public User register(String name, String phoneNumber, String pin) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (!Validator.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");
        }
        if (!Validator.isValidPin(pin)) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        if (users.containsKey(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already registered.");
        }

        User newUser = new User(name.trim(), phoneNumber.trim(), pin.trim());
        users.put(phoneNumber.trim(), newUser);
        saveData();
        return newUser;
    }

    /**
     * Logs in a user by verifying phone number and PIN.
     *
     * @param phoneNumber the 10-digit phone number
     * @param pin         the 4-digit PIN
     * @return the logged in User
     * @throws InvalidPinException if the PIN is incorrect
     * @throws IllegalArgumentException if the user doesn't exist
     */
    public User login(String phoneNumber, String pin) throws InvalidPinException {
        if (!users.containsKey(phoneNumber)) {
            throw new IllegalArgumentException("Phone number is not registered.");
        }
        User user = users.get(phoneNumber);
        if (!user.getPin().equals(pin)) {
            throw new InvalidPinException("Incorrect PIN. Access Denied.");
        }
        return user;
    }

    /**
     * Adds money to the user's wallet.
     *
     * @param user   the user to deposit to
     * @param amount the deposit amount
     * @throws IllegalArgumentException if amount is invalid
     */
    public void addMoney(User user, double amount) {
        addMoney(user, amount, "Deposited money via Self");
    }

    /**
     * Adds money to the user's wallet with a custom description.
     * Overloads addMoney(User, double) to demonstrate method overloading.
     *
     * @param user        the user to deposit to
     * @param amount      the deposit amount
     * @param description a custom description for the transaction
     * @throws IllegalArgumentException if amount is invalid
     */
    public void addMoney(User user, double amount, String description) {
        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        user.setBalance(user.getBalance() + amount);
        user.addTransaction(new Transaction(
                LocalDateTime.now(),
                amount,
                "DEPOSIT",
                description
        ));
        saveData();
    }

    /**
     * Sends money from sender to receiver.
     *
     * @param sender        the sender
     * @param receiverPhone the phone number of the receiver
     * @param amount        the transfer amount
     * @throws InsufficientBalanceException if sender has less balance than the amount
     * @throws IllegalArgumentException if receiver doesn't exist or sender transfers to self
     */
    public void sendMoney(User sender, String receiverPhone, double amount) throws InsufficientBalanceException {
        if (sender.getPhoneNumber().equals(receiverPhone)) {
            throw new IllegalArgumentException("You cannot send money to yourself.");
        }
        if (!Validator.isValidAmount(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (!users.containsKey(receiverPhone)) {
            throw new IllegalArgumentException("Receiver phone number is not registered.");
        }
        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    String.format(Locale.US, "Insufficient balance. Current balance: Rs. %.2f. Requested: Rs. %.2f",
                            sender.getBalance(), amount)
            );
        }

        User receiver = users.get(receiverPhone);

        // Deduct sender balance and add transaction
        sender.setBalance(sender.getBalance() - amount);
        sender.addTransaction(new Transaction(
                LocalDateTime.now(),
                amount,
                "SENT",
                "Sent to " + receiver.getName() + " (" + receiverPhone + ")"
        ));

        // Credit receiver balance and add transaction
        receiver.setBalance(receiver.getBalance() + amount);
        receiver.addTransaction(new Transaction(
                LocalDateTime.now(),
                amount,
                "RECEIVED",
                "Received from " + sender.getName() + " (" + sender.getPhoneNumber() + ")"
        ));
        saveData();
    }

    /**
     * Changes the PIN of a user.
     *
     * @param user   the user whose PIN needs to be changed
     * @param oldPin the old PIN for verification
     * @param newPin the new PIN to set
     * @throws InvalidPinException if the old PIN does not match
     * @throws IllegalArgumentException if the new PIN format is invalid
     */
    public void changePin(User user, String oldPin, String newPin) throws InvalidPinException {
        if (!user.getPin().equals(oldPin)) {
            throw new InvalidPinException("Current PIN is incorrect.");
        }
        if (!Validator.isValidPin(newPin)) {
            throw new IllegalArgumentException("New PIN must be exactly 4 digits.");
        }
        user.setPin(newPin);
        saveData();
    }

    /**
     * Exits the application and saves all data.
     */
    public void exitAndSave() {
        saveData();
    }

    /**
     * Saves user data and transaction logs to separate text files.
     * Synchronizes across both active data directory and common mirrors (e.g. data/ and app/data/).
     */
    public synchronized void saveData() {
        List<File> targetDirs = new ArrayList<>();
        File primaryDir = new File(dataDir);
        targetDirs.add(primaryDir);

        File appDataDir = new File("app/data");
        if (appDataDir.exists() && !appDataDir.getAbsoluteFile().equals(primaryDir.getAbsoluteFile())) {
            targetDirs.add(appDataDir);
        }
        File standardDataDir = new File("data");
        if (standardDataDir.exists() && !standardDataDir.getAbsoluteFile().equals(primaryDir.getAbsoluteFile())) {
            targetDirs.add(standardDataDir);
        }

        for (File dir : targetDirs) {
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File uFile = new File(dir, "users.txt");
            try (BufferedWriter userWriter = new BufferedWriter(new FileWriter(uFile))) {
                for (User user : users.values()) {
                    userWriter.write(String.format(Locale.US, "%s|%s|%s|%.2f%n",
                            user.getPhoneNumber(),
                            user.getName(),
                            user.getPin(),
                            user.getBalance()));
                }
            } catch (IOException e) {
                System.err.println("Error saving users to " + uFile.getPath() + ": " + e.getMessage());
            }

            File tFile = new File(dir, "transactions.txt");
            try (BufferedWriter txWriter = new BufferedWriter(new FileWriter(tFile))) {
                for (User user : users.values()) {
                    for (Transaction tx : user.getTransactions()) {
                        txWriter.write(String.format(Locale.US, "%s|%s|%.2f|%s|%s%n",
                                user.getPhoneNumber(),
                                tx.getDateTime().toString(),
                                tx.getAmount(),
                                tx.getType(),
                                tx.getDescription()));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error saving transactions to " + tFile.getPath() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Loads user and transaction logs from text files on startup.
     */
    private void loadData() {
        File userFile = new File(usersFile);
        if (!userFile.exists()) {
            return; // No saved data to load
        }

        // Load users
        try (BufferedReader userReader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = userReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    String phone = parts[0].trim();
                    String name = parts[1].trim();
                    String pin = parts[2].trim();
                    double balance = Double.parseDouble(parts[3].trim());
                    User user = new User(name, phone, pin, balance);
                    users.put(phone, user);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        File txFile = new File(transactionsFile);
        if (!txFile.exists()) {
            return;
        }

        // Load transactions
        try (BufferedReader txReader = new BufferedReader(new FileReader(txFile))) {
            String line;
            while ((line = txReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", 5);
                if (parts.length == 5) {
                    String phone = parts[0].trim();
                    LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim());
                    double amount = Double.parseDouble(parts[2].trim());
                    String type = parts[3].trim();
                    String description = parts[4].trim();

                    User user = users.get(phone);
                    if (user != null) {
                        Transaction tx = new Transaction(dateTime, amount, type, description);
                        user.addTransaction(tx);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }
}
