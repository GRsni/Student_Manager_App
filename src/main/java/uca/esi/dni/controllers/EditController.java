package uca.esi.dni.controllers;

import processing.data.Table;
import processing.data.TableRow;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.EmailHandler;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;
import uca.esi.dni.views.View;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static processing.core.PConstants.ENTER;
import static processing.core.PConstants.*;
import static processing.event.KeyEvent.TYPE;
import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.main;

public class EditController extends Controller {

    public EditController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);
    }

    @Override
    public void controllerLogic() {
        view.update(model.getDBStudents(), model.getTemporaryStudents(), model.getInputFile(), model.getDBReference(), model.getWarnings());
    }

    @Override
    public void handleMouseEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        TextField idTF = (TextField) view.getUIElement("idTF");
        TextField emailTF = (TextField) view.getUIElement("emailTF");

        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement("enterStudentB").inside(x, y)) {

                    if (!idTF.getContent().isEmpty() && !emailTF.getContent().isEmpty()) {
                        if (EmailHandler.isValidEmailAddress(emailTF.getContent())) {
                            model.addTemporaryStudent(new Student(idTF.getContent(), emailTF.getContent()));
                            idTF.setContent("");
                            emailTF.setContent("");
                            getFocusedTextField().setFocused(false);
                        } else {
                            System.err.println("Error while validating email address");
                        }
                    } else {
                        System.err.println("Empty fields");
                    }
                } else if (view.getUIElement("selectFileB").inside(x, y)) {
                    selectInputFile();
                } else if (view.getUIElement("backB").inside(x, y)) {
                    //change view to mainView
                    changeState(main);
                } else if (view.getUIElement("addToListB").inside(x, y)) {
                    //TODO: Add aux list to DB list
                } else if (view.getUIElement("deleteFromListB").inside(x, y)) {
                    //TODO: Remove aux list from DB list
                } else if (view.getUIElement("emptyListB").inside(x, y)) {
                    //TODO: Empty DB list
                } else if (idTF.inside(x, y)) {
                    idTF.setFocused(true);
                    emailTF.setFocused(false);
                } else if (emailTF.inside(x, y)) {
                    idTF.setFocused(false);
                    emailTF.setFocused(true);
                }

                for (String key : view.getElementKeys()) {
                    BaseElement element = view.getUIElement(key);
                    if (element.isClicked()) {
                        element.isClicked(false);
                    }
                }
                break;
            case MouseEvent.MOVE:
                for (String key : view.getElementKeys()) {
                    BaseElement element = view.getUIElement(key);
                    element.isHover(element.inside(x, y));

                }
                break;
            case PRESS:
                for (String key : view.getElementKeys()) {
                    BaseElement element = view.getUIElement(key);
                    if (element.inside(x, y)) {
                        element.isClicked(true);
                    }
                }
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                ItemList dbList = (ItemList) view.getUIElement("dbStudentIL");
                ItemList auxList = (ItemList) view.getUIElement("auxStudentsIL");
                if (dbList.inside(x, y)) {
                    dbList.handleInput(e);
                } else if (auxList.inside(x, y)) {
                    auxList.handleInput(e);
                }
        }
        controllerLogic();
    }

    @Override
    public void handleKeyEvent(KeyEvent e) {
        if (e.getAction() == TYPE) {

            final char k = e.getKey();
            TextField tf = getFocusedTextField();

            if (k == BACKSPACE) {
                tf.removeCharacter();
            } else if (k == ENTER || k == RETURN) {
                tf.setFocused(false);
            } else if (k == DELETE) {
                tf.setContent("");
            } else if (k >= ' ') {
                tf.addCharToContent(k);
            }
        }
    }

    private void selectInputFile() {
        closedContextMenu = false;

        parent.selectInput("Seleccione el archivo de texto:", "selectInputFile");
        while (!closedContextMenu) {
            Thread.onSpinWait(); //We need to wait for the input file context menu to be closed before resuming execution
        }

        if (model.getInputFile().exists()) {
            try {
                Table studentIDTable = parent.loadTable(model.getInputFile().getAbsolutePath(), "header");
                System.out.println(studentIDTable.getRowCount());
                Set<Student> studentList = generateStudentListFromTable(studentIDTable);
/*                for (Student s : studentList) {
                    System.out.println(s.toString());
                }*/
                model.addTemporaryStudentList(studentList);
            } catch (Exception e) {
                System.err.println("Error loading the student list from CSV file");
            }

        }
    }

    private Set<Student> generateStudentListFromTable(Table studentIDTable) {
        Set<Student> students = new HashSet<>();
        for (TableRow row : studentIDTable.rows()) {
            String id = row.getString("DNI");
            String email = row.getString("email");
            if (EmailHandler.isValidEmailAddress(email)) {
                students.add(new Student(id, email));
            }
        }
        return students;
    }

    private TextField getFocusedTextField() {
        TextField idTF = (TextField) view.getUIElement("idTF");
        TextField emailTF = (TextField) view.getUIElement("emailTF");
        if (idTF.isFocused()) {
            return idTF;
        } else {
            return emailTF;
        }
    }


    @Override
    public void onContextMenuClosed(File file) {
        model.setInputFile(file);
        controllerLogic();
    }
}
