package com.github.bercik;

import java.io.IOException;

public class Main {
    private static final String programCliArgument = "expense_summary";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: " + programCliArgument + " [command]");
            System.err.println("where command can be one of:");
            System.err.println("  convert - converts given pdf file to JSON");
            System.exit(1);
        }

        String command = args[0];
        if ("convert".equals(command)) {
            convert(args);
        }
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
}


