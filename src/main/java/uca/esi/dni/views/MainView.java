package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;


public class MainView extends View {

    private Button editButton;
    private Button generateFilesButton;
    private Button generateStatsButton;
    private ItemList DBStudentList;
    private PImage background;
    private boolean backgroundLoaded = false;
    private TextField DBItems;


    public MainView(AppModel model, PApplet parent) {
        super(parent);
        onCreate();

    }

    private void onCreate() {
        PFont smallFont = parent.loadFont("data/fonts/Calibri-14.vlw");
        PFont bigFont = parent.loadFont("data/fonts/Calibri-30.vlw");

        background = parent.loadImage("data/background/main_back.png");

        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        editButton = new Button(parent, parent.width / 16, parent.height / 16 * 2, parent.width / 3,
                parent.height / 16 * 3, 6, "Editar Lista", true);
        editButton.setIcon(parent.loadImage("data/icons/edit-list.png"));
        editButton.setColor(COLORS.PRIMARY);
        editButton.setTextColor(COLORS.WHITE);
        editButton.setFont(bigFont);
        editButton.setFontSize(10);

        generateFilesButton = new Button(parent, parent.width / 16, parent.height / 16 * 6, parent.width / 3,
                parent.height / 16 * 3, 6, "Generar archivos Excel", true);
        generateFilesButton.setIcon(parent.loadImage("data/icons/excel.png"));
        generateFilesButton.setColor(COLORS.PRIMARY);
        generateFilesButton.setTextColor(COLORS.WHITE);
        generateFilesButton.setFont(bigFont);
        generateFilesButton.setFontSize(30);
        //generateFilesButton.setContentBackgroundColor(COLORS.ACCENT);

        generateStatsButton = new Button(parent, parent.width / 16, parent.height / 16 * 10, parent.width / 3,
                parent.height / 16 * 3, 6, "Generar estadísticas", true);
        generateStatsButton.setIcon(parent.loadImage("data/icons/statistics.png"));
        generateStatsButton.setColor(COLORS.PRIMARY);
        generateStatsButton.setTextColor(COLORS.WHITE);
        generateStatsButton.setFont(bigFont);
        generateStatsButton.setFontSize(30);

        DBItems = new TextField(parent, parent.width / 16 * 13, parent.height / 16 * 14,
                parent.width / 8, parent.height / 16, "Alumnos en BD: ");
        DBItems.setFont(smallFont);
        DBItems.setTextColor(COLORS.BLACK);
        DBItems.setFontSize(10);

        DBStudentList = new ItemList(parent, parent.width / 16 * 12, parent.height / 16 * 2, parent.width / 16 * 3,
                parent.height / 16 * 12, "Alumnos en base de datos:");
        DBStudentList.setTextColor(COLORS.BLACK);
        DBStudentList.setBackgroundColor(COLORS.WHITE);
        DBStudentList.setTitleBackgroundColor(COLORS.ACCENT);
        DBStudentList.setFont(smallFont);
        DBStudentList.setFontSize(10);
        DBStudentList.setTitleFontSize(20);
        for (int i = 0; i < 4; i++) {
            DBStudentList.addItem(String.valueOf(parent.millis()));
        }
    }

    public void show() {
        parent.image(background, 0, 0);
        editButton.show();
        generateFilesButton.show();
        generateStatsButton.show();
        DBItems.display();
        DBStudentList.display();
    }

    @Override
    public void update() {

    }
}
