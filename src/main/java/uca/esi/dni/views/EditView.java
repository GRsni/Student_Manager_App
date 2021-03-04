package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;

public class EditView extends View {

    public EditView(PApplet parent) {
        super(parent);
        onCreate();
    }

    @Override
    protected void onCreate() {
        background = parent.loadImage("data/background/edit_back.png");
        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        createElements();

    }

    @Override
    protected void createElements() {
        PFont smallFont = parent.loadFont("data/fonts/Calibri-14.vlw");
        PFont bigFont = parent.loadFont("data/fonts/Calibri-30.vlw");

        Button enterStudent = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 4.5f, WIDTH_UNIT_SIZE * 4,
                HEIGHT_UNIT_SIZE * 2, 3, "Introducir estudiante", true);
        enterStudent.setIcon(parent.loadImage("data/icons/edit-list.png"));
        enterStudent.setFont(bigFont);
        elements.add(enterStudent);

        Button selectFile = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 9.5f, WIDTH_UNIT_SIZE * 4,
                HEIGHT_UNIT_SIZE*2, 3, "Seleccionar archivo", true);
        selectFile.setIcon(parent.loadImage("data/icons/txt-file-symbol.png"));
        selectFile.setFont(bigFont);
        elements.add(selectFile);

        Button back = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 13, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Volver atrás", true);
        back.setIcon(parent.loadImage("data/icons/back-arrow.png"));
        back.setFont(smallFont);
        elements.add(back);

        Button addToList = new Button(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Añadir a lista", true);
        addToList.setIcon(parent.loadImage("data/icons/add-to-list.png"));
        addToList.setFont(smallFont);
        elements.add(addToList);

        Button deleteFromList = new Button(parent, WIDTH_UNIT_SIZE * 10, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Eliminar de lista", true);
        deleteFromList.setIcon(parent.loadImage("data/icons/delete-item.png"));
        deleteFromList.setFont(smallFont);
        elements.add(deleteFromList);

        Button emptyList = new Button(parent, WIDTH_UNIT_SIZE * 13, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE*2,
                HEIGHT_UNIT_SIZE, 3, "Vaciar lista", true);
        emptyList.setIcon(parent.loadImage("data/icons/empty-list.png"));
        emptyList.setFont(smallFont);
        elements.add(emptyList);


        TextField manuallyTF = new TextField(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 1.5f, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE, "Manualmente:");
        manuallyTF.setFont(bigFont);
        manuallyTF.setFontSize(30);
        elements.add(manuallyTF);

        TextField IDTF = new TextField(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 3, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "ID");
        IDTF.setBackgroundColor(COLORS.WHITE);
        IDTF.setFont(smallFont);
        elements.add(IDTF);

        TextField currentStudents = new TextField(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 14, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "Alumnos en lista actual:");
        currentStudents.setFont(smallFont);
        elements.add(currentStudents);

        TextField DBItems = new TextField(parent, WIDTH_UNIT_SIZE * 12, HEIGHT_UNIT_SIZE * 14, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "Alumnos en BD: ");
        DBItems.setFont(smallFont);
        elements.add(DBItems);


        ItemList auxStudentsItemList = new ItemList(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 4, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE * 10, "Lista previa");
        auxStudentsItemList.setFont(smallFont);
        auxStudentsItemList.setTitleFont(bigFont);
        elements.add(auxStudentsItemList);

        ItemList DBStudentsItemList = new ItemList(parent, WIDTH_UNIT_SIZE * 12, HEIGHT_UNIT_SIZE * 4, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE * 10, "Alumnos en BD");
        DBStudentsItemList.setFont(smallFont);
        DBStudentsItemList.setTitleFont(bigFont);
        elements.add(DBStudentsItemList);

    }

    @Override
    public void update() {

    }
}
