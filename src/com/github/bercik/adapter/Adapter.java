package com.github.bercik.adapter;

import com.github.bercik.plot.TimeValuePlot;
import com.github.bercik.transactions.Transactions;

interface Adapter {
    @SuppressWarnings("unused")
    TimeValuePlot.PlotData adapt(Transactions transactions);
}
