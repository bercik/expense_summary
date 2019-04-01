package com.github.bercik.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

public class Converter {

    private SimpleDateFormat dateFormatter;

    public Converter() {
        dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        dateFormatter.setLenient(false);
    }

    public String convertToJSON(String inputText) {
        inputText = normalizeText(inputText);

        List<Block> blocks = getBlocks(inputText);

        List<Transaction> transactions = proccessBlocks(blocks);

        JSONObject jsonObject = toJSON(transactions);

        return jsonObject.toJSONString();
    }

    private JSONObject toJSON(List<Transaction> transactions) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("transactions", transactions);

        return jsonObject;
    }

    private List<Transaction> proccessBlocks(List<Block> blocks) {
        List<Transaction> transactions = new ArrayList<>();

        for (Block block : blocks) {
            transactions.add(proccessBlock(block));
        }

        return transactions;
    }

    private Transaction proccessBlock(Block block) {
        String name = null;
        StringBuilder tmpName = new StringBuilder();
        int valueInPennies = 0;
        int accountBalanceInPennies = 0;

        for (String word : block.getWords()) {
            word = word.replaceAll("\u00A0", "");
            if (name == null) {
                Optional<Integer> value = getValueInPennies(word);
                if (value.isPresent()) {
                    name = tmpName.substring(0, tmpName.length() - 1);
                    valueInPennies = value.get();
                } else {
                    tmpName.append(word).append(" ");
                }
            } else {
                accountBalanceInPennies = getValueInPennies(word).orElse(0);
                break;
            }
        }

        return new Transaction(block.getFirstDate(), block.getSecondDate(), name, valueInPennies,
                accountBalanceInPennies);
    }

    private Optional<Integer> getValueInPennies(String word) {
        Pattern pattern = Pattern.compile("^(-?)(\\d+),(\\d{2})$");
        Matcher matcher = pattern.matcher(word);
        if (matcher.find()) {
            String minus = matcher.group(1);
            String zloty = matcher.group(2);
            String pennies = matcher.group(3);

            int iMinus = minus.equals("-") ? -1 : 1;
            int iZloty = Integer.parseInt(zloty);
            int iPennies = Integer.parseInt(pennies);

            int valueInPennies = iMinus * iZloty * 100 + iPennies;

            return Optional.of(valueInPennies);
        }

        return Optional.empty();
    }

    private List<Block> getBlocks(String text) {
        StringBuilder word = new StringBuilder();
        Date firstDate = null;
        Date secondDate = null;
        boolean inBlock = false;
        boolean shouldNextBeSecondDate = false;
        List<String> wordsInBlock = new ArrayList<>();
        List<Block> blocks = new ArrayList<>();

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                Optional<Date> date = getDate(word.toString());

                if (shouldNextBeSecondDate && date.isEmpty()) {
                    firstDate = null;
                }
                shouldNextBeSecondDate = false;

                if (date.isPresent()) {
                    if (inBlock) {
                        blocks.add(new Block(firstDate, secondDate, wordsInBlock));

                        inBlock = false;
                        firstDate = null;
                        secondDate = null;
                        wordsInBlock = new ArrayList<>();
                    }

                    if (firstDate == null) {
                        firstDate = date.get();
                        shouldNextBeSecondDate = true;
                    } else {
                        secondDate = date.get();
                        inBlock = true;
                    }
                } else if (inBlock) {
                    wordsInBlock.add(word.toString());
                }

                word = new StringBuilder();
            } else {
                word.append(c);
            }
        }

        return blocks;
    }

    private Optional<Date> getDate(String word) {
        try {
            return Optional.of(dateFormatter.parse(word));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    private String normalizeText(String text) {
        text = text.replaceAll("\n", " ");
        text = text.replaceAll("\\s+", " ");
        return text;
    }

    private class Block {
        private final Date firstDate;
        private final Date secondDate;
        private final List<String> words;

        Block(Date firstDate, Date secondDate, List<String> words) {
            this.firstDate = firstDate;
            this.secondDate = secondDate;
            this.words = words;
        }

        Date getFirstDate() {
            return firstDate;
        }

        Date getSecondDate() {
            return secondDate;
        }

        List<String> getWords() {
            return words;
        }
    }

    // TODO dodac od kogo/do kogo przelew, tytul itp.
    private class Transaction {
        // data ksiegowania
        private final Date bookingDate;
        // data operacji
        private final Date transactionDate;
        // nazwa operacji np. PRZELEW KRAJOWY, PŁACĘ Z T-MOBILE USŁUGI BANKOWE
        private final String name;
        // wartość operacji w groszach np. -7200, 27570
        private final int valueInPennies;
        // saldo ksiegowe w groszach
        private final int accountBalanceInPennies;

        Transaction(Date bookingDate, Date transactionDate, String name, int valueInPennies, int accountBalanceInPennies) {
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
    }
}
