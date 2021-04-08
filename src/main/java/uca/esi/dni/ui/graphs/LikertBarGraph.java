package uca.esi.dni.ui.graphs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.Rectangle;

import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LikertBarGraph extends Graph {

    public LikertBarGraph(PApplet parent, Rectangle rectangle, String chartTitle, String xAxisLabel, String yAxisLabel) {
        super(parent, rectangle);
        this.chart = ChartFactory.createBarChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                createEmptyCategoryDataSet(),
                PlotOrientation.VERTICAL,
                true, true, false);
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.setBackgroundImageAlpha(100);
        this.chartImage = new PImage(chart.createBufferedImage(w, h));
    }

    @Override
    public void updateData(List<Survey> surveyList) {
        JFreeChart barChart = ChartFactory.createBarChart(
                chart.getTitle().getText(),
                chart.getCategoryPlot().getDomainAxis().getLabel(),
                chart.getCategoryPlot().getRangeAxis().getLabel(),
                createCategoryDataset(surveyList),
                PlotOrientation.VERTICAL,
                true, true, false);
        this.chart = barChart;
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.setBackgroundImageAlpha(100);
        this.chartImage = new PImage(barChart.createBufferedImage(w, h));
    }

    private CategoryDataset createCategoryDataset(List<Survey> surveyList) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Survey.LIKERT_FIELDS, int[]> combined = getCombinedData(surveyList);

        for (Map.Entry<Survey.LIKERT_FIELDS, int[]> entry : combined.entrySet()) {
            for (int i = 0; i < entry.getValue().length; i++) {
                dataset.addValue(entry.getValue()[i], Integer.toString(i + 1), entry.getKey().toString());
            }
        }
        return dataset;
    }

    private Map<Survey.LIKERT_FIELDS, int[]> getCombinedData(List<Survey> surveyList) {
        Map<Survey.LIKERT_FIELDS, int[]> combined = getInitialMap();

        for (Survey survey : surveyList) {
            for (Survey.LIKERT_FIELDS field : Survey.LIKERT_FIELDS.values()) {
                int valInField = survey.getLikertValue(field);
                //The scores from 1-5 get counted in an array from 0-4
                combined.get(field)[valInField - 1]++;
            }
        }
        return combined;
    }

    private Map<Survey.LIKERT_FIELDS, int[]> getInitialMap() {
        Map<Survey.LIKERT_FIELDS, int[]> map = new EnumMap<>(Survey.LIKERT_FIELDS.class);
        for (Survey.LIKERT_FIELDS field : Survey.LIKERT_FIELDS.values()) {
            map.put(field, new int[5]);
        }
        return map;
    }
}
