package com.github.bercik.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.json.simple.JSONObject;

public class Transactions implements Iterable<Transaction> {
    private final List<Transaction> transactions;

    public Transactions() {
        this.transactions = new ArrayList<>();
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
