package com.github.bercik;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.github.bercik.cli.BadCliArgumentsException;
import com.github.bercik.cli.Command;
import com.github.bercik.cli.ExecutionException;
import com.github.bercik.cli.RootCommand;
import com.github.bercik.config.Configuration;

public class Main {

    public static void main(String[] args) {
        Command rootCommand = initialize();

        try {
            System.out.print(rootCommand.run(Arrays.asList(args)));
        } catch (BadCliArgumentsException | ExecutionException error) {
            System.err.println(error.getMessage());
            System.exit(1);
        }
    }

    private static Command initialize() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        dateFormatter.setLenient(false);
        Configuration.setDateFormatter(dateFormatter);

        return RootCommand.create();
    }
}


