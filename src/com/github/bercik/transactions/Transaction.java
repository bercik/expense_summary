package com.github.bercik.transactions;

import java.util.Date;

// TODO dodac od kogo/do kogo przelew, tytul itp.
public class Transaction {
    // data ksiegowania
    private final Date bookingDate;
    // data operacji
    private final Date transactionDate;
    // nazwa operacji np. PRZELEW KRAJOWY, PŁACĘ Z T-MOBILE USŁUGI BANKOWE
    private final String name;
    // wartość operacji w groszach np. -7200, 27570
    private final long valueInPennies;
    // saldo ksiegowe w groszach
    private final long accountBalanceInPennies;

    public Transaction(Date bookingDate, Date transactionDate, String name,
                       long valueInPennies,
                       long accountBalanceInPennies) {
        this.bookingDate = bookingDate;
        this.transactionDate = transactionDate;
        this.name = name;
        this.valueInPennies = valueInPennies;
        this.accountBalanceInPennies = accountBalanceInPennies;
    }

    @SuppressWarnings("unused")
    public Date getBookingDate() {
        return bookingDate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public long getValueInPennies() {
        return valueInPennies;
    }

    @SuppressWarnings("unused")
    public long getAccountBalanceInPennies() {
        return accountBalanceInPennies;
    }
}
