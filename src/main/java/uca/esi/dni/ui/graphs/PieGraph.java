package uca.esi.dni.ui.graphs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.Rectangle;

import java.awt.*;
import java.util.List;

public class PieGraph extends Graph {
    private final String field;

    public PieGraph(PApplet parent, Rectangle rectangle, String charTitle, String field) {
        super(parent, rectangle);
        this.chart = ChartFactory.createPieChart(
                charTitle,
                createEmptyPieDataSet(),
                true, true, false);
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.setBackgroundImageAlpha(100);
        this.chartImage = new PImage(chart.createBufferedImage(w, h));
        this.field = field;
    }

    @Override
    public void updateData(List<Survey> surveyList) {
        JFreeChart pieChart = ChartFactory.createPieChart(
                chart.getTitle().getText(),
                createPieDataset(surveyList, field),
                true, true, false);
        this.chart = pieChart;
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.setBackgroundImageAlpha(100);
        this.chartImage = new PImage(pieChart.createBufferedImage(w, h));
    }

    private PieDataset createPieDataset(List<Survey> surveyList, String field) {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        int[] results = getCombinedData(surveyList, field);

        dataset.setValue("Si", results[0]);
        dataset.setValue("No", results[1]);

        return dataset;
    }

    private int[] getCombinedData(List<Survey> surveyList, String field) {
        int[] combined = new int[2];
        for (Survey survey : surveyList) {
            if (survey.getValue(field)) {
                combined[0]++;
            } else {
                combined[1]++;
            }
        }
        return combined;
    }
}
