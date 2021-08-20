package uca.esi.dni.ui.graphs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.views.View;

import java.awt.*;
import java.util.List;

/**
 * The type Pie graph.
 */
public class PieGraph extends Graph {

    /**
     * The Field.
     */
    private final String field;

    /**
     * Instantiates a new Pie graph.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     * @param charTitle the char title
     * @param field     the field
     */
    public PieGraph(PApplet parent, Rectangle rectangle, String charTitle, String field) {
        super(parent, rectangle);
        this.chart = ChartFactory.createPieChart(
                charTitle,
                new DefaultPieDataset(),
                false, true, false);
        setupChart();
        this.chartImage = new PImage(chart.createBufferedImage(w, h));
        this.field = field;
    }

    /**
     * Sets chart.
     */
    @Override
    protected void setupChart() {
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.getPlot().setBackgroundPaint(new Color(View.COLORS.ACCENT_LIGHT));
        ((PiePlot) this.chart.getPlot()).setLabelGenerator(getLabelGenerator());
    }

    /**
     * Update data.
     *
     * @param surveyList the survey list
     */
    @Override
    public void updateData(List<Survey> surveyList) {
        ((PiePlot) this.chart.getPlot()).setDataset((PieDataset) createDataset(surveyList));
        this.chartImage = new PImage(this.chart.createBufferedImage(w, h));
    }

    /**
     * Gets label generator.
     *
     * @return the label generator
     */
    private PieSectionLabelGenerator getLabelGenerator() {
        return new StandardPieSectionLabelGenerator("{0} ({2})");
    }

    /**
     * Create dataset dataset.
     *
     * @param surveyList the survey list
     * @return the dataset
     */
    @Override
    protected Dataset createDataset(List<Survey> surveyList) {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        int[] results = getCombinedData(surveyList, field);

        dataset.setValue("Si", results[0]);
        dataset.setValue("No", results[1]);

        return dataset;
    }

    /**
     * Get combined data int [ ].
     *
     * @param surveyList the survey list
     * @param field      the field
     * @return the int [ ]
     */
    private int[] getCombinedData(List<Survey> surveyList, String field) {
        int[] combined = new int[2];
        for (Survey survey : surveyList) {
            if (survey.getYesNoAnswer(field)) {
                combined[0]++;
            } else {
                combined[1]++;
            }
        }
        return combined;
    }
}
