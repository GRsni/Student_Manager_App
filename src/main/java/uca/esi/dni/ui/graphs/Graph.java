package uca.esi.dni.ui.graphs;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;

import java.util.List;

/**
 * The type Graph.
 */
public abstract class Graph extends BaseElement {
    /**
     * The Chart.
     */
    protected JFreeChart chart;
    /**
     * The Chart image.
     */
    protected PImage chartImage;

    /**
     * Instantiates a new Graph.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     */
    protected Graph(PApplet parent, Rectangle rectangle) {
        super(parent, rectangle);
    }

    /**
     * Sets chart.
     */
    protected abstract void setupChart();

    /**
     * Update data.
     *
     * @param surveyList the survey list
     */
    public abstract void updateData(List<Survey> surveyList);

    /**
     * Create dataset dataset.
     *
     * @param surveyList the survey list
     * @return the dataset
     */
    protected abstract Dataset createDataset(List<Survey> surveyList);

    /**
     * Display.
     */
    @Override
    public void display() {
        parent.push();
        parent.image(chartImage, pos.x, pos.y);
        parent.pop();
    }
}
