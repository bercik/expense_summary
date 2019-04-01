package com.github.bercik.adapter;

import com.github.bercik.commons.Transactions;
import com.github.bercik.plot.TimeValuePlot;

public interface IAdapter {

    interface ITransformValue {
        Double transform(Long value);
    }

    TimeValuePlot.PlotData adapt(Transactions transactions, ITransformValue transformValue);
}
