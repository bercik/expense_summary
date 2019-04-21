package com.github.bercik.transactions;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsTest {
    @Test
    public void equalsShouldReturnTrueIfContainsTheSameTransactionsInTheSameOrder() {
        // given
        Transactions transactions1 = createTransactions(createFirstTransaction(), createSecondTransaction());
        Transactions transactions2 = createTransactions(createFirstTransaction(), createSecondTransaction());

        // when
        boolean areEqual = transactions1.equals(transactions2);

        // then
        assertThat(areEqual).isTrue();
    }

    @Test
    public void equalsShouldReturnFalseIfContainsDiffrentTransactions() {
        // given
        Transactions transactions1 = createTransactions(createFirstTransaction(), createSecondTransaction());
        Transactions transactions2 = createTransactions(createFirstTransaction(), createThirdTransaction());

        // when
        boolean areEqual = transactions1.equals(transactions2);

        // then
        assertThat(areEqual).isFalse();
    }

    private Transactions createTransactions(Transaction... transactionList) {
        Transactions transactions = new Transactions(new SimpleDateFormat());

        for (Transaction transaction : transactionList) {
            transactions.addTransaction(transaction);
        }

        return transactions;
    }

    private Transaction createFirstTransaction() {
        Date bookingDate = new GregorianCalendar(2019, GregorianCalendar.APRIL, 15).getTime();
        Date transactionDate = new GregorianCalendar(2019, GregorianCalendar.APRIL, 22).getTime();
        String name = "transaction1";
        long valueInPennies = 1500L;
        long accountBalanceInPennies = 24000L;

        return Transaction.builder()
                .bookingDate(bookingDate)
                .transactionDate(transactionDate)
                .name(name)
                .valueInPennies(valueInPennies)
                .accountBalanceInPennies(accountBalanceInPennies)
                .build();
    }

    private Transaction createSecondTransaction() {
        Date bookingDate = new GregorianCalendar(2020, GregorianCalendar.MAY, 16).getTime();
        Date transactionDate = new GregorianCalendar(2020, GregorianCalendar.MAY, 23).getTime();
        String name = "transaction2";
        long valueInPennies = 3000L;
        long accountBalanceInPennies = 48000L;

        return Transaction.builder()
                .bookingDate(bookingDate)
                .transactionDate(transactionDate)
                .name(name)
                .valueInPennies(valueInPennies)
                .accountBalanceInPennies(accountBalanceInPennies)
                .build();
    }

    private Transaction createThirdTransaction() {
        Date bookingDate = new GregorianCalendar(2021, GregorianCalendar.JULY, 17).getTime();
        Date transactionDate = new GregorianCalendar(2021, GregorianCalendar.JULY, 24).getTime();
        String name = "transaction3";
        long valueInPennies = 4500L;
        long accountBalanceInPennies = 68000L;

        return Transaction.builder()
                .bookingDate(bookingDate)
                .transactionDate(transactionDate)
                .name(name)
                .valueInPennies(valueInPennies)
                .accountBalanceInPennies(accountBalanceInPennies)
                .build();
    }
}