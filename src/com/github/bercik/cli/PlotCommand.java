package com.github.bercik.cli;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.github.bercik.adapter.SpendEachDayAdapter;
import com.github.bercik.config.Configuration;
import com.github.bercik.plot.TimeValuePlot;
import com.github.bercik.transactions.Transactions;

public class PlotCommand extends AbstractCommand {
    private static final int PLOT_WIDTH = 1200;
    private static final int PLOT_HEIGHT = 800;

    public PlotCommand() {
        super("plot");
    }

    @Override
    String execute(List<String> args) throws Exception {
        assertArgs(args);

        String inputFilepath = args.get(0);

        Transactions transactions = Transactions.getTransactionsFromPdfFile(inputFilepath,
                Configuration.getDateFormatter());

        TimeValuePlot timeValuePlot = createTimeValuePlot(transactions);

        Optional<String> outputFilepath = Optional.empty();
        if (args.size() > 1) {
            outputFilepath = Optional.of(args.get(1));
        }

        if (outputFilepath.isPresent()) {
            try {
                timeValuePlot.saveToFileAsJpeg(outputFilepath.get());
            } catch (IOException e) {
                throw new ExecutionException(e.getMessage());
            }
        } else {
            timeValuePlot.showOnScreen();
        }

        return "";
    }

    private void assertArgs(List<String> args) throws BadCliArgumentsException {
        if (args.size() <= 0) {
            String errorMessage = "You must provide input_file\n";
            errorMessage += "  Usage: " + name() + " " + help();

            throw new BadCliArgumentsException(errorMessage);
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

    @Override
    public String help() {
        return "[input_file] {output_file} - plots given pdf file and shows on screen or if given saves to " +
                "output_file in jpeg format";
    }
}
