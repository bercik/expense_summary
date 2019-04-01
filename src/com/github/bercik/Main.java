package com.github.bercik;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.json.simple.parser.ParseException;

import com.github.bercik.adapter.SpendEachDayAdapter;
import com.github.bercik.commons.Transactions;
import com.github.bercik.pdf_converter.PdfConverter;
import com.github.bercik.pdf_converter.PdfReader;
import com.github.bercik.plot.TimeValuePlot;

public class Main {
    private static final String programCliArgument = "expense_summary";
    private static final SimpleDateFormat dateFormatter;

    static {
        dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        dateFormatter.setLenient(false);
    }

    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        if (args.length < 1) {
            showHelpAndExit();
        }

        String command = args[0];
        if ("convert".equals(command)) {
            convert(args);
        } else if ("plot".equals(command)) {
            plot(args);
        } else {
            showHelpAndExit();
        }
    }

    private static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private static void plot(String[] args) throws IOException, ParseException, java.text.ParseException {
        if (args.length < 2) {
            System.err.println("Usage: " + programCliArgument + " " + args[0] + " [input_filepath] {output_filepath}");
            System.exit(1);
        }

        String inputFilepath = args[1];
        Optional<String> outputFilepath = Optional.empty();
        if (args.length > 2) {
            outputFilepath = Optional.of(args[2]);
        }

        String jsonText = readFile(inputFilepath);
        Transactions transactions = Transactions.fromJson(jsonText, dateFormatter);

        SpendEachDayAdapter spendEachDayAdapter = new SpendEachDayAdapter();

        TimeValuePlot timeValuePlot = new TimeValuePlot();
        timeValuePlot.showOnScreen(spendEachDayAdapter.adapt(transactions, value -> value / 100.0));
    }

    private static void convert(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: " + programCliArgument + " " + args[0] + " [input_filepath]");
            System.exit(1);
        }
        String inputFilepath = args[1];

        PdfReader pdfReader = new PdfReader();
        String text = pdfReader.readTextFrom(inputFilepath);

        PdfConverter pdfConverter = new PdfConverter(dateFormatter);
        String jsonText = pdfConverter.convertToJSON(text);

        System.out.println(jsonText);
    }

    private static void showHelpAndExit() {
        System.err.println("Usage: " + programCliArgument + " [command]");
        System.err.println("where command can be one of:");
        System.err.println("  convert [input_file] - converts given pdf file to JSON");
        System.err.println("  plot [input_file] {output_file} - plots given json file and shows on screen or if given" +
                " to output_file in jpeg format");
        System.exit(1);
    }
}


