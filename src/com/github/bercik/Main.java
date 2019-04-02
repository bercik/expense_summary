package com.github.bercik;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import com.github.bercik.adapter.SpendEachDayAdapter;
import com.github.bercik.commons.Transactions;
import com.github.bercik.pdf_converter.PdfConverter;
import com.github.bercik.pdf_converter.PdfReader;
import com.github.bercik.plot.TimeValuePlot;

public class Main {
    private static final String programCliArgument = "expense_summary";
    private static final SimpleDateFormat dateFormatter;
    private static final int PLOT_WIDTH = 1200;
    private static final int PLOT_HEIGHT = 800;

    static {
        dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        dateFormatter.setLenient(false);
    }

    public static void main(String[] args) throws IOException {
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

    private static void plot(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: " + programCliArgument + " " + args[0] + " [input_filepath] {output_filepath}");
            System.exit(1);
        }

        String inputFilepath = args[1];

        Transactions transactions = getTransactionsFromPdfFile(inputFilepath);

        TimeValuePlot timeValuePlot = createTimeValuePlot(transactions);

        Optional<String> outputFilepath = Optional.empty();
        if (args.length > 2) {
            outputFilepath = Optional.of(args[2]);
        }

        if (outputFilepath.isPresent()) {
            timeValuePlot.saveToFileAsJpeg(outputFilepath.get());
        } else {
            timeValuePlot.showOnScreen();
        }
    }

    private static TimeValuePlot createTimeValuePlot(Transactions transactions) {
        TimeValuePlot.ChartMetadata chartMetadata = new TimeValuePlot.ChartMetadata()
                .title("each day spending")
                .timeAxisLabel("day")
                .valueAxisLabel("spending [zł]");
        TimeValuePlot.PlotMetadata plotMetadata =
                new TimeValuePlot.PlotMetadata(new TimeValuePlot.PlotDimensions(PLOT_WIDTH, PLOT_HEIGHT))
                        .applicationTitle("each day spending")
                        .chartMetadata(chartMetadata);
        TimeValuePlot.PlotShowingOptions plotShowingOptions =
                new TimeValuePlot.PlotShowingOptions().moneyShowing(TimeValuePlot.PlotShowingOptions.MoneyShowing.ZLOTYS);
        TimeValuePlot.PlotData plotData = new SpendEachDayAdapter().adapt(transactions);
        return new TimeValuePlot(plotData, plotMetadata, plotShowingOptions);
    }

    private static void convert(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: " + programCliArgument + " " + args[0] + " [input_filepath]");
            System.exit(1);
        }
        String inputFilepath = args[1];

        Transactions transactions = getTransactionsFromPdfFile(inputFilepath);

        System.out.println(transactions.toJsonString());
    }

    private static Transactions getTransactionsFromPdfFile(String inputFilepath) throws IOException {
        PdfReader pdfReader = new PdfReader();
        String text = pdfReader.readTextFromPdf(inputFilepath);

        PdfConverter pdfConverter = new PdfConverter(dateFormatter);
        return pdfConverter.convertToTransactions(text);
    }

    private static void showHelpAndExit() {
        System.err.println("Usage: " + programCliArgument + " [command]");
        System.err.println("where command can be one of:");
        System.err.println("  convert [input_file] - converts given pdf file to JSON");
        System.err.println("  plot [input_file] {output_file} - plots given pdf file and shows on screen or if given" +
                " to output_file in jpeg format");
        System.exit(1);
    }
}


