package com.github.bercik.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYDataset;

public class Plot {

    public Plot(String jsonText) {
    }

    class PlotOnScreen extends ApplicationFrame {

        PlotOnScreen(String applicationTitle, String chartTitle, XYDataset dataset) {
            super(applicationTitle);
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    chartTitle,
                    "Category",
                    "Score",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            ChartPanel chartPanel = new ChartPanel(xylineChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
            final XYPlot plot = xylineChart.getXYPlot();

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.GREEN);
            renderer.setSeriesPaint(2, Color.YELLOW);
            renderer.setSeriesStroke(0, new BasicStroke(4.0f));
            renderer.setSeriesStroke(1, new BasicStroke(3.0f));
            renderer.setSeriesStroke(2, new BasicStroke(2.0f));
            plot.setRenderer(renderer);
            setContentPane(chartPanel);
        }

        void showOnScreen() {
            pack();
            setVisible(true);
        }
    }

    public void showOnScreen(XYDataset xyDataset) {
        PlotOnScreen plotOnScreen = new PlotOnScreen("Expense summary", "expense summary", xyDataset);
        plotOnScreen.showOnScreen();
    }

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
