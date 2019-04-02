package com.github.bercik.adapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.github.bercik.plot.TimeValuePlot;
import com.github.bercik.transactions.Transaction;
import com.github.bercik.transactions.Transactions;

public class SpendEachDayAdapter implements Adapter {
    @Override
    public TimeValuePlot.PlotData adapt(Transactions transactions) {

        Map<Date, Long> everyDaySpendingMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            Date transactionDate = transaction.getTransactionDate();

            if (!everyDaySpendingMap.containsKey(transactionDate)) {
                everyDaySpendingMap.put(transactionDate, 0L);
            }

            Long value = everyDaySpendingMap.get(transactionDate);
            everyDaySpendingMap.put(transactionDate, value + transaction.getValueInPennies());
        }

        TimeValuePlot.PlotSeries plotSeries = new TimeValuePlot.PlotSeries("spending each day");

        for (Map.Entry<Date, Long> entry : everyDaySpendingMap.entrySet()) {
            TimeValuePlot.PlotPoint plotPoint = new TimeValuePlot.PlotPoint(entry.getKey(), entry.getValue());
            plotSeries.addPlotPoint(plotPoint);
        }

        TimeValuePlot.PlotData plotData = new TimeValuePlot.PlotData();
        plotData.addSeries(plotSeries);

        return plotData;
    }
}
