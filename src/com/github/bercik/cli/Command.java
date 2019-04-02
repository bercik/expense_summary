package com.github.bercik.cli;

import java.util.List;

public interface Command {
    boolean isApplicable(String command);

    String run(List<String> args) throws BadCliArgumentsException, ExecutionException;

    String name();

    String help();

    void addSubcommand(Command command);
}
