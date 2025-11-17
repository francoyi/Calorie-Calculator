package com.caloriecalc.ui;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.service.FoodLogService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {

    private FoodLogService foodLogService;

    public GraphPanel(FoodLogService foodLogService) {
        this.foodLogService = foodLogService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JFreeChart lineChart = createChart();
        ChartPanel chartPanel = new ChartPanel(lineChart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart() {
        TimeSeriesCollection dataset = createDataset();
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Calorie and Weight Tracker",
                "Date",
                "Value",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        return chart;
    }

    private TimeSeriesCollection createDataset() {
        TimeSeries caloriesSeries = new TimeSeries("Calories");
        TimeSeries bmrSeries = new TimeSeries("BMR");
        TimeSeries weightSeries = new TimeSeries("Weight");

        List<DailyLog> dailyLogs = foodLogService.getAllDailyLogs();
        UserSettings userSettings = foodLogService.getSettings();

        for (DailyLog log : dailyLogs) {
            caloriesSeries.add(new Day(log.getDate().getDayOfMonth(), log.getDate().getMonthValue(), log.getDate().getYear()), log.getTotalKcal());
        }

        // For BMR and Weight, we'd ideally have historical data.
        // For now, let's assume we plot the current BMR and weight for the days we have calorie data.
        if (userSettings != null) {
            double bmr = userSettings.getBmr();
            double weight = userSettings.getWeight();
            for (DailyLog log : dailyLogs) {
                if (bmr > 0) {
                    bmrSeries.add(new Day(log.getDate().getDayOfMonth(), log.getDate().getMonthValue(), log.getDate().getYear()), bmr);
                }
                if (weight > 0) {
                    weightSeries.add(new Day(log.getDate().getDayOfMonth(), log.getDate().getMonthValue(), log.getDate().getYear()), weight);
                }
            }
        }


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(caloriesSeries);
        dataset.addSeries(bmrSeries);
        dataset.addSeries(weightSeries);

        return dataset;
    }
}

