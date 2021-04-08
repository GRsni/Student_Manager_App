package uca.esi.dni.ui.graphs;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Rectangle;

import java.util.List;

public abstract class Graph extends BaseElement {
    protected JFreeChart chart;
    protected PImage chartImage;

    protected Graph(PApplet parent, Rectangle rectangle) {
        super(parent, rectangle);
    }

    public abstract void updateData(List<Survey> surveyList);

    protected CategoryDataset createEmptyCategoryDataSet() {
        return new DefaultCategoryDataset();
    }

    protected PieDataset createEmptyPieDataSet() {
        return new DefaultPieDataset();
    }

    @Override
    public void display() {
        parent.push();
        parent.image(chartImage, pos.x, pos.y);
        parent.pop();
    }
}
