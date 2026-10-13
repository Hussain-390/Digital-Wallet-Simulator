package util;

/**
 * Utility class for validating input parameters in the digital wallet.
 */
public class Validator {

    private Validator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates if the phone number is exactly 10 digits.
     *
     * @param phoneNumber the phone number string to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10}$");
    }

    /**
     * Validates if the PIN is exactly 4 digits.
     *
     * @param pin the PIN string to validate
     * @return true if the PIN is valid, false otherwise
     */
    public static boolean isValidPin(String pin) {
        return pin != null && pin.matches("^\\d{4}$");
    }

    /**
     * Validates if the transaction amount is greater than zero and finite.
     *
     * @param amount the double value to validate
     * @return true if the amount is greater than zero and finite, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return !Double.isNaN(amount) && !Double.isInfinite(amount) && amount > 0;
    }
}
