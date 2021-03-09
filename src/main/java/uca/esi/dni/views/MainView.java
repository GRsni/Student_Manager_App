package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;


public class MainView extends View {

    public MainView(PApplet parent) {
        super(parent);
        onCreate();

    }

    protected void onCreate() {
        background = parent.loadImage("data/background/main_back.png");

        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        createElements();
    }

    @Override
    protected void createElements() {
        PFont smallFont = parent.loadFont("data/fonts/Calibri-14.vlw");
        PFont bigFont = parent.loadFont("data/fonts/Calibri-30.vlw");

        Button editButton = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 6,
                HEIGHT_UNIT_SIZE * 3, 6, "Editar Lista", true);
        editButton.setIcon(parent.loadImage("data/icons/edit-list.png"));
        editButton.setFont(bigFont);
        editButton.setFontSize(10);
        elements.put("editB", editButton);

        Button generateFilesButton = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 6.5f, WIDTH_UNIT_SIZE * 6,
                HEIGHT_UNIT_SIZE * 3, 6, "Generar archivos Excel", true);
        generateFilesButton.setIcon(parent.loadImage("data/icons/excel.png"));
        generateFilesButton.setFont(bigFont);
        generateFilesButton.setFontSize(30);
        elements.put("generateFilesB", generateFilesButton);

        Button generateStatsButton = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 11f, WIDTH_UNIT_SIZE * 6,
                HEIGHT_UNIT_SIZE * 3, 6, "Generar estadísticas", true);
        generateStatsButton.setIcon(parent.loadImage("data/icons/statistics.png"));
        generateStatsButton.setFont(bigFont);
        generateStatsButton.setFontSize(30);
        elements.put("generateStatsB", generateStatsButton);

        TextField dbStudentsCounter = new TextField(parent, WIDTH_UNIT_SIZE * 13, HEIGHT_UNIT_SIZE * 14, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "Alumnos en BD: 0", "");
        dbStudentsCounter.setFont(smallFont);
        dbStudentsCounter.setFontSize(10);
        elements.put("dbStudentsTF", dbStudentsCounter);


        ItemList DBStudentsItemList = new ItemList(parent, WIDTH_UNIT_SIZE * 12, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE * 12, "Alumnos en BD");
        DBStudentsItemList.setFont(smallFont);
        DBStudentsItemList.setFontSize(10);
        DBStudentsItemList.setTitleFontSize(20);
        DBStudentsItemList.setTitleFont(bigFont);
        elements.put("dbStudentsIL", DBStudentsItemList);
    }

    @Override
    public void update(Set<Student> dbList, Set<Student> modList, File inputFile, String dbReference, ArrayList<Warning> warnings) {
        TextField dbStudentsCounter = (TextField) elements.get("dbStudentsTF");
        dbStudentsCounter.modifyCounter(dbList.size());

        ItemList studentsIL = (ItemList) elements.get("dbStudentsIL");
        Set<String> stringSet = UtilParser.studentSetToStringSet(dbList);
        studentsIL.getContentList().clear();
        studentsIL.addList(stringSet);

    }
}
