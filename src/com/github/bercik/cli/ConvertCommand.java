package com.github.bercik.cli;

import java.util.List;

import com.github.bercik.config.Configuration;
import com.github.bercik.transactions.Transactions;

class ConvertCommand extends AbstractCommand {

    ConvertCommand() {
        super("convert");
    }

    @Override
    String execute(List<String> args) throws Exception {
        assertArgs(args);

        String inputFilepath = args.get(0);

        Transactions transactions = Transactions.getTransactionsFromPdfFile(inputFilepath,
                Configuration.getDateFormatter());

        return transactions.toJsonString() + "\n";
    }

    private void assertArgs(List<String> args) throws BadCliArgumentsException {
        if (args.size() <= 0) {
            String errorMessage = "You must provide input_file\n";
            errorMessage += "  Usage: " + name() + " " + help();

            throw new BadCliArgumentsException(errorMessage);
        }
    }

    @Override
    public String help() {
        return "[input_file] - converts given pdf file to json and prints to standard output";
    }
}
