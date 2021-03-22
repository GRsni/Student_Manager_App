package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.ui.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

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

        Button enterStudent = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 4.5f, WIDTH_UNIT_SIZE * 27 / 10,
                HEIGHT_UNIT_SIZE * 2, 3, "Introducir estudiante", true);
        enterStudent.setIcon(parent.loadImage("data/icons/edit-list.png"));
        enterStudent.setFont(bigFont);
        elements.put("enterStudentB", enterStudent);

        Button removeStudent = new Button(parent, WIDTH_UNIT_SIZE * 4, HEIGHT_UNIT_SIZE * 4.5f, WIDTH_UNIT_SIZE,
                HEIGHT_UNIT_SIZE * 2, 3, "Borrar estudiante", true);
        removeStudent.setFont(smallFont);
        elements.put("removeStudentAuxB", removeStudent);

        Button selectFile = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 10.5f, WIDTH_UNIT_SIZE * 4,
                HEIGHT_UNIT_SIZE * 2, 3, "Seleccionar archivo", true);
        selectFile.setIcon(parent.loadImage("data/icons/txt-file-symbol.png"));
        selectFile.setFont(bigFont);
        elements.put("selectFileB", selectFile);

        Button back = new Button(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 13, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Volver atrás", true);
        back.setIcon(parent.loadImage("data/icons/back-arrow.png"));
        back.setFont(smallFont);
        elements.put("backB", back);

        Button addToList = new Button(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Añadir a base de datos", true);
        addToList.setIcon(parent.loadImage("data/icons/add-to-list.png"));
        addToList.setFont(smallFont);
        elements.put("addToListB", addToList);

        Button deleteFromList = new Button(parent, WIDTH_UNIT_SIZE * 10, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Eliminar de base de datos", true);
        deleteFromList.setIcon(parent.loadImage("data/icons/delete-item.png"));
        deleteFromList.setFont(smallFont);
        elements.put("deleteFromListB", deleteFromList);

        Button emptyList = new Button(parent, WIDTH_UNIT_SIZE * 13, HEIGHT_UNIT_SIZE * 2, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, 3, "Vaciar base de datos", true);
        emptyList.setIcon(parent.loadImage("data/icons/empty-list.png"));
        emptyList.setFont(smallFont);
        elements.put("emptyListB", emptyList);


        TextField manuallyTF = new TextField(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 1.5f, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE, "Manualmente:", "");
        manuallyTF.setFont(bigFont);
        manuallyTF.setFontSize(30);
        elements.put("manuallyTF", manuallyTF);

        TextField IDTF = new TextField(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 3, WIDTH_UNIT_SIZE * 3 / 2,
                HEIGHT_UNIT_SIZE, "", "ID");
        IDTF.setBackgroundColor(COLORS.WHITE);
        IDTF.setFont(smallFont);
        IDTF.setClickable(true);
        elements.put("idTF", IDTF);

        TextField emailTF = new TextField(parent, WIDTH_UNIT_SIZE * 3f, HEIGHT_UNIT_SIZE * 3, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "", "email");
        emailTF.setBackgroundColor(COLORS.WHITE);
        emailTF.setFont(smallFont);
        emailTF.setClickable(true);
        elements.put("emailTF", emailTF);

        TextField auxStudentsCounter = new TextField(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 14, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "Alumnos en lista actual: 0", "");
        auxStudentsCounter.setFont(smallFont);
        elements.put("auxStudentsTF", auxStudentsCounter);

        TextField dbStudentsCounter = new TextField(parent, WIDTH_UNIT_SIZE * 12, HEIGHT_UNIT_SIZE * 14, WIDTH_UNIT_SIZE * 2,
                HEIGHT_UNIT_SIZE, "Alumnos en BD: 0", "");
        dbStudentsCounter.setFont(smallFont);
        elements.put("dbStudentsTF", dbStudentsCounter);

        TextField inputFile = new TextField(parent, WIDTH_UNIT_SIZE, HEIGHT_UNIT_SIZE * 9, WIDTH_UNIT_SIZE * 4,
                HEIGHT_UNIT_SIZE, "", "Archivo de texto");
        inputFile.setBackgroundColor(COLORS.WHITE);
        inputFile.setFont(smallFont);
        elements.put("inputFileTF", inputFile);


        ItemList auxStudentsItemList = new ItemList(parent, WIDTH_UNIT_SIZE * 7, HEIGHT_UNIT_SIZE * 4, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE * 10, "Lista previa");
        auxStudentsItemList.setFont(smallFont);
        auxStudentsItemList.setTitleFont(bigFont);
        elements.put("auxStudentsIL", auxStudentsItemList);

        ItemList DBStudentsItemList = new ItemList(parent, WIDTH_UNIT_SIZE * 12, HEIGHT_UNIT_SIZE * 4, WIDTH_UNIT_SIZE * 3,
                HEIGHT_UNIT_SIZE * 10, "Alumnos en BD");
        DBStudentsItemList.setFont(smallFont);
        DBStudentsItemList.setTitleFont(bigFont);
        elements.put("dbStudentIL", DBStudentsItemList);

        PImage modal = parent.loadImage("data/background/modal.png");
        ModalCard modalCard = new ModalCard(parent, 0, 0, WIDTH_UNIT_SIZE * 16, HEIGHT_UNIT_SIZE * 16, modal);
        modalCard.setVisible(false);
        elementsOverModal.put("modalCard", modalCard);

        Button confirmEmpty = new Button(parent, WIDTH_UNIT_SIZE * 4, HEIGHT_UNIT_SIZE * 4, WIDTH_UNIT_SIZE * 8,
                HEIGHT_UNIT_SIZE * 6, 3, "Confirmar vaciar la base de datos", false);
        confirmEmpty.setIcon(parent.loadImage("data/icons/checked.png"));
        confirmEmpty.setFont(bigFont);
        confirmEmpty.setVisible(false);
        elementsOverModal.put("confirmEmptyB", confirmEmpty);

    }

    @Override
    public void update(Set<Student> dbList, Set<Student> auxList, File inputFile, String dbReference, ArrayList<Warning> warnings) {

        TextField inputFileTF = (TextField) elements.get("inputFileTF");
        inputFileTF.setContent(inputFile.getName());

        TextField auxCounter = (TextField) elements.get("auxStudentsTF");
        auxCounter.modifyCounter(auxList.size());

        TextField dbCounter = (TextField) elements.get("dbStudentsTF");
        dbCounter.modifyCounter(dbList.size());

        ItemList auxItemList = (ItemList) elements.get("auxStudentsIL");
        Set<String> auxStrings = UtilParser.studentSetToStringSet(auxList);
        auxItemList.getContentList().clear();
        auxItemList.addList(auxStrings);

        ItemList dbItemList = (ItemList) elements.get("dbStudentIL");
        Set<String> dbStrings = UtilParser.studentSetToStringSet(dbList);
        dbItemList.getContentList().clear();
        dbItemList.addList(dbStrings);


    }
}
