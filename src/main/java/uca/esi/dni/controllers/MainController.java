package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.data.Table;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.ItemList;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.edit;

public class MainController extends Controller {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public MainController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);

        initModelState();
    }

    private void initModelState() {
        model.setDBReference(dbHandler.getDBReference(parent, "data/files/settings.json"));
        if (model.getDBStudents().isEmpty()) {
            asyncLoadStudentDataFromDB();
        }
    }

    @Override
    public void controllerLogic() {
        view.update(model.getDBStudents(), model.getTemporaryStudents(), model.getInputFile(), model.getDBReference(), model.getWarnings());
    }

    @Override
    public void handleMouseEvent(MouseEvent e) {
        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement("editB").inside(e.getX(), e.getY())) {
                    //change view to editView
                    changeState(edit);
                } else if (view.getUIElement("generateFilesB").inside(e.getX(), e.getY())) {
                    generateExcelFiles();
                } else if (view.getUIElement("generateStatsB").inside(e.getX(), e.getY())) {
                    //change view to statsView
                    //TODO: Implement stats view
                    //changeState(stats);
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
                    System.out.println("input in IL" + System.currentTimeMillis());
                }
                break;
        }
        controllerLogic();
    }

    @Override
    public void handleKeyEvent(KeyEvent e) {

    }

    public void generateExcelFiles() {
        closedContextMenu = false;

        parent.selectFolder("Seleccione la carpeta de destino:", "selectOutputFolder");
        while (!closedContextMenu) {
            Thread.onSpinWait(); //We need to wait for the output folder context menu to be closed before resuming execution
        }

        if (model.getDBReference() != null && model.getOutputFolder().exists()) {
            try {
                String usersURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Users");
                ArrayList<String> response = dbHandler.getDataFromDB(usersURL);
                if (response.get(0).equals("200")) {
                    JSONObject studentData = UtilParser.parseJSONObject(response.get(1));
                    Map<String, Map<String, Table>> tableMap = UtilParser.createStudentsDataTables(studentData);
                    if (model.getOutputFolder() != null) {
                        saveLabTables(tableMap, model.getOutputFolder());
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException when reading database");
                LOGGER.warning("[IOException when reading database]: " + e.getMessage());
            } catch (NullPointerException e) {
                System.err.println("NullPointerException when generating the excel files.");
                LOGGER.warning("[NullPointerException when generating the excel files]: " + e.getMessage());
            } catch (RuntimeException e) {
                System.err.println("[Error while generating the CSV files]: " + e.getMessage());
                LOGGER.severe("[Error while generating the CSV files]: \" + e.getMessage()");

            }
        }
    }

    private void saveLabTables(Map<String, Map<String, Table>> studentLabsMap, File route) {
        String pathToFolder = route.getAbsolutePath() + File.separator + "datos" + File.separator;
        for (String student : studentLabsMap.keySet()) {
            String pathToFolderStudent = pathToFolder + student + File.separator;
            Map<String, Table> typeTableMap = studentLabsMap.get(student);
            for (String type : typeTableMap.keySet()) {
                String pathToFolderStudentType = pathToFolderStudent + type + ".csv";
                Table labTable = typeTableMap.get(type);
                try {
                    parent.saveTable(labTable, pathToFolderStudentType, "csv");
                } catch (Exception e) {
                    System.err.println("[Error while saving the student data tables]: " + e.getMessage());
                    LOGGER.warning("[Error while saving the student data tables]: " + e.getMessage());
                }
            }
        }
        System.out.println("Generated all files");
        LOGGER.info("Generated all files.");
    }

    @Override
    public void onContextMenuClosed(File folder) {
        model.setOutputFolder(folder);

    }

}
