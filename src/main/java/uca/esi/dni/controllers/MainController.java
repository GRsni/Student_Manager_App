package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.data.Table;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.edit;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.stats;

public class MainController extends Controller {

    public MainController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);

        initModelState();
    }

    private void initModelState() {
        model.setDBReference(Controller.getDbHandler().getDBReference(parent, "data/files/databaseURL.json"));
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
                if (view.getUIElement(0).inside(e.getX(), e.getY())) {
                    //change view to editView
                    changeState(edit);
                } else if (view.getUIElement(1).inside(e.getX(), e.getY())) {
                    generateExcelFiles();
                } else if (view.getUIElement(2).inside(e.getX(), e.getY())) {
                    //change view to statsView
                    changeState(stats);
                }
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    if (element.isClicked()) {
                        element.isClicked(false);
                    }
                }
                controllerLogic();
                break;
            case MOVE:
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    element.isHover(element.inside(e.getX(), e.getY()));

                }
                break;
            case PRESS:
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    if (element.inside(e.getX(), e.getY())) {
                        element.isClicked(true);
                    }
                }
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                BaseElement list = view.getUIElement(4);
                //TODO:scroll item list
        }
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

        if (model.getDBReference() != null && model.getOutputFolder() != null) {
            try {
                String usersURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "Users");
                JSONObject studentData = JSONObject.parse(dbHandler.getDataFromDB(usersURL));
                Map<String, Map<String, Table>> tableMap = UtilParser.createStudentsDataTables(studentData);
                if (model.getOutputFolder() != null) {
                    saveLabTables(tableMap, model.getOutputFolder());
                }
            } catch (IOException e) {
                System.err.println("IOException when reading database");
            } catch (NullPointerException e) {
                System.err.println("Empty response from database");
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
                    System.err.println("Error while saving the student data tables");
                }
            }
        }
        System.out.println("Generated all files");
    }

    private void asyncLoadStudentDataFromDB() {

        Runnable runnable = () -> {
            try {
                String usersURL = dbHandler.generateDatabaseDirectoryURL(model.getDBReference(), "Ids");
                JSONObject data = JSONObject.parse(dbHandler.getDataFromDB(usersURL));
                Set<Student> studentsInDB = UtilParser.generateStudentListFromJSONObject(data);
                model.addDBStudentList(studentsInDB);
                controllerLogic();
            } catch (IOException e) {
                System.err.println("Error cargando datos de alumnos desde la base de datos.");
                //Add warning
            } catch (RuntimeException e) {
                System.err.println("Error generando la lista de alumnos");
            }

        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    public void onContextMenuClosed(File folder) {
        model.setOutputFolder(folder);

    }

}
