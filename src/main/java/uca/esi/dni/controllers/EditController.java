package uca.esi.dni.controllers;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.handlers.*;
import uca.esi.dni.handlers.db.DatabaseHandler;
import uca.esi.dni.handlers.Email.EmailHandlerI;
import uca.esi.dni.handlers.json.JSONHandler;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.types.Student;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.TextField;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static processing.core.PConstants.ENTER;
import static processing.core.PConstants.*;
import static processing.event.KeyEvent.TYPE;
import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.MAIN;

/**
 * The type Edit controller.
 */
public class EditController extends Controller {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Instantiates a new Edit controller.
     *
     * @param parent the parent
     * @param model  the model
     * @param view   the view
     */
    public EditController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);
        onCreate();
    }

    /**
     * On create.
     */
    @Override
    protected void onCreate() {
        controllerLogic();
    }

    /**
     * Handle mouse event.
     *
     * @param e the e
     */
    @Override
    public void handleMouseEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        switch (e.getAction()) {
            case CLICK:
                handleClickEvent(e, x, y);
                removeClick();
                break;
            case MouseEvent.MOVE:
                checkHover(x, y);
                break;
            case PRESS:
                checkPress(x, y);
                break;
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                ItemList dbList = (ItemList) view.getUIElement("dbStudentsIL");
                ItemList auxList = (ItemList) view.getUIElement("auxStudentsIL");
                if (dbList.inside(x, y)) {
                    dbList.handleInput(e);
                } else if (auxList.inside(x, y)) {
                    auxList.handleInput(e);
                }
                break;
            default:
                break;
        }
        controllerLogic();
    }

    /**
     * Handle click event.
     *
     * @param e the e
     * @param x the x
     * @param y the y
     */
    private void handleClickEvent(MouseEvent e, int x, int y) {
        if (view.isModalActive()) {
            handleModalClickEvent(x, y);
        } else {
            handleNonModalClickEvent(e, x, y);
        }
    }

    /**
     * Handle modal click event.
     *
     * @param x the x
     * @param y the y
     */
    private void handleModalClickEvent(int x, int y) {
        if (view.getUIModalElement("confirmEmptyB").inside(x, y)) {
            asyncEmptyDBButtonHook();
        }
        setModalVisibility(false);
    }

    /**
     * Handle non modal click event.
     *
     * @param e the e
     * @param x the x
     * @param y the y
     */
    private void handleNonModalClickEvent(MouseEvent e, int x, int y) {
        TextField idTF = (TextField) view.getUIElement("idTF");
        TextField emailTF = (TextField) view.getUIElement("emailTF");
        if (view.getUIElement("enterStudentB").inside(x, y)) {
            addManualDataToAuxList(idTF, emailTF);
            clearTextFields(idTF, emailTF);
        } else if (view.getUIElement("removeStudentAuxB").inside(x, y)) {
            removeStudentFromAuxList(idTF, emailTF);
            clearTextFields(idTF, emailTF);
        } else if (view.getUIElement("selectFileB").inside(x, y)) {
            addStudentsFromFileToTemporaryList();
        } else if (view.getUIElement("backB").inside(x, y)) {
            changeState(MAIN);
        } else if (view.getUIElement("addToListB").inside(x, y)) {
            asyncAddStudentsToDBListButtonHook();
        } else if (view.getUIElement("deleteFromListB").inside(x, y)) {
            asyncRemoveStudentsFromDBButtonHook();
        } else if (view.getUIElement("emptyListB").inside(x, y)) {
            setModalVisibility(true);
        } else if (idTF.inside(x, y)) {
            idTF.setFocused(true);
            emailTF.setFocused(false);
        } else if (emailTF.inside(x, y)) {
            idTF.setFocused(false);
            emailTF.setFocused(true);
        } else if (view.getUIElement("dbStudentsIL").inside(x, y)) {
            view.getUIElement("dbStudentsIL").handleInput(e);
        } else if (view.getUIElement("auxStudentsIL").inside(x, y)) {
            view.getUIElement("auxStudentsIL").handleInput(e);
        }

    }

    /**
     * Handle key event.
     *
     * @param e the e
     */
    @Override
    public void handleKeyEvent(KeyEvent e) {
        if (e.getKey() == ESC) {
            if (view.isModalActive()) {
                parent.key = 0; //Hack to stop Processing from closing the app when pressing the ESC key
                view.setModalActive(false);
            }
        } else if (e.getAction() == TYPE) {
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

    /**
     * Sets modal visibility.
     *
     * @param visibility the visibility
     */
    private void setModalVisibility(boolean visibility) {
        view.setModalActive(visibility);
        for (String key : view.getModalElementKeys()) {
            view.getUIModalElement(key).setVisible(visibility);
        }
    }

    /**
     * Add manual data to aux list.
     *
     * @param idTF    the id tf
     * @param emailTF the email tf
     */
    private void addManualDataToAuxList(TextField idTF, TextField emailTF) {
        if (checkValidManualStudentData(idTF, emailTF)) {
            try {
                model.addTemporaryStudent(new Student(idTF.getContent().toLowerCase(Locale.ROOT), emailTF.getContent().toLowerCase(Locale.ROOT)));
                addWarning("Alumno introducido correctamente.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
            } catch (NullPointerException e) {
                LOGGER.severe("[Error while trying to insert new Student into temporary list]: " + e.getMessage());
                addWarning("Error al introducir el alumno.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            }
        }
    }

    /**
     * Clear text fields.
     *
     * @param idTF    the id tf
     * @param emailTF the email tf
     */
    private void clearTextFields(TextField idTF, TextField emailTF) {
        idTF.setContent("");
        emailTF.setContent("");
        getFocusedTextField().setFocused(false);
    }


    /**
     * Check valid manual student data boolean.
     *
     * @param idTF    the id tf
     * @param emailTF the email tf
     * @return the boolean
     */
    private boolean checkValidManualStudentData(TextField idTF, TextField emailTF) {
        return checkUserIDTextField(idTF) && checkUserEmailTextField(emailTF);
    }

    /**
     * Check user id text field boolean.
     *
     * @param idTF the id tf
     * @return the boolean
     */
    private boolean checkUserIDTextField(TextField idTF) {
        if (!idTF.getContent().isEmpty()) {
            return checkUserIDString(idTF.getContent());
        } else {
            LOGGER.warning("[Error while reading student data]:Empty ID field.");
            addWarning("Identificador no introducido.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            return false;
        }
    }

    /**
     * Check user id string boolean.
     *
     * @param id the id
     * @return the boolean
     */
    private boolean checkUserIDString(String id) {
        if (Util.checkId(id)) {
            return true;
        } else {
            LOGGER.warning("[Error validating user Id]: Wrong user ID.");
            addWarning("Identificador no válido", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            return false;
        }
    }

    /**
     * Check user email text field boolean.
     *
     * @param emailTF the email tf
     * @return the boolean
     */
    private boolean checkUserEmailTextField(TextField emailTF) {
        if (!emailTF.getContent().isEmpty()) {
            return checkUserEmailString(emailTF.getContent());
        } else {
            return true;
        }
    }

    /**
     * Check user email string boolean.
     *
     * @param email the email
     * @return the boolean
     */
    private boolean checkUserEmailString(String email) {
        if (EmailHandlerI.isValidEmailAddress(email)) {
            return true;
        } else {
            LOGGER.warning("[Error validating email address]: No valid address.");
            addWarning("Email no válido.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            return false;
        }
    }

    /**
     * Remove student from aux list.
     *
     * @param idTF    the id tf
     * @param emailTF the email tf
     */
    private void removeStudentFromAuxList(TextField idTF, TextField emailTF) {
        if (checkValidManualStudentData(idTF, emailTF)) {
            for (Student student : model.getTemporaryStudents()) {
                if (student.getId().equals(idTF.getContent())) {
                    model.removeTemporaryStudent(student);
                    addWarning("Alumno eliminado de la lista temporal.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
                    break;
                }
            }
        }
    }


    /**
     * Async add students to db list button hook.
     */
    private void asyncAddStudentsToDBListButtonHook() {
        addWarning("Cargando.", Warning.DURATION.SHORTEST, Warning.TYPE.INFO);
        Runnable runnable = () -> {
            Set<Student> uniqueStudentSet = Util.getUniqueStudentSet(model.getTemporaryStudents(), model.getDbStudents());
            if (!uniqueStudentSet.isEmpty()) {
                try {
                    uploadStudentListToDB(uniqueStudentSet);

                    model.setStudentDataModified(true);
                    model.getTemporaryStudents().clear();
                    savePlainStudentDataToFile(uniqueStudentSet);
                    emailHandler.sendSecretKeyEmails(uniqueStudentSet);
                    addWarning("Alumnos añadidos a la base de datos.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
                    String toLog = "[General information]: Added " + uniqueStudentSet.size() + " students to DB.";
                    LOGGER.info(toLog);
                    controllerLogic();

                    asyncLoadStudentDataFromDB();

                } catch (IOException | NullPointerException | DatabaseResponseException e) {
                    LOGGER.severe("[Error while uploading data to the DB]: " + e.getMessage());
                    addWarning("Error subiendo datos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Upload student list to db.
     *
     * @param uniqueStudentSet the unique student set
     * @throws IOException               the io exception
     * @throws DatabaseResponseException the database response exception
     */
    private void uploadStudentListToDB(Set<Student> uniqueStudentSet) throws IOException, DatabaseResponseException {
        Map<String, JSONObject> urlContentsMap = getHashKeyEmailMap(uniqueStudentSet);
        String combined = JSONHandler.generateMultiPathJSONString(urlContentsMap);
        String baseURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference());
        dbHandler.updateData(baseURL, combined);
    }

    /**
     * Gets hash key email map.
     *
     * @param uniqueStudentSet the unique student set
     * @return the hash key email map
     */
    @NotNull
    private Map<String, JSONObject> getHashKeyEmailMap(Set<Student> uniqueStudentSet) {
        JSONObject hashKeyList = Util.getStudentAttributeJSONObject(uniqueStudentSet, "hashKey");
        JSONObject emailList = Util.getStudentAttributeJSONObject(uniqueStudentSet, "email");
        Map<String, JSONObject> urlContentsMap = new HashMap<>();
        urlContentsMap.put("Ids", hashKeyList);
        urlContentsMap.put("Emails", emailList);
        return urlContentsMap;
    }

    /**
     * Save plain student data to file.
     *
     * @param students the students
     */
    private void savePlainStudentDataToFile(Set<Student> students) {
        try {
            JSONObject studentBackup = parent.loadJSONObject(DniParser.DATA_BACKUP_FILEPATH);
            for (Student student : students) {
                studentBackup.setString(student.getId(), student.toString());
            }
            parent.saveJSONObject(studentBackup, DniParser.DATA_BACKUP_FILEPATH);
        } catch (NullPointerException e) {
            LOGGER.severe("[Error while saving plain student data]: " + e.getMessage());
        }
    }


    /**
     * Async remove students from db button hook.
     */
    private void asyncRemoveStudentsFromDBButtonHook() {
        addWarning("Cargando.", Warning.DURATION.SHORTEST, Warning.TYPE.INFO);
        Runnable runnable = () -> {
            Set<Student> coincidentStudentSet = Util.getIntersectionOfStudentSets(model.getTemporaryStudents(), model.getDbStudents());
            try {
                if (!coincidentStudentSet.isEmpty()) {
                    Map<String, JSONObject> urlContentsMap = getIDsEmailsUsersNullListMap(coincidentStudentSet);
                    String combined = JSONHandler.generateMultiPathJSONString(urlContentsMap);
                    String baseURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference());
                    dbHandler.updateData(baseURL, combined);

                    model.setStudentDataModified(true);
                    model.getTemporaryStudents().clear();
                    removePlainStudentDataFromFile(coincidentStudentSet);
                    addWarning("Borrados alumnos de la base de datos.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
                    String toLog = "[General information]: Removed " + coincidentStudentSet.size() + " students from DB.";
                    LOGGER.info(toLog);
                    controllerLogic();

                    asyncLoadStudentDataFromDB();

                }

            } catch (IOException | NullPointerException | DatabaseResponseException e) {
                LOGGER.severe("[Error while deleting data from the DB]: " + e.getMessage());
                addWarning("Error eliminando alumnos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Remove plain student data from file.
     *
     * @param students the students
     */
    private void removePlainStudentDataFromFile(Set<Student> students) {
        try {
            JSONObject studentBackup = parent.loadJSONObject(DniParser.DATA_BACKUP_FILEPATH);
            for (Student student : students) {
                if (!studentBackup.isNull(student.getId())) {
                    studentBackup.remove(student.getId());
                }
            }
            parent.saveJSONObject(studentBackup, DniParser.DATA_BACKUP_FILEPATH);
        } catch (NullPointerException e) {
            LOGGER.severe("[Error while saving plain student data]: " + e.getMessage());
        }
    }

    /**
     * Gets i ds emails users null list map.
     *
     * @param coincidentStudentSet the coincident student set
     * @return the i ds emails users null list map
     */
    @NotNull
    private Map<String, JSONObject> getIDsEmailsUsersNullListMap(Set<Student> coincidentStudentSet) {
        JSONObject nullList = Util.getStudentAttributeJSONObject(coincidentStudentSet, null);
        Map<String, JSONObject> urlContentsMap = new HashMap<>();
        urlContentsMap.put("Ids", nullList);
        urlContentsMap.put("Emails", nullList);
        urlContentsMap.put("Users", nullList);
        return urlContentsMap;
    }

    /**
     * Async empty db button hook.
     */
    private void asyncEmptyDBButtonHook() {
        addWarning("Cargando.", Warning.DURATION.SHORTEST, Warning.TYPE.INFO);
        Runnable runnable = () -> {
            try {
                String baseURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference());

                Map<String, JSONObject> urlContentsMap = getIDsEmailsUsersNullListMap(model.getDbStudents());
                String combined = JSONHandler.generateMultiPathJSONString(urlContentsMap);

                dbHandler.updateData(baseURL, combined);
                removePlainStudentDataFromFile(model.getDbStudents());
                model.getDbStudents().clear();
                model.setStudentDataModified(true);

                addWarning("Base de datos vaciada.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
                LOGGER.info("[General information]: Emptied data from DB.");
                controllerLogic();

            } catch (IOException | DatabaseResponseException e) {
                LOGGER.severe("[Error deleting data from DB]: " + e.getMessage());
                addWarning("Error vaciando la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Add students from file to temporary list.
     */
    private void addStudentsFromFileToTemporaryList() {
        selectInputFile();
        if (model.getInputFile().exists()) {
            try {
                Table studentIDTable = parent.loadTable(model.getInputFile().getAbsolutePath(), "header");
                Set<Student> newStudentList = generateStudentListFromTable(studentIDTable);
                Set<Student> unique = Util.getUniqueStudentSet(newStudentList, model.getTemporaryStudents());
                model.addTemporaryStudentList(unique);
            } catch (Exception e) {
                LOGGER.warning("[Error loading the student list from CSV file]: " + e.getMessage());
            }
        }
    }

    /**
     * Select input file.
     */
    private void selectInputFile() {
        closedContextMenu = false;

        parent.selectInput("Seleccione el archivo de texto:", "selectInputFile");
        while (!closedContextMenu) {
            Thread.yield();
        }//We need to wait for the input file context menu to be closed before resuming execution
    }

    /**
     * Generate student list from table set.
     *
     * @param studentIDTable the student id table
     * @return the set
     */
    private Set<Student> generateStudentListFromTable(Table studentIDTable) {
        Set<Student> students = new HashSet<>();
        int failedEmails = 0;
        int failedIDs = 0;
        for (TableRow row : studentIDTable.rows()) {
            String id = row.getString(0);
            String email = row.getString("email");
            if (Util.checkId(id)) {
                if (EmailHandlerI.isValidEmailAddress(email)) {
                    try {
                        students.add(new Student(id, email));
                    } catch (NullPointerException e) {
                        LOGGER.severe("[Error while trying to insert new Student into temporary list]: " + e.getMessage());
                    }
                } else {
                    failedEmails++;
                }
            } else {
                failedIDs++;
            }
        }
        if (failedIDs > 0) {
            addWarning("Detectados " + failedIDs + " errores con indentificadores.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            String toLog = "[General information]: " + failedIDs + " invalid user IDs detected.";
            LOGGER.warning(toLog);
        }
        if (failedEmails > 0) {
            addWarning("Detectados " + failedEmails + " errores con emails.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            String toLog = "[General information]: " + failedEmails + " invalid emails detected.";
            LOGGER.warning(toLog);
        }
        return students;
    }

    /**
     * Gets focused text field.
     *
     * @return the focused text field
     */
    private TextField getFocusedTextField() {
        TextField idTF = (TextField) view.getUIElement("idTF");
        TextField emailTF = (TextField) view.getUIElement("emailTF");
        if (idTF.isFocused()) {
            return idTF;
        } else {
            return emailTF;
        }
    }

    /**
     * On context menu closed.
     *
     * @param file the file
     */
    @Override
    public void onContextMenuClosed(File file) {
        if (file != model.getInputFile()) {
            model.setInputFile(file);
        }
        controllerLogic();
    }
}
