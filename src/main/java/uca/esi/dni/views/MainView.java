package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;

import static processing.event.MouseEvent.CLICK;


public class MainView extends View {

    private Button editButton;
    private Button generateFilesButton;
    private Button generateStatsButton;
    private ItemList DBStudentList;
    private PImage background;
    private TextField DBItems;

    public MainView(PApplet parent) {
        super(parent);
        onCreate();

    }

    private void onCreate() {
        PFont smallFont = parent.loadFont("data/fonts/Calibri-14.vlw");
        PFont bigFont = parent.loadFont("data/fonts/Calibri-30.vlw");

        background = parent.loadImage("data/background/main_back.png");

        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        editButton = new Button(parent, widthUnitSize, heightUnitSize * 2f, widthUnitSize * 6,
                heightUnitSize * 3, 6, "Editar Lista", true);
        editButton.setIcon(parent.loadImage("data/icons/edit-list.png"));
        editButton.setColor(COLORS.PRIMARY);
        editButton.setClickColor(COLORS.ACCENT_DARK);
        editButton.setTextColor(COLORS.WHITE);
        editButton.setFont(bigFont);
        editButton.setFontSize(10);

        generateFilesButton = new Button(parent, widthUnitSize, heightUnitSize * 6.5f, widthUnitSize * 6,
                heightUnitSize * 3, 6, "Generar archivos Excel", true);
        generateFilesButton.setIcon(parent.loadImage("data/icons/excel.png"));
        generateFilesButton.setColor(COLORS.PRIMARY);
        generateFilesButton.setClickColor(COLORS.ACCENT_DARK);
        generateFilesButton.setTextColor(COLORS.WHITE);
        generateFilesButton.setFont(bigFont);
        generateFilesButton.setFontSize(30);
        //generateFilesButton.setContentBackgroundColor(COLORS.ACCENT);

        generateStatsButton = new Button(parent, widthUnitSize, heightUnitSize * 11f, widthUnitSize * 6,
                heightUnitSize * 3, 6, "Generar estadísticas", true);
        generateStatsButton.setIcon(parent.loadImage("data/icons/statistics.png"));
        generateStatsButton.setColor(COLORS.PRIMARY);
        generateStatsButton.setClickColor(COLORS.ACCENT_DARK);
        generateStatsButton.setTextColor(COLORS.WHITE);
        generateStatsButton.setFont(bigFont);
        generateStatsButton.setFontSize(30);

        DBItems = new TextField(parent, widthUnitSize * 13, heightUnitSize * 14, widthUnitSize * 2,
                heightUnitSize, "Alumnos en BD: ");
        DBItems.setFont(smallFont);
        DBItems.setTextColor(COLORS.BLACK);
        DBItems.setFontSize(10);


        DBStudentList = new ItemList(parent, widthUnitSize * 12, heightUnitSize * 2, widthUnitSize * 3,
                heightUnitSize * 12, "Alumnos en base de datos:");
        DBStudentList.setTextColor(COLORS.BLACK);
        DBStudentList.setBackgroundColor(COLORS.WHITE);
        DBStudentList.setTitleBackgroundColor(COLORS.ACCENT_DARK);
        DBStudentList.setFont(smallFont);
        DBStudentList.setFontSize(10);
        DBStudentList.setTitleFontSize(20);

        //testing item list display
        for (int i = 0; i < 13; i++) {
            DBStudentList.addItem(String.valueOf(parent.random(1000)));
        }
    }


    @Override
    public void show() {
        parent.image(background, 0, 0);
        editButton.show();
        generateFilesButton.show();
        generateStatsButton.show();
        DBItems.display();
        DBStudentList.display();
    }

    public void handleInput(processing.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        switch (e.getAction()){

        }

    }

    @Override
    public void update() {

    }
}
