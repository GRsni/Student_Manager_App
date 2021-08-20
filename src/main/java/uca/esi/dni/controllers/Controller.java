package uca.esi.dni.controllers;

import org.jetbrains.annotations.NotNull;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.handlers.DB.DatabaseHandler;
import uca.esi.dni.handlers.DB.DatabaseHandlerI;
import uca.esi.dni.handlers.Email.EmailHandler;
import uca.esi.dni.handlers.Email.EmailHandlerI;
import uca.esi.dni.handlers.JSON.JSONHandler;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.types.JSONParsingException;
import uca.esi.dni.types.Student;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The type Controller.
 */
public abstract class Controller {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The enum View states.
     */
    public enum VIEW_STATES {
        /**
         * Main view states.
         */
        MAIN,
        /**
         * Edit view states.
         */
        EDIT,
        /**
         * Stats view states.
         */
        STATS
    }

    /**
     * The constant dbHandler.
     */
    protected static final DatabaseHandlerI dbHandler = new DatabaseHandler();
    /**
     * The constant emailHandler.
     */
    protected static final EmailHandlerI emailHandler = new EmailHandler();
    /**
     * The Model.
     */
    protected final AppModel model;
    /**
     * The View.
     */
    protected final View view;
    /**
     * The Parent.
     */
    protected final DniParser parent;

    /**
     * The Closed context menu.
     */
    protected volatile boolean closedContextMenu = false;


    /**
     * Instantiates a new Controller.
     *
     * @param parent the parent
     * @param model  the model
     * @param view   the view
     */
    protected Controller(DniParser parent, AppModel model, View view) {
        this.parent = parent;
        this.model = model;
        this.view = view;
        emailHandler.loadSettings(model.getSettingsObject());

    }

    /**
     * On create.
     */
    protected abstract void onCreate();

    /**
     * Is closed context menu boolean.
     *
     * @return the boolean
     */
    public boolean isClosedContextMenu() {
        return closedContextMenu;
    }

    /**
     * Sets closed context menu.
     *
     * @param closedContextMenu the closed context menu
     */
    public void setClosedContextMenu(boolean closedContextMenu) {
        this.closedContextMenu = closedContextMenu;
    }

    /**
     * Controller logic.
     */
    public void controllerLogic() {
        view.update(model.getDbStudents(), model.getTemporaryStudents(), model.getInputFile(), model.getStudentSurveys());
    }

    /**
     * Handle mouse event.
     *
     * @param e the e
     */
    public abstract void handleMouseEvent(MouseEvent e);

    /**
     * Handle key event.
     *
     * @param e the e
     */
    public abstract void handleKeyEvent(KeyEvent e);

    /**
     * On context menu closed.
     *
     * @param file the file
     */
    public abstract void onContextMenuClosed(File file);

    /**
     * Add warning.
     *
     * @param contentString the content string
     * @param duration      the duration
     * @param type          the type
     */
    public void addWarning(String contentString, Warning.DURATION duration, Warning.TYPE type) {
        view.getWarnings().add(View.generateWarning(parent, contentString, duration, type));
    }

    /**
     * Change state.
     *
     * @param state the state
     */
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

    /**
     * Remove click.
     */
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

    /**
     * Check hover.
     *
     * @param x the x
     * @param y the y
     */
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

    /**
     * Check press.
     *
     * @param x the x
     * @param y the y
     */
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

    /**
     * Async load student data from db.
     */
    protected void asyncLoadStudentDataFromDB() {

        Runnable runnable = () -> {
            try {
                String idsURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Ids");
                String responseIDs = dbHandler.getData(idsURL);

                String emailsURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Emails");
                String responseEmails = dbHandler.getData(emailsURL);

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

    /**
     * Generate student set from db set.
     *
     * @param responseIDs    the response i ds
     * @param responseEmails the response emails
     * @return the set
     */
    @NotNull
    private Set<Student> generateStudentSetFromDB(String responseIDs, String responseEmails) {
        JSONObject studentKeys = JSONHandler.parseJSONObject(responseIDs);
        JSONObject studentEmails = JSONHandler.parseJSONObject(responseEmails);
        return generateStudentListFromJSONObject(studentKeys, studentEmails);
    }

    /**
     * Generate student list from json object set.
     *
     * @param hashKeys the hash keys
     * @param emails   the emails
     * @return the set
     * @throws JSONParsingException the json parsing exception
     */
    private Set<Student> generateStudentListFromJSONObject(JSONObject hashKeys, JSONObject emails) throws
            JSONParsingException {
        Set<Student> students = new HashSet<>();
        List<String> ids = JSONHandler.getJSONObjectKeys(emails);
        for (String id : ids) {
            try {
                Student student = new Student(id, emails.getString(id), hashKeys.getString(id));
                students.add(student);
            } catch (RuntimeException e) {
                throw new JSONParsingException(e.getMessage());
            }
        }
        return students;
    }

    /**
     * Load initial state.
     */
    private void loadInitialState() {
        parent.getCurrentController().controllerLogic();
    }

    /**
     * On app closing.
     */
    public void onAppClosing() {
        addWarning("Cerrando aplicación.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
        if (model.isStudentDataModified() && !model.getDbStudents().isEmpty()) {
            emailHandler.sendBackupEmail(DniParser.DATA_BACKUP_FILEPATH);
        }
        LOGGER.info("[General information]: Closing app");
    }
}
