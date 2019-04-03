package com.github.bercik.pdf_converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.bercik.transactions.Transactions;

@RunWith(MockitoJUnitRunner.class)
public class PdfConverterTest {

    @Ignore
    @Test
    public void testConvertToTransactions() throws Exception {
        // given
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        PdfConverter sut = new PdfConverter(dateFormatter);

        // when
        String inputText = readInputText();
        System.out.println(inputText);
        Transactions result = sut.convertToTransactions(inputText);

        // then
        Transactions expected = new Transactions(dateFormatter);
        assertThat(result).isEqualTo(expected);
    }

    public String readInputText() throws IOException {
        return IOUtils.toString(
                this.getClass().getResourceAsStream("./pdf_text.txt"), "UTF-8"
        );
    }
}