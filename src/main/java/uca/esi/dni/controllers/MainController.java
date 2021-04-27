package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.data.Table;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.Util;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.EDIT;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.STATS;

public class MainController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public MainController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);

        initModelState();
    }

    private void initModelState() {
        model.setSettingsObject(Util.loadJSONObject(DniParser.SETTINGS_FILEPATH));
        model.setDBReference(model.getSettingsObject().getString("databaseURL"));
        if (model.getDbStudents().isEmpty() && !model.isDataFirstLoaded()) {
            asyncLoadStudentDataFromDB();
            model.setDataFirstLoaded(true);
        }
    }

    @Override
    public void controllerLogic() {
        view.update(model.getDbStudents(), model.getTemporaryStudents(), model.getInputFile(), model.getStudentSurveys());
    }

    @Override
    public void handleMouseEvent(MouseEvent e) {
        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement("editB").inside(e.getX(), e.getY())) {
                    //change view to editView
                    changeState(EDIT);
                } else if (view.getUIElement("generateFilesB").inside(e.getX(), e.getY())) {
                    generateExcelFiles();
                } else if (view.getUIElement("generateStatsB").inside(e.getX(), e.getY())) {
                    changeState(STATS);
                } else if (view.getUIElement("dbStudentsIL").inside(e.getX(), e.getY())) {
                    view.getUIElement("dbStudentsIL").handleInput(e);
                }
                for (String key : view.getElementKeys()) {
                    BaseElement element = view.getUIElement(key);
                    if (element.isClicked()) {
                        element.isClicked(false);
                    }
                }
                break;
            case MOVE:
                checkHover(e.getX(), e.getY());
                break;
            case PRESS:
                checkPress(e.getX(), e.getY());
                break;
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                ItemList list = (ItemList) view.getUIElement("dbStudentsIL");
                if (list.inside(e.getX(), e.getY())) {
                    list.handleInput(e);
                }
                break;
            default:
                break;
        }
        controllerLogic();
    }

    @Override
    public void handleKeyEvent(KeyEvent e) {
        // There are no UI elements that need to handle key inputs in the main View
    }

    public void generateExcelFiles() {
        closedContextMenu = false;

        parent.selectFolder("Seleccione la carpeta de destino:", "selectOutputFolder");
        while (!closedContextMenu) {
            Thread.yield(); //We need to wait for the output folder context menu to be closed before resuming execution
        }

        if (model.getDBReference() != null && model.getOutputFolder().exists()) {
            try {
                String usersURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Users");
                String response = dbHandler.getDataFromDB(usersURL);
                JSONObject studentData = Util.parseJSONObject(response);
                Map<String, Map<String, Table>> tableMap = Util.createStudentsDataTables(studentData);
                if (model.getOutputFolder() != null) {
                    saveLabTables(tableMap, model.getOutputFolder());
                    addWarning("Generados archivos de alumnos.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
                }

            } catch (IOException e) {
                LOGGER.warning("[IOException when reading database]: " + e.getMessage());
                addWarning("Error leyendo la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            } catch (RuntimeException e) {
                LOGGER.severe("[NullPointerException when generating the CSV files]: " + e.getMessage());
                addWarning("Error generando los archivos.", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            } catch (DatabaseResponseException e) {
                LOGGER.warning("[Error while getting data from the DB]: " + e.getMessage());
                addWarning("Error leyendo la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            }
        }
    }

    private void saveLabTables(Map<String, Map<String, Table>> studentLabsMap, File route) {
        String pathToFolder = route.getAbsolutePath() + File.separator + "datos" + File.separator;
        for (Map.Entry<String, Map<String, Table>> studentLabEntry : studentLabsMap.entrySet()) {
            String pathToFolderStudent = pathToFolder + studentLabEntry.getKey() + File.separator;
            Map<String, Table> typeTableMap = studentLabEntry.getValue();
            for (Map.Entry<String, Table> typeTableEntry : typeTableMap.entrySet()) {
                String pathToFolderStudentType = pathToFolderStudent + typeTableEntry.getKey() + ".csv";
                Table labTable = typeTableEntry.getValue();
                try {
                    parent.saveTable(labTable, pathToFolderStudentType, "csv");
                } catch (Exception e) {
                    LOGGER.warning("[Error while saving the student data tables]: " + e.getMessage());
                }
            }
        }
        LOGGER.info("[General information]: Generated all CSV files.");
    }

    @Override
    public void onContextMenuClosed(File folder) {
        model.setOutputFolder(folder);

    }

}
