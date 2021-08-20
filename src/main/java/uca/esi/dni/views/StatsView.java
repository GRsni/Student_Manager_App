package uca.esi.dni.views;

import processing.core.PApplet;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.graphs.LikertBarGraph;
import uca.esi.dni.ui.graphs.PieGraph;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * The type Stats view.
 */
public class StatsView extends View {

    /**
     * Instantiates a new Stats view.
     *
     * @param parent the parent
     */
    public StatsView(PApplet parent) {
        super(parent);
        onCreate();
    }

    /**
     * On create.
     */
    @Override
    protected void onCreate() {
        background = parent.loadImage("data/background/stats_back.png");

        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        createElements();
    }

    /**
     * Create elements.
     */
    @Override
    protected void createElements() {
        LikertBarGraph likertBarGraph = new LikertBarGraph(parent,
                new BaseElement.Rectangle(widthUnitSize * 0.5f, heightUnitSize * 1.5f, widthUnitSize * 10, heightUnitSize * 10),
                "Valoraciones", "Categorías", "Votos");
        elements.put("likertBG", likertBarGraph);

        PieGraph likePieGraph = new PieGraph(parent,
                new BaseElement.Rectangle(widthUnitSize * 12f, heightUnitSize * 1.5f, widthUnitSize * 3, heightUnitSize * 7 / 2),
                "Gusta la aplicación.", "like");
        elements.put("likePG", likePieGraph);

        PieGraph learningPieGraph = new PieGraph(parent,
                new BaseElement.Rectangle(widthUnitSize * 12f, heightUnitSize * 6.25f, widthUnitSize * 3, heightUnitSize * 7 / 2),
                "Mejora aprendizaje.", "learning");
        elements.put("learningPG", learningPieGraph);

        PieGraph outsidePieGraph = new PieGraph(parent,
                new BaseElement.Rectangle(widthUnitSize * 12f, heightUnitSize * 11f, widthUnitSize * 3, heightUnitSize * 7 / 2),
                "Usará otra vez.", "outside");
        elements.put("outsidePG", outsidePieGraph);

        Button back = new Button(parent,
                new BaseElement.Rectangle(widthUnitSize * 2f, heightUnitSize * 13.5f, widthUnitSize * 3, heightUnitSize),
                3, "Volver atrás", true);
        back.setIcon(parent.loadImage("data/icons/back-arrow.png"));
        back.setFont(fontSmall);
        elements.put("backB", back);

        Button screenCap = new Button(parent,
                new BaseElement.Rectangle(widthUnitSize * 6f, heightUnitSize * 13.5f, widthUnitSize * 3, heightUnitSize),
                3, "Captura de pantalla", true);
        screenCap.setIcon(parent.loadImage("data/icons/photo-camera.png"));
        screenCap.setFont(fontSmall);
        elements.put("screenCapB", screenCap);
    }

    /**
     * Update.
     *
     * @param dbList    the db list
     * @param modList   the mod list
     * @param inputFile the input file
     * @param surveys   the surveys
     */
    @Override
    public void update(Set<Student> dbList, Set<Student> modList, File inputFile, List<Survey> surveys) {
        LikertBarGraph likertBarGraph = (LikertBarGraph) elements.get("likertBG");
        likertBarGraph.updateData(surveys);

        PieGraph likePieGraph = (PieGraph) elements.get("likePG");
        likePieGraph.updateData(surveys);

        PieGraph learningPieGraph = (PieGraph) elements.get("learningPG");
        learningPieGraph.updateData(surveys);

        PieGraph outsidePirGraph = (PieGraph) elements.get("outsidePG");
        outsidePirGraph.updateData(surveys);
    }
}
