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
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class TimeValuePlot {

    public static final int PLOT_WIDTH = 1200;
    public static final int PLOT_HEIGHT = 800;

    static public class PlotShowingOptions {
        public enum MoneyShowing implements ValueTransformer {
            PENNIES {
                @Override
                public double transform(double pennies) {
                    return pennies;
                }
            },
            ZLOTYS {
                @Override
                public double transform(double pennies) {
                    return pennies / 100.0;
                }
            }
        }

        private MoneyShowing moneyShowing;

        public PlotShowingOptions moneyShowing(MoneyShowing moneyShowing) {
            this.moneyShowing = moneyShowing;
            return this;
        }
    }

    static public class PlotMetadata {
        String applicationTitle = "";
        ChartMetadata chartMetadata = new ChartMetadata();

        public PlotMetadata applicationTitle(String applicationTitle) {
            this.applicationTitle = applicationTitle;
            return this;
        }

        public PlotMetadata chartMetadata(ChartMetadata chartMetadata) {
            this.chartMetadata = chartMetadata;
            return this;
        }
    }

    static public class ChartMetadata {
        String title = "";
        String timeAxisLabel = "";
        String valueAxisLabel = "";

        public ChartMetadata title(String title) {
            this.title = title;
            return this;
        }

        public ChartMetadata timeAxisLabel(String timeAxisLabel) {
            this.timeAxisLabel = timeAxisLabel;
            return this;
        }

        public ChartMetadata valueAxisLabel(String valueAxisLabel) {
            this.valueAxisLabel = valueAxisLabel;
            return this;
        }
    }

    interface ValueTransformer {
        double transform(double value);
    }

    static public class PlotData {
        private List<PlotSeries> plotSeriesList;

        public PlotData() {
            this.plotSeriesList = new ArrayList<>();
        }

        public void addSeries(PlotSeries plotSeries) {
            plotSeriesList.add(plotSeries);
        }

        XYDataset toTimeSeriesCollection(ValueTransformer valueTransformer) {
            TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

            for (PlotSeries plotSeries : plotSeriesList) {
                timeSeriesCollection.addSeries(plotSeries.toTimeSeries(valueTransformer));
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

        TimeSeries toTimeSeries(ValueTransformer valueTransformer) {
            TimeSeries timeSeries = new TimeSeries(name);

            for (PlotPoint plotPoint : plotPoints) {
                timeSeries.add(new Day(plotPoint.date), valueTransformer.transform(plotPoint.value));
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

    private final PlotData plotData;
    private final PlotMetadata plotMetadata;
    private final PlotShowingOptions plotShowingOptions;

    public TimeValuePlot(PlotData plotData, PlotMetadata plotMetadata, PlotShowingOptions plotShowingOptions) {
        this.plotData = plotData;
        this.plotMetadata = plotMetadata;
        this.plotShowingOptions = plotShowingOptions;
    }

    public void showOnScreen() {
        new PlotOnScreen(plotData, plotMetadata, plotShowingOptions).showOnScreen();
    }

    public void saveToFile(String outputFilepath) throws IOException {
        new PlotToFile().saveToFile(plotData, plotMetadata, plotShowingOptions, outputFilepath);
    }

    static class PlotOnScreen extends ApplicationFrame {

        PlotOnScreen(PlotData plotData, PlotMetadata plotMetadata, PlotShowingOptions plotShowingOptions) {
            super(plotMetadata.applicationTitle);

            JFreeChart timeseriesChart =
                    new TimeseriesChart()
                            .create(plotData.toTimeSeriesCollection(plotShowingOptions.moneyShowing), plotMetadata);

            ChartPanel chartPanel = new ChartPanel(timeseriesChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(PLOT_WIDTH, PLOT_HEIGHT));

            setContentPane(chartPanel);
        }

        void showOnScreen() {
            pack();
            setVisible(true);
        }

    }

    static class PlotToFile {
        public void saveToFile(PlotData plotData, PlotMetadata plotMetadata,
                               PlotShowingOptions plotShowingOptions, String outputFilepath) throws IOException {
            JFreeChart timeseriesChart =
                    new TimeseriesChart()
                            .create(plotData.toTimeSeriesCollection(plotShowingOptions.moneyShowing), plotMetadata);

            File XYChart = new File(outputFilepath);
            ChartUtils.saveChartAsJPEG(XYChart, timeseriesChart, PLOT_WIDTH, PLOT_HEIGHT);
        }
    }

    static class TimeseriesChart {
        TimeseriesChart() {
        }

        JFreeChart create(final XYDataset dataset, PlotMetadata plotMetadata) {
            return ChartFactory.createTimeSeriesChart(
                    plotMetadata.chartMetadata.title,
                    plotMetadata.chartMetadata.timeAxisLabel,
                    plotMetadata.chartMetadata.valueAxisLabel,
                    dataset,
                    false,
                    false,
                    false);
        }
    }
}
