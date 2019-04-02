package com.github.bercik.adapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.bercik.commons.Transaction;
import com.github.bercik.commons.Transactions;
import com.github.bercik.plot.TimeValuePlot;

public class SpendEachDayAdapter implements Adapter {
    @Override
    public TimeValuePlot.PlotData adapt(Transactions transactions) {

        Map<Date, Long> abc = new HashMap<>();

        for (Transaction transaction : transactions) {
            Date transactionDate = transaction.getTransactionDate();

            if (!abc.containsKey(transactionDate)) {
                abc.put(transactionDate, 0L);
            }

            Long value = abc.get(transactionDate);
            abc.put(transactionDate, value + transaction.getValueInPennies());
        }

        TimeValuePlot.PlotSeries plotSeries = new TimeValuePlot.PlotSeries("spending each day");

        for (Map.Entry<Date, Long> entry : abc.entrySet()) {
            TimeValuePlot.PlotPoint plotPoint = new TimeValuePlot.PlotPoint(entry.getKey(), entry.getValue());
            plotSeries.addPlotPoint(plotPoint);
        }

        TimeValuePlot.PlotData plotData = new TimeValuePlot.PlotData();
        plotData.addSeries(plotSeries);

        return plotData;
    }
}
