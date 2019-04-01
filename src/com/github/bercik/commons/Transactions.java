package com.github.bercik.commons;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Transactions implements Iterable<Transaction> {
    private final List<Transaction> transactions;

    public Transactions() {
        this.transactions = new ArrayList<>();
    }

    public static Transactions fromJson(String jsonText, SimpleDateFormat dateFormatter) throws ParseException, java.text.ParseException {
        JSONParser parser = new JSONParser();
        JSONObject rootObject = (JSONObject) parser.parse(jsonText);
        JSONArray transactionsArray = (JSONArray) rootObject.get("transactions");

        Transactions transactions = new Transactions();

        for (Object transactionObject : transactionsArray) {
            JSONObject transactionJsonObject = (JSONObject) transactionObject;

            Date bookingDate = dateFormatter.parse((String) transactionJsonObject.get("bookingDate"));
            Date transactionDate = dateFormatter.parse((String) transactionJsonObject.get("transactionDate"));
            String name = (String) transactionJsonObject.get("name");
            long valueInPennies = (long) transactionJsonObject.get("valueInPennies");
            long accountBalanceInPennies = (long) transactionJsonObject.get(
                    "accountBalanceInPennies");

            Transaction transaction = new Transaction(dateFormatter, bookingDate, transactionDate, name,
                    valueInPennies, accountBalanceInPennies);
            transactions.addTransaction(transaction);
        }

        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("transactions", transactions);

        return jsonObject.toJSONString();
    }

    @Override
    public Iterator<Transaction> iterator() {
        return transactions.iterator();
    }

    @Override
    public void forEach(Consumer<? super Transaction> action) {
        transactions.forEach(action);
    }

    @Override
    public Spliterator<Transaction> spliterator() {
        return transactions.spliterator();
    }
}
