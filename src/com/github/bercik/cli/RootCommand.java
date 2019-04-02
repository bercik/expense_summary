package com.github.bercik.cli;

public class RootCommand extends AbstractCommand {
    private RootCommand() {
        super("");
    }

    public static Command create() {
        Command rootCommand = new RootCommand();
        rootCommand.addSubcommand(new ConvertCommand());
        rootCommand.addSubcommand(new PlotCommand());

        return rootCommand;
    }
}
