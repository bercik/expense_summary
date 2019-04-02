package com.github.bercik.transactions;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

// TODO dodac od kogo/do kogo przelew, tytul itp.
public class Transaction {
    private SimpleDateFormat dateFormatter;
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

    public Transaction(SimpleDateFormat dateFormatter, Date bookingDate, Date transactionDate, String name,
                       long valueInPennies,
                       long accountBalanceInPennies) {
        this.dateFormatter = dateFormatter;
        this.bookingDate = bookingDate;
        this.transactionDate = transactionDate;
        this.name = name;
        this.valueInPennies = valueInPennies;
        this.accountBalanceInPennies = accountBalanceInPennies;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", name);
        jsonObject.put("bookingDate", dateFormatter.format(bookingDate));
        jsonObject.put("transactionDate", dateFormatter.format(transactionDate));
        jsonObject.put("valueInPennies", valueInPennies);
        jsonObject.put("accountBalanceInPennies", accountBalanceInPennies);

        return jsonObject.toJSONString();
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getName() {
        return name;
    }

    public long getValueInPennies() {
        return valueInPennies;
    }

    public long getAccountBalanceInPennies() {
        return accountBalanceInPennies;
    }
}
