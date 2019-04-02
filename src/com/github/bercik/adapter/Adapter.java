package com.github.bercik.adapter;

import com.github.bercik.plot.TimeValuePlot;
import com.github.bercik.transactions.Transactions;

public interface Adapter {
    TimeValuePlot.PlotData adapt(Transactions transactions);
}
