package com.github.bercik.transactions;

import java.util.Date;

// TODO dodac od kogo/do kogo przelew, tytul itp.
public class Transaction {

    public static class TransactionBuilder {
        private Date bookingDate;
        private Date transactionDate;
        private String name;
        private long valueInPennies;
        private long accountBalanceInPennies;

        private TransactionBuilder() {
        }

        public TransactionBuilder bookingDate(Date bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public TransactionBuilder transactionDate(Date transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TransactionBuilder valueInPennies(long valueInPennies) {
            this.valueInPennies = valueInPennies;
            return this;
        }

        public TransactionBuilder accountBalanceInPennies(long accountBalanceInPennies) {
            this.accountBalanceInPennies = accountBalanceInPennies;
            return this;
        }

        public Transaction build() {
            return new Transaction(bookingDate, transactionDate, name, valueInPennies, accountBalanceInPennies);
        }
    }

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

    private Transaction(Date bookingDate, Date transactionDate, String name,
                        long valueInPennies,
                        long accountBalanceInPennies) {
        this.bookingDate = bookingDate;
        this.transactionDate = transactionDate;
        this.name = name;
        this.valueInPennies = valueInPennies;
        this.accountBalanceInPennies = accountBalanceInPennies;
    }

    static public TransactionBuilder builder() {
        return new TransactionBuilder();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (valueInPennies != that.valueInPennies) return false;
        if (accountBalanceInPennies != that.accountBalanceInPennies) return false;
        if (bookingDate != null ? !bookingDate.equals(that.bookingDate) : that.bookingDate != null) return false;
        if (transactionDate != null ? !transactionDate.equals(that.transactionDate) : that.transactionDate != null)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = bookingDate != null ? bookingDate.hashCode() : 0;
        result = 31 * result + (transactionDate != null ? transactionDate.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (valueInPennies ^ (valueInPennies >>> 32));
        result = 31 * result + (int) (accountBalanceInPennies ^ (accountBalanceInPennies >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "bookingDate=" + bookingDate +
                ", transactionDate=" + transactionDate +
                ", name='" + name + '\'' +
                ", valueInPennies=" + valueInPennies +
                ", accountBalanceInPennies=" + accountBalanceInPennies +
                '}';
    }
}
