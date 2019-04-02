package com.github.bercik.pdf_converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.bercik.transactions.Transaction;
import com.github.bercik.transactions.Transactions;

public class PdfConverter {

    private final SimpleDateFormat dateFormatter;

    public PdfConverter(SimpleDateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public Transactions convertToTransactions(String inputText) {
        inputText = normalizeText(inputText);

        List<Block> blocks = getBlocks(inputText);

        return proccessBlocks(blocks);
    }

    private Transactions proccessBlocks(List<Block> blocks) {
        Transactions transactions = new Transactions(dateFormatter);

        for (Block block : blocks) {
            transactions.addTransaction(proccessBlock(block));
        }

        return transactions;
    }

    private Transaction proccessBlock(Block block) {
        String name = null;
        StringBuilder tmpName = new StringBuilder();
        int valueInPennies = 0;
        int accountBalanceInPennies = 0;

        for (String word : block.getWords()) {
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
        // We do this because if amount is at least 1000 it is splitted using this character (it looks like space)
        // 1 000, 2 340 etc.
        word = word.replaceAll("\u00A0", "");

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

}
