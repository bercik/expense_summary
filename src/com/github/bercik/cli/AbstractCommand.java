package com.github.bercik.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract class AbstractCommand implements Command {
    private static final String HELP_INDENT = "  ";
    private final List<Command> subCommands = new ArrayList<>();
    private final String name;

    AbstractCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean isApplicable(String command) {
        return command.equals(name);
    }

    @Override
    public final String run(List<String> args) throws BadCliArgumentsException, ExecutionException {
        Optional<String> result = tryToRunSubCommand(args);
        if (result.isPresent()) {
            return result.get();
        }

        return tryToExecute(args);
    }

    private String tryToExecute(List<String> args) throws BadCliArgumentsException, ExecutionException {
        try {
            return execute(args);
        } catch (BadCliArgumentsException e) {
            throw e;
        } catch (Exception e) {
            throw new ExecutionException(e.getMessage());
        }
    }

    private Optional<String> tryToRunSubCommand(List<String> args)
            throws BadCliArgumentsException, ExecutionException {
        if (args.size() > 0) {
            String possibleSubCommand = args.get(0);
            for (Command subCommand : subCommands) {
                if (subCommand.isApplicable(possibleSubCommand)) {
                    return Optional.of(subCommand.run(args.subList(1, args.size())));
                }
            }
        }

        return Optional.empty();
    }

    String execute(List<String> args) throws Exception {
        throw new BadCliArgumentsException(badUsageMessageIfExecuteIsNotOverriden(args));
    }

    private String badUsageMessageIfExecuteIsNotOverriden(List<String> args) {
        String errorMessage;
        if (args.size() <= 0) {
            errorMessage = "You must provide command";
        } else {
            errorMessage = "Unknown command " + args.get(0);
        }

        errorMessage += "\n";
        errorMessage += help();

        return errorMessage;
    }

    @Override
    public String help() {
        StringBuilder result = new StringBuilder();
        result.append("Possible commands:\n");

        for (Command subCommand : subCommands) {
            String[] helpLines = subCommand.help().split("\n");
            for (String helpLine : helpLines) {
                result.append(HELP_INDENT).append(subCommand.name()).append(" ").append(helpLine).append("\n");
            }
        }

        return removeLastCharacter(result);
    }

    private String removeLastCharacter(StringBuilder result) {
        return result.substring(0, result.length() - 1);
    }

    @Override
    public final void addSubcommand(Command command) {
        subCommands.add(command);
    }

    @Override
    public final String name() {
        return name;
    }
}
