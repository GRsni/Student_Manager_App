package uca.esi.dni.ui.graphs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.types.Survey;
import uca.esi.dni.views.View;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Likert bar graph.
 */
public class LikertBarGraph extends Graph {

    /**
     * The enum Likert legend.
     */
    private enum LIKERT_LEGEND {
        /**
         * Very bad likert legend.
         */
        VERY_BAD(1),
        /**
         * Bad likert legend.
         */
        BAD(2),
        /**
         * Average likert legend.
         */
        AVERAGE(3),
        /**
         * Good likert legend.
         */
        GOOD(4),
        /**
         * Very good likert legend.
         */
        VERY_GOOD(5);

        /**
         * To string string.
         *
         * @return the string
         */
        @Override
        public String toString() {
            switch (this) {
                case VERY_GOOD:
                    return "Muy bueno";
                case GOOD:
                    return "Bueno";
                case AVERAGE:
                    return "Normal";
                case BAD:
                    return "Malo";
                case VERY_BAD:
                    return "Muy malo";
                default:
                    return "";
            }
        }

        /**
         * The Value.
         */
        private final int value;
        /**
         * The constant map.
         */
        private static final Map<Object, Object> map = new HashMap<>();

        /**
         * Instantiates a new Likert legend.
         *
         * @param value the value
         */
        LIKERT_LEGEND(int value) {
            this.value = value;
        }

        static {
            for (LIKERT_LEGEND pageType : LIKERT_LEGEND.values()) {
                map.put(pageType.value, pageType);
            }
        }

        /**
         * Value of likert legend.
         *
         * @param pageType the page type
         * @return the likert legend
         */
        public static LIKERT_LEGEND valueOf(int pageType) {
            return (LIKERT_LEGEND) map.get(pageType);
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Instantiates a new Likert bar graph.
     *
     * @param parent     the parent
     * @param rectangle  the rectangle
     * @param chartTitle the chart title
     * @param xAxisLabel the x axis label
     * @param yAxisLabel the y axis label
     */
    public LikertBarGraph(PApplet parent, Rectangle rectangle, String chartTitle, String xAxisLabel, String yAxisLabel) {
        super(parent, rectangle);
        this.chart = ChartFactory.createBarChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                new DefaultCategoryDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        setupChart();
        this.chartImage = new PImage(chart.createBufferedImage(w, h));
    }

    /**
     * Sets chart.
     */
    @Override
    protected void setupChart() {
        chart.setBackgroundPaint(new Color(255, 255, 255, 0));
        chart.getPlot().setBackgroundPaint(new Color(View.COLORS.ACCENT_LIGHT));
        chart.getLegend().setBackgroundPaint(new Color(View.COLORS.ACCENT_LIGHT));
        chart.getCategoryPlot().setRenderer(getBarRenderer());
        chart.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelLines(2);
        chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    }

    /**
     * Gets bar renderer.
     *
     * @return the bar renderer
     */
    private BarRenderer getBarRenderer() {
        BarRenderer bar = new BarRenderer();
        bar.setSeriesPaint(0, new Color(View.COLORS.SECONDARY_DARK)); //first bar
        bar.setSeriesPaint(1, new Color(View.COLORS.SECONDARY)); // second bar
        bar.setSeriesPaint(2, new Color(View.COLORS.ACCENT)); // third bar
        bar.setSeriesPaint(3, new Color(View.COLORS.PRIMARY)); // fourth bar
        bar.setSeriesPaint(4, new Color(View.COLORS.PRIMARY_DARK)); // fifth bar
        return bar;
    }

    /**
     * Update data.
     *
     * @param surveyList the survey list
     */
    @Override
    public void updateData(List<Survey> surveyList) {
        chart.getCategoryPlot().setDataset((CategoryDataset) createDataset(surveyList));
        chartImage = new PImage(this.chart.createBufferedImage(w, h));
    }

    /**
     * Create dataset dataset.
     *
     * @param surveyList the survey list
     * @return the dataset
     */
    @Override
    protected Dataset createDataset(List<Survey> surveyList) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> combined = getCombinedData(surveyList);

        for (Map.Entry<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> likertFieldsMapEntry : combined.entrySet()) {
            for (Map.Entry<LIKERT_LEGEND, Integer> legendIntegerEntry : likertFieldsMapEntry.getValue().entrySet()) {
                dataset.addValue(legendIntegerEntry.getValue(), legendIntegerEntry.getKey().toString(), likertFieldsMapEntry.getKey().toString());
            }
        }
        return dataset;
    }

    /**
     * Gets combined data.
     *
     * @param surveyList the survey list
     * @return the combined data
     */
    private Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> getCombinedData(List<Survey> surveyList) {
        Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> combined = getEmptyMap();

        for (Survey survey : surveyList) {
            populateLikertGraphDataMap(combined, survey);
        }
        return combined;
    }

    /**
     * Gets empty map.
     *
     * @return the empty map
     */
    private Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> getEmptyMap() {
        Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> map = new EnumMap<>(Survey.LIKERT_FIELDS.class);
        for (Survey.LIKERT_FIELDS field : Survey.LIKERT_FIELDS.values()) {
            Map<LIKERT_LEGEND, Integer> legendIntegerMap = new EnumMap<>(LIKERT_LEGEND.class);
            for (LIKERT_LEGEND legend : LIKERT_LEGEND.values()) {
                legendIntegerMap.put(legend, 0);
            }
            map.put(field, legendIntegerMap);
        }
        return map;
    }

    /**
     * Populate likert graph data map.
     *
     * @param combined the combined
     * @param survey   the survey
     */
    private void populateLikertGraphDataMap(Map<Survey.LIKERT_FIELDS, Map<LIKERT_LEGEND, Integer>> combined, Survey survey) {
        for (Survey.LIKERT_FIELDS field : Survey.LIKERT_FIELDS.values()) {
            Map<LIKERT_LEGEND, Integer> legendIntegerEntry = combined.get(field);
            int valueInSurveyLikertField = survey.getLikertValue(field);
            LIKERT_LEGEND legend = LIKERT_LEGEND.valueOf(valueInSurveyLikertField);
            legendIntegerEntry.put(legend, legendIntegerEntry.get(legend) + 1);
        }
    }
}
