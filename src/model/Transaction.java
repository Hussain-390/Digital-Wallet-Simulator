package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a single financial transaction in the digital wallet.
 */
public class Transaction {
    private final LocalDateTime dateTime;
    private final double amount;
    private final String type; // DEPOSIT, SENT, RECEIVED
    private final String description;

    /**
     * Constructs a new Transaction.
     *
     * @param dateTime    the timestamp of the transaction
     * @param amount      the transaction amount
     * @param type        the type of transaction (e.g. "DEPOSIT", "SENT", "RECEIVED")
     * @param description details about the transaction
     */
    public Transaction(LocalDateTime dateTime, double amount, String type, String description) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(Locale.US, "[%s] Type: %-8s | Amount: Rs. %-8.2f | Info: %s",
                dateTime.format(formatter), type, amount, description);
    }
}
