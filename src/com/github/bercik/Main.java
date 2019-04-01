package com.github.bercik;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.github.bercik.convert.Converter;
import com.github.bercik.convert.PdfReader;
import com.github.bercik.plot.Plot;

public class Main {
    private static final String programCliArgument = "expense_summary";

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

    private static XYDataset createDataset() {
        final XYSeries firefox = new XYSeries("Firefox");
        firefox.add(1.0, 1.0);
        firefox.add(2.0, 4.0);
        firefox.add(3.0, 3.0);

        final XYSeries chrome = new XYSeries("Chrome");
        chrome.add(1.0, 4.0);
        chrome.add(2.0, 5.0);
        chrome.add(3.0, 6.0);

        final XYSeries iexplorer = new XYSeries("InternetExplorer");
        iexplorer.add(3.0, 4.0);
        iexplorer.add(4.0, 5.0);
        iexplorer.add(5.0, 4.0);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(firefox);
        dataset.addSeries(chrome);
        dataset.addSeries(iexplorer);
        return dataset;
    }

    private static void plot(String[] args) {
        XYDataset xyDataset = createDataset();
        Plot plot = new Plot();
        plot.showOnScreen(xyDataset);
    }

    private static void convert(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: " + programCliArgument + " " + args[0] + " [input_filepath]");
            System.exit(1);
        }
        String inputFilepath = args[1];

        PdfReader pdfReader = new PdfReader();
        String text = pdfReader.readTextFrom(inputFilepath);

        Converter converter = new Converter();
        String jsonText = converter.convertToJSON(text);

        System.out.println(jsonText);
    }

    private static void showHelpAndExit() {
        System.err.println("Usage: " + programCliArgument + " [command]");
        System.err.println("where command can be one of:");
        System.err.println("  convert [input_file] - converts given pdf file to JSON");
        System.err.println("  plot [input_file] {output_file} - plots given json file on screen or if given to " +
                "output_file in jpeg format");
        System.exit(1);
    }
}


