package uca.esi.dni.controllers;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.EmailHandler;
import uca.esi.dni.file.Util;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.types.Student;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public abstract class Controller {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public enum VIEW_STATES {
        MAIN,
        EDIT,
        STATS
    }

    protected static final DatabaseHandler dbHandler = new DatabaseHandler();
    protected final AppModel model;
    protected final View view;
    protected final DniParser parent;

    protected volatile boolean closedContextMenu = false;


    protected Controller(DniParser parent, AppModel model, View view) {
        this.parent = parent;
        this.model = model;
        this.view = view;
        EmailHandler.loadSettings();
    }

    public boolean isClosedContextMenu() {
        return closedContextMenu;
    }

    public void setClosedContextMenu(boolean closedContextMenu) {
        this.closedContextMenu = closedContextMenu;
    }

    public abstract void controllerLogic();

    public abstract void handleMouseEvent(MouseEvent e);

    public abstract void handleKeyEvent(KeyEvent e);

    public void onContextMenuClosed(File file) {
    }

    public void addWarning(String contentString, Warning.DURATION duration, Warning.TYPE type) {
        view.getWarnings().add(View.generateWarning(parent, contentString, duration, type));
    }

    public void changeState(VIEW_STATES state) {
        View newView;
        Controller newController;
        switch (state) {
            case EDIT:
                newView = new EditView(parent);
                newController = new EditController(parent, parent.getAppModel(), newView);
                break;
            case STATS:
                newView = new StatsView(parent);
                newController = new StatsController(parent, parent.getAppModel(), newView);
                break;
            default:
                newView = new MainView(parent);
                newController = new MainController(parent, parent.getAppModel(), newView);
                break;
        }
        parent.setCurrentView(newView);
        parent.setCurrentController(newController);
        loadInitialState();
    }

    protected void removeClick() {
        if (view.isModalActive()) {

            for (String key : view.getModalElementKeys()) {
                BaseElement element = view.getUIModalElement(key);
                if (element.isClicked()) {
                    element.isClicked(false);
                }
            }
        } else {
            for (String key : view.getElementKeys()) {
                BaseElement element = view.getUIElement(key);
                if (element.isClicked()) {
                    element.isClicked(false);
                }
            }
        }
    }

    public void checkHover(int x, int y) {
        if (!view.isModalActive()) {
            for (String key : view.getElementKeys()) {
                BaseElement element = view.getUIElement(key);
                element.isHover(element.inside(x, y));
            }
        } else {
            for (String key : view.getModalElementKeys()) {
                BaseElement element = view.getUIModalElement(key);
                element.isHover(element.inside(x, y));
            }
        }
    }

    public void checkPress(int x, int y) {
        if (!view.isModalActive()) {
            for (String key : view.getElementKeys()) {
                BaseElement element = view.getUIElement(key);
                element.isClicked(element.inside(x, y));
            }
        } else {
            for (String key : view.getModalElementKeys()) {
                BaseElement element = view.getUIModalElement(key);
                element.isClicked(element.inside(x, y));
            }
        }
    }

    protected void asyncLoadStudentDataFromDB() {

        Runnable runnable = () -> {
            try {
                String idsURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Ids");
                String responseIDs = dbHandler.getDataFromDB(idsURL);

                String emailsURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Emails");
                String responseEmails = dbHandler.getDataFromDB(emailsURL);

                Set<Student> studentsInDB = generateStudentSetFromDB(responseIDs, responseEmails);
                model.getDbStudents().clear();
                model.addDBStudentList(studentsInDB);
                addWarning("Cargados datos de la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
                controllerLogic();
                String toLog = "[General information]: Loaded " + studentsInDB.size() + " students from DB.";
                LOGGER.info(toLog);

            } catch (IOException | NullPointerException | DatabaseResponseException e) {
                LOGGER.warning("[Error loading data from DB]: " + e.getMessage());
                addWarning("Error leyendo la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            } catch (RuntimeException e) {
                LOGGER.severe("[Error generating student list]: " + e.getMessage());
                addWarning("Error generando la lista de alumnos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            }

        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @NotNull
    private Set<Student> generateStudentSetFromDB(String responseIDs, String responseEmails) {
        JSONObject studentKeys = Util.parseJSONObject(responseIDs);
        JSONObject studentEmails = Util.parseJSONObject(responseEmails);
        return Util.generateStudentListFromJSONObject(studentKeys, studentEmails);
    }

    private void loadInitialState() {
        parent.getCurrentController().controllerLogic();
    }

}
