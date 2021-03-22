package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.EmailHandler;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

public abstract class Controller {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public enum VIEW_STATES {
        main,
        edit,
        stats
    }

    protected static final DatabaseHandler dbHandler = new DatabaseHandler();
    protected static EmailHandler emailHandler;
    protected final AppModel model;
    protected final View view;
    protected final DniParser parent;

    public volatile boolean closedContextMenu = false;


    public Controller(DniParser parent, AppModel model, View view) {
        this.parent = parent;
        this.model = model;
        this.view = view;
        emailHandler = new EmailHandler(parent);
    }

    public boolean isClosedContextMenu() {
        return closedContextMenu;
    }

    public void setClosedContextMenu(boolean closedContextMenu) {
        this.closedContextMenu = closedContextMenu;
    }

    public static DatabaseHandler getDbHandler() {
        return dbHandler;
    }

    public abstract void controllerLogic();

    public abstract void handleMouseEvent(MouseEvent e);

    public abstract void handleKeyEvent(KeyEvent e);

    public abstract void onContextMenuClosed(File file);

    public void changeState(VIEW_STATES state) {
        switch (state) {
            case edit:
                DniParser.currentView = new EditView(parent);
                DniParser.currentController = new EditController(parent, DniParser.appModel, DniParser.currentView);
                break;
            case stats:
                DniParser.currentView = new StatsView(parent);
                DniParser.currentController = new StatsController(parent, DniParser.appModel, DniParser.currentView);
                break;
            case main:
                DniParser.currentView = new MainView(parent);
                DniParser.currentController = new MainController(parent, DniParser.appModel, DniParser.currentView);
                break;
        }
        loadInitialState();
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
                String idsURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "Ids");
                ArrayList<String> responseIDs = dbHandler.getDataFromDB(idsURL);

                String emailsURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "Emails");
                ArrayList<String> responseEmails = dbHandler.getDataFromDB(emailsURL);

                JSONObject studentKeys = JSONObject.parse(responseIDs.get(1));
                JSONObject studentEmails = JSONObject.parse(responseEmails.get((1)));
                Set<Student> studentsInDB = UtilParser.generateStudentListFromJSONObject(studentKeys, studentEmails);

                model.getDBStudents().clear();
                model.addDBStudentList(studentsInDB);
                controllerLogic();
            } catch (IOException | NullPointerException e) {
                System.err.println("[Error loading data from DB]: " + e.getMessage());
                LOGGER.warning("[Error loading data from DB]: " + e.getMessage());
                //Add warning
            } catch (RuntimeException e) {
                System.err.println("[Error generating student list]: " + e.getMessage());
                LOGGER.warning("[Error generating student list]: " + e.getMessage());
            }

        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    private void loadInitialState() {
        DniParser.currentController.controllerLogic();
    }

}
