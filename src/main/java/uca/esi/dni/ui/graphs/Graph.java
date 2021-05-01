package uca.esi.dni.ui.graphs;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;

import java.util.List;

public abstract class Graph extends BaseElement {
    protected JFreeChart chart;
    protected PImage chartImage;

    protected Graph(PApplet parent, Rectangle rectangle) {
        super(parent, rectangle);
    }

    protected abstract void setupChart();

    public abstract void updateData(List<Survey> surveyList);

    protected abstract Dataset createDataset(List<Survey> surveyList);

    @Override
    public void display() {
        parent.push();
        parent.image(chartImage, pos.x, pos.y);
        parent.pop();
    }
}
