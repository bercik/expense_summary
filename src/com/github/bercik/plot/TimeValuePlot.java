package com.github.bercik.plot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class TimeValuePlot {

    static public class PlotData {
        List<PlotSeries> plotSeriesList;

        public PlotData() {
            this.plotSeriesList = new ArrayList<>();
        }

        public void addSeries(PlotSeries plotSeries) {
            plotSeriesList.add(plotSeries);
        }

        XYDataset toTimeSeriesCollection() {
            TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

            for (PlotSeries plotSeries : plotSeriesList) {
                timeSeriesCollection.addSeries(plotSeries.toTimeSeries());
            }

            return timeSeriesCollection;
        }
    }

    static public class PlotSeries {
        private final List<PlotPoint> plotPoints;
        private final String name;

        public PlotSeries(String name) {
            this.name = name;
            this.plotPoints = new ArrayList<>();
        }

        public void addPlotPoint(PlotPoint plotPoint) {
            for (PlotPoint pp : plotPoints) {
                if (pp.date.equals(plotPoint.date)) {
                    throw new IllegalArgumentException("Date " + pp.date + " is already in PlotSeries. Avoid " +
                            "duplicate dates in PlotSeries");
                }
            }

            plotPoints.add(plotPoint);
        }

        TimeSeries toTimeSeries() {
            TimeSeries timeSeries = new TimeSeries(name);

            for (PlotPoint plotPoint : plotPoints) {
                timeSeries.add(new Day(plotPoint.date), plotPoint.value);
            }

            return timeSeries;
        }
    }

    static public class PlotPoint {
        private final Date date;
        private final double value;

        public PlotPoint(Date date, double value) {
            this.date = date;
            this.value = value;
        }
    }

    public TimeValuePlot() {
    }

    class PlotOnScreen extends ApplicationFrame {

        PlotOnScreen(PlotData plotData, String applicationTitle) {
            super(applicationTitle);

            JFreeChart timeseriesChart = createTimeseriesChart(plotData.toTimeSeriesCollection());

            ChartPanel chartPanel = new ChartPanel(timeseriesChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(1200, 800));

            setContentPane(chartPanel);
        }

        private JFreeChart createTimeseriesChart(final XYDataset dataset) {
            return ChartFactory.createTimeSeriesChart(
                    "Expense summary",
                    "Date",
                    "Spending",
                    dataset,
                    false,
                    false,
                    false);
        }

        void showOnScreen() {
            pack();
            setVisible(true);
        }
    }

    public void showOnScreen(PlotData plotData) {
        PlotOnScreen plotOnScreen = new PlotOnScreen(plotData, "expense summary");
        plotOnScreen.showOnScreen();
    }

    // TODO
    public void saveToFile(XYDataset dataset, String outputFilepath) throws IOException {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Browser usage statastics",
                "Category",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File XYChart = new File(outputFilepath);
        ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
    }
}
