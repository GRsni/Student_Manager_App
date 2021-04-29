package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.file.Util;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.*;

import java.io.File;
import java.util.List;
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

        Button enterStudent = new Button(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 4.5f, widthUnitSize * 27 / 10, heightUnitSize * 2),
                3, "Introducir estudiante", true);
        enterStudent.setIcon(parent.loadImage("data/icons/edit-list.png"));
        enterStudent.setFont(fontBig);
        elements.put("enterStudentB", enterStudent);

        Button removeStudent = new Button(parent,
                new Rectangle(widthUnitSize * 4f, heightUnitSize * 4.5f, widthUnitSize, heightUnitSize * 2),
                3, "Borrar estudiante", true);
        removeStudent.setFont(fontSmall);
        elements.put("removeStudentAuxB", removeStudent);

        Button selectFile = new Button(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 10.5f, widthUnitSize * 4, heightUnitSize * 2),
                3, "Seleccionar archivo", true);
        selectFile.setIcon(parent.loadImage("data/icons/txt-file-symbol.png"));
        selectFile.setFont(fontBig);
        elements.put("selectFileB", selectFile);

        Button back = new Button(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 13.5f, widthUnitSize * 2, heightUnitSize),
                3, "Volver atrás", true);
        back.setIcon(parent.loadImage("data/icons/back-arrow.png"));
        back.setFont(fontSmall);
        elements.put("backB", back);

        Button addToList = new Button(parent,
                new Rectangle(widthUnitSize * 7f, heightUnitSize * 2f, widthUnitSize * 2, heightUnitSize),
                3, "Añadir a base de datos", true);
        addToList.setIcon(parent.loadImage("data/icons/add-to-list.png"));
        addToList.setFont(fontSmall);
        elements.put("addToListB", addToList);

        Button deleteFromList = new Button(parent,
                new Rectangle(widthUnitSize * 10f, heightUnitSize * 2f, widthUnitSize * 2, heightUnitSize),
                3, "Eliminar de base de datos", true);
        deleteFromList.setIcon(parent.loadImage("data/icons/delete-item.png"));
        deleteFromList.setFont(fontSmall);
        elements.put("deleteFromListB", deleteFromList);

        Button emptyList = new Button(parent,
                new Rectangle(widthUnitSize * 13f, heightUnitSize * 2f, widthUnitSize * 2, heightUnitSize),
                3, "Vaciar base de datos", true);
        emptyList.setIcon(parent.loadImage("data/icons/empty-list.png"));
        emptyList.setFont(fontSmall);
        elements.put("emptyListB", emptyList);


        TextField manuallyTF = new TextField(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 1.5f, widthUnitSize * 3, heightUnitSize),
                "Manualmente:", "");
        manuallyTF.setFont(fontBig);
        manuallyTF.setFontSize(30);
        elements.put("manuallyTF", manuallyTF);

        TextField idTF = new TextField(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 3f, widthUnitSize * 3 / 2, heightUnitSize),
                "", "ID");
        idTF.setBackgroundColor(COLORS.WHITE);
        idTF.setFont(fontSmall);
        idTF.setClickable(true);
        idTF.setMaxLength(10);
        elements.put("idTF", idTF);

        TextField emailTF = new TextField(parent,
                new Rectangle(widthUnitSize * 3f, heightUnitSize * 3f, widthUnitSize * 2, heightUnitSize),
                "", "email");
        emailTF.setBackgroundColor(COLORS.WHITE);
        emailTF.setFont(fontSmall);
        emailTF.setClickable(true);
        emailTF.setScrollable(true);
        elements.put("emailTF", emailTF);

        TextField auxStudentsCounter = new TextField(parent,
                new Rectangle(widthUnitSize * 7f, heightUnitSize * 14f, widthUnitSize * 3, heightUnitSize),
                "Alumnos en lista temporal: 0", "");
        auxStudentsCounter.setFont(fontSmall);
        elements.put("auxStudentsTF", auxStudentsCounter);

        TextField dbStudentsCounter = new TextField(parent,
                new Rectangle(widthUnitSize * 12f, heightUnitSize * 14f, widthUnitSize * 3, heightUnitSize),
                "Alumnos en BD: 0", "");
        dbStudentsCounter.setFont(fontSmall);
        elements.put("dbStudentsTF", dbStudentsCounter);

        TextField inputFile = new TextField(parent,
                new Rectangle(widthUnitSize, heightUnitSize * 9f, widthUnitSize * 4, heightUnitSize),
                "", "Archivo de texto");
        inputFile.setBackgroundColor(COLORS.WHITE);
        inputFile.setFont(fontSmall);
        inputFile.setCuttable(true);
        elements.put("inputFileTF", inputFile);

        ItemList auxStudentsItemList = new ItemList(parent,
                new Rectangle(widthUnitSize * 7f, heightUnitSize * 4f, widthUnitSize * 3, heightUnitSize * 10),
                "Lista temporal");
        auxStudentsItemList.setFont(fontSmall);
        auxStudentsItemList.setTitleFont(fontBig);
        elements.put("auxStudentsIL", auxStudentsItemList);

        ItemList dbStudentsItemList = new ItemList(parent,
                new Rectangle(widthUnitSize * 12f, heightUnitSize * 4f, widthUnitSize * 3, heightUnitSize * 10),
                "Alumnos en BD");
        dbStudentsItemList.setFont(fontSmall);
        dbStudentsItemList.setTitleFont(fontBig);
        elements.put("dbStudentsIL", dbStudentsItemList);


        PImage modal = parent.loadImage("data/background/modal.png");
        ModalCard modalCard = new ModalCard(parent, new Rectangle(0, 0, widthUnitSize * 16, heightUnitSize * 16), modal);
        modalCard.setVisible(false);
        elementsOverModal.put("modalCard", modalCard);

        Button confirmEmpty = new Button(parent,
                new Rectangle(widthUnitSize * 4f, heightUnitSize * 4f, widthUnitSize * 8, heightUnitSize * 6),
                3, "Confirmar vaciar la base de datos", false);
        confirmEmpty.setIcon(parent.loadImage("data/icons/checked.png"));
        confirmEmpty.setFont(fontBig);
        confirmEmpty.setVisible(false);
        elementsOverModal.put("confirmEmptyB", confirmEmpty);

        Button exitModal = new Button(parent,
                new Rectangle(widthUnitSize * 6f, heightUnitSize * 11f, widthUnitSize * 4, heightUnitSize * 2),
                3, "Cancelar", false);
        exitModal.setFont(fontBig);
        exitModal.setVisible(false);
        exitModal.setCentered(true);
        elementsOverModal.put("exitModalB", exitModal);

    }

    @Override
    public void update(Set<Student> dbList, Set<Student> auxList, File inputFile, List<Survey> surveys) {

        TextField inputFileTF = (TextField) elements.get("inputFileTF");
        inputFileTF.setContent(inputFile.getName());

        TextField auxCounter = (TextField) elements.get("auxStudentsTF");
        auxCounter.modifyCounter(auxList.size());

        TextField dbCounter = (TextField) elements.get("dbStudentsTF");
        dbCounter.modifyCounter(dbList.size());

        ItemList auxItemList = (ItemList) elements.get("auxStudentsIL");
        Set<String> auxStrings = Util.studentSetToStringSet(auxList);
        auxItemList.getContentList().clear();
        auxItemList.addList(auxStrings);

        ItemList dbItemList = (ItemList) elements.get("dbStudentsIL");
        Set<String> dbStrings = Util.studentSetToStringSet(dbList);
        dbItemList.getContentList().clear();
        dbItemList.addList(dbStrings);
    }
}
