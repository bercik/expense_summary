package com.github.bercik.transactions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bercik.pdf_converter.PdfConverter;
import com.github.bercik.pdf_converter.PdfReader;

public class Transactions implements Iterable<Transaction> {
    private final List<Transaction> transactions;
    private final SimpleDateFormat dateFormatter;

    public Transactions(SimpleDateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
        this.transactions = new ArrayList<>();
    }

    public static Transactions getTransactionsFromPdfFile(String inputFilepath, SimpleDateFormat dateFormatter)
            throws IOException {
        PdfReader pdfReader = new PdfReader();
        String text = pdfReader.readTextFromPdf(inputFilepath);

        PdfConverter pdfConverter = new PdfConverter(dateFormatter);
        return pdfConverter.convertToTransactions(text);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormatter);

        return mapper.writeValueAsString(transactions);
    }

    public Transaction get(int index) {
        return transactions.get(index);
    }

    public int size() {
        return transactions.size();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transactions that = (Transactions) o;

        return transactions.equals(that.transactions);
    }

    @Override
    public int hashCode() {
        return transactions.hashCode();
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transactions=" + transactions +
                '}';
    }
}
