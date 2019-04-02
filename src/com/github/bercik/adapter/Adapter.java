package com.github.bercik.adapter;

import com.github.bercik.commons.Transactions;
import com.github.bercik.plot.TimeValuePlot;

public interface Adapter {
    TimeValuePlot.PlotData adapt(Transactions transactions);
}
