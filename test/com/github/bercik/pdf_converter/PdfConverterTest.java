package com.github.bercik.pdf_converter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.bercik.transactions.Transaction;
import com.github.bercik.transactions.Transactions;

@RunWith(MockitoJUnitRunner.class)
public class PdfConverterTest {

    @Test
    public void testConvertToTransactions() throws Exception {
        // given
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        PdfConverter sut = new PdfConverter(dateFormatter);

        // when
        String inputText = readInputText();
        Transactions result = sut.convertToTransactions(inputText);

        // then
        // 0
        Transactions expected = new Transactions(dateFormatter);
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .transactionDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .name("PRZELEW KRAJOWY")
                .valueInPennies(130)
                .accountBalanceInPennies(3470)
                .build());

        // 1
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .transactionDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .name("PRZELEW KRAJOWY")
                .valueInPennies(130)
                .accountBalanceInPennies(3770)
                .build());

        // 2
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .transactionDate(getTime(2019, GregorianCalendar.FEBRUARY, 1))
                .name("PŁACĘ Z T-MOBILE USŁUGI BANKOWE")
                .valueInPennies(-700)
                .accountBalanceInPennies(7570)
                .build());

        // 3
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .name("PRZELEW WŁASNY")
                .valueInPennies(100000)
                .accountBalanceInPennies(127570)
                .build());

        // 4
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.JANUARY, 31))
                .name("TRANSAKCJA KARTĄ DEBETOWĄ")
                .valueInPennies(-5)
                .accountBalanceInPennies(12475)
                .build());

        // 5
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.JANUARY, 31))
                .name("TRANSAKCJA KARTĄ DEBETOWĄ")
                .valueInPennies(-78)
                .accountBalanceInPennies(12553)
                .build());

        // 6
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.JANUARY, 31))
                .name("TRANSAKCJA KARTĄ DEBETOWĄ")
                .valueInPennies(-12)
                .accountBalanceInPennies(12365)
                .build());

        // 7
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.JANUARY, 31))
                .name("TRANSAKCJA KARTĄ DEBETOWĄ")
                .valueInPennies(-210)
                .accountBalanceInPennies(11380)
                .build());

        // 8
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 2))
                .transactionDate(getTime(2019, GregorianCalendar.JANUARY, 31))
                .name("TRANSAKCJA KARTĄ DEBETOWĄ")
                .valueInPennies(-1500)
                .accountBalanceInPennies(117880)
                .build());

        // 9
        expected.addTransaction(Transaction.builder()
                .bookingDate(getTime(2019, GregorianCalendar.FEBRUARY, 5))
                .transactionDate(getTime(2019, GregorianCalendar.FEBRUARY, 5))
                .name("PRZELEW KRAJOWY")
                .valueInPennies(107233)
                .accountBalanceInPennies(219474)
                .build());

        assertThat(result).isEqualTo(expected);
    }

    private Date getTime(@SuppressWarnings("SameParameterValue") int year, int month, int dayOfMonth) {
        return new GregorianCalendar(year, month, dayOfMonth).getTime();
    }

    public String readInputText() throws IOException {
        //noinspection ConstantConditions
        return IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("tmobile_pdf_text.txt"),
                Charsets.toCharset("UTF-8")
        );
    }

}