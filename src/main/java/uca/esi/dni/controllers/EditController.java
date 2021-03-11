package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.EmailHandler;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

        switch (e.getAction()) {
            case CLICK:
                if (view.isModalActive()) {
                    if (view.getUIModalElement("confirmEmptyB").inside(x, y)) {
                        asyncEmptyDB();
                    }
                    setModalVisibility(false);
                } else {
                    TextField idTF = (TextField) view.getUIElement("idTF");
                    TextField emailTF = (TextField) view.getUIElement("emailTF");
                    if (view.getUIElement("enterStudentB").inside(x, y)) {
                        if (checkManualStudentData(idTF, emailTF)) {
                            addManualDataToAuxList(idTF, emailTF);
                            clearTextFields(idTF, emailTF);
                        }
                    } else if (view.getUIElement("removeStudentAuxB").inside(x, y)) {
                        if (checkManualStudentData(idTF, emailTF)) {
                            removeStudentFromAuxList(idTF.getContent());
                            clearTextFields(idTF, emailTF);
                        }
                    } else if (view.getUIElement("selectFileB").inside(x, y)) {
                        selectInputFile();
                    } else if (view.getUIElement("backB").inside(x, y)) {
                        changeState(main);
                    } else if (view.getUIElement("addToListB").inside(x, y)) {
                        asyncAddStudentsToDB();
                    } else if (view.getUIElement("deleteFromListB").inside(x, y)) {
                        asyncRemoveStudentsFromDB();
                    } else if (view.getUIElement("emptyListB").inside(x, y)) {
                        setModalVisibility(true);
                    } else if (idTF.inside(x, y)) {
                        idTF.setFocused(true);
                        emailTF.setFocused(false);
                    } else if (emailTF.inside(x, y)) {
                        idTF.setFocused(false);
                        emailTF.setFocused(true);
                    } else if (view.getUIElement("dbStudentIL").inside(x, y)) {
                        System.out.println("clicked db IL");
                    } else if (view.getUIElement("auxStudentsIL").inside(x, y)) {
                        System.out.println("clicked aux IL");
                    }
                }

                for (String key : view.getElementKeys()) {
                    BaseElement element = view.getUIElement(key);
                    if (element.isClicked()) {
                        element.isClicked(false);
                    }
                }
                break;
            case MouseEvent.MOVE:
                checkHover(x, y);
                break;
            case PRESS:
                checkPress(x, y);
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                ItemList dbList = (ItemList) view.getUIElement("dbStudentIL");
                ItemList auxList = (ItemList) view.getUIElement("auxStudentsIL");
                if (dbList.inside(x, y)) {
                    dbList.handleScroll(e);
                } else if (auxList.inside(x, y)) {
                    auxList.handleScroll(e);
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

    private void setModalVisibility(boolean visibility) {
        view.setModalActive(visibility);
        for (String key : view.getModalElementKeys()) {
            view.getUIModalElement(key).setVisible(visibility);
        }
    }

    private void addManualDataToAuxList(TextField idTF, TextField emailTF) {
        model.addTemporaryStudent(new Student(idTF.getContent(), emailTF.getContent()));

    }

    private void clearTextFields(TextField idTF, TextField emailTF) {
        idTF.setContent("");
        emailTF.setContent("");
        getFocusedTextField().setFocused(false);
    }


    private boolean checkManualStudentData(TextField idTF, TextField emailTF) {
        if (!idTF.getContent().isEmpty()) {
            if (!emailTF.getContent().isEmpty()) {
                if (EmailHandler.isValidEmailAddress(emailTF.getContent())) {
                    return true;
                } else {
                    System.err.println("[Error validating email address].");
                    return false;
                }
            } else {
                return true;
            }
        } else {
            System.err.println("[Error while reading student data]: Empty ID field");
            return false;
        }
    }

    private void removeStudentFromAuxList(String id) {
        for (Student student : model.getTemporaryStudents()) {
            if (student.getID().equals(id)) {
                model.removeTemporaryStudent(student);
                break;
            }
        }
    }


    private void asyncAddStudentsToDB() {
        Runnable runnable = () -> {
            try {
                Set<Student> uniqueStudentSet = UtilParser.getUniqueStudentSet(model.getTemporaryStudents(), model.getDBStudents());

                JSONObject hashKeyList = UtilParser.getStudentAttributeJSONObject(uniqueStudentSet, "hashKey");
                JSONObject emailList = UtilParser.getStudentAttributeJSONObject(uniqueStudentSet, "email");
                Map<String, JSONObject> urlContentsMap = new HashMap<>();
                urlContentsMap.put("Ids", hashKeyList);
                urlContentsMap.put("Emails", emailList);

                String combined = UtilParser.generateMultiPathJSONString(urlContentsMap);

                String baseURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "");
                String responseDataUpdate = dbHandler.updateData(baseURL, combined);


                model.getTemporaryStudents().clear();
                controllerLogic();
                asyncLoadStudentDataFromDB();
                System.out.println(responseDataUpdate);
            } catch (IOException | NullPointerException e) {
                System.err.println("[Error while uploading data to the DB]: " + e.getMessage());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void asyncRemoveStudentsFromDB() {
        Runnable runnable = () -> {
            try {
                Set<Student> coincidentStudentSet = UtilParser.getCoincidentStudentSet(model.getTemporaryStudents(), model.getDBStudents());
                JSONObject nullList = UtilParser.getStudentNullJSONObject(coincidentStudentSet);
                Map<String, JSONObject> urlContentsMap = new HashMap<>();
                urlContentsMap.put("Ids", nullList);
                urlContentsMap.put("Emails", nullList);
                urlContentsMap.put("Users", nullList);

                String combined = UtilParser.generateMultiPathJSONString(urlContentsMap);

                String baseURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "");
                String responseDataDelete = dbHandler.updateData(baseURL, combined);

                model.getTemporaryStudents().clear();
                controllerLogic();
                asyncLoadStudentDataFromDB();
                System.out.println(responseDataDelete);
            } catch (IOException | NullPointerException e) {
                System.err.println("[Error while deleting data from the DB]: " + e.getMessage());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void asyncEmptyDB() {
        Runnable runnable = () -> {
            try {
                String baseURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "");
                String response = dbHandler.emptyDB(baseURL);
                model.getDBStudents().clear();
                controllerLogic();
                System.out.println(response);
            } catch (IOException e) {
                System.err.println("Error deleting data from DB: " + e.getMessage());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
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

                model.addTemporaryStudentList(studentList);
            } catch (Exception e) {
                System.err.println("[Error loading the student list from CSV file]: " + e.getMessage());
            }

        }
    }

    private Set<Student> generateStudentListFromTable(Table studentIDTable) {
        Set<Student> students = new HashSet<>();
        for (TableRow row : studentIDTable.rows()) {
            String id = row.getString(0);
            System.out.println(studentIDTable.getColumnIndex("email"));
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
