package uca.esi.dni.views;

import processing.core.PApplet;
import uca.esi.dni.handlers.Util;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;

import java.io.File;
import java.util.List;
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

        Button editButton = new Button(parent,
                new BaseElement.Rectangle(widthUnitSize, heightUnitSize * 2f, widthUnitSize * 6, heightUnitSize * 3),
                6, "Editar lista de alumnos", true);
        editButton.setIcon(parent.loadImage("data/icons/edit-list.png"));
        editButton.setFont(fontBig);
        editButton.setFontSize(10);
        elements.put("editB", editButton);

        Button generateFilesButton = new Button(parent,
                new BaseElement.Rectangle(widthUnitSize, heightUnitSize * 6.5f, widthUnitSize * 6, heightUnitSize * 3),
                6, "Generar archivos Excel", true);
        generateFilesButton.setIcon(parent.loadImage("data/icons/excel.png"));
        generateFilesButton.setFont(fontBig);
        generateFilesButton.setFontSize(30);
        elements.put("generateFilesB", generateFilesButton);

        Button generateStatsButton = new Button(parent,
                new BaseElement.Rectangle(widthUnitSize, heightUnitSize * 11f, widthUnitSize * 6, heightUnitSize * 3),
                6, "Generar estadísticas", true);
        generateStatsButton.setIcon(parent.loadImage("data/icons/statistics.png"));
        generateStatsButton.setFont(fontBig);
        generateStatsButton.setFontSize(30);
        elements.put("generateStatsB", generateStatsButton);

        TextField dbStudentsCounter = new TextField(parent,
                new BaseElement.Rectangle(widthUnitSize * 13f, heightUnitSize * 14f, widthUnitSize * 2, heightUnitSize),
                "Alumnos en BD: 0", "");
        dbStudentsCounter.setFont(fontSmall);
        dbStudentsCounter.setFontSize(10);
        elements.put("dbStudentsTF", dbStudentsCounter);


        ItemList dbStudentsItemList = new ItemList(parent,
                new BaseElement.Rectangle(widthUnitSize * 12f, heightUnitSize * 2f, widthUnitSize * 3, heightUnitSize * 12),
                "Alumnos en BD");
        dbStudentsItemList.setFont(fontSmall);
        dbStudentsItemList.setFontSize(10);
        dbStudentsItemList.setTitleFontSize(20);
        dbStudentsItemList.setTitleFont(fontBig);
        elements.put("dbStudentsIL", dbStudentsItemList);
    }

    @Override
    public void update(Set<Student> dbList, Set<Student> modList, File inputFile, List<Survey> surveys) {
        TextField dbStudentsCounter = (TextField) elements.get("dbStudentsTF");
        dbStudentsCounter.modifyCounter(dbList.size());

        ItemList studentsIL = (ItemList) elements.get("dbStudentsIL");
        Set<String> stringSet = Util.studentSetToStringSet(dbList);
        studentsIL.getContentList().clear();
        studentsIL.addList(stringSet);

    }
}
