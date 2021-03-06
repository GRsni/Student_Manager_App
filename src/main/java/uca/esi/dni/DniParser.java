package uca.esi.dni;

import processing.core.PApplet;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.InfoParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.View;

import java.io.File;

public class DniParser extends PApplet {

    public static AppModel appModel;
    public static View currentView;
    public static Controller currentController;

    public static void main(String[] args) {
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    public void settings() {
        size(displayWidth / 2, displayHeight / 2);
    }

    public void setup() {
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        surface.setResizable(false);
        registerMethod("mouseEvent", this);
        initMVCObjects();
        initState();
    }

    private void initMVCObjects() {
        appModel = new AppModel();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
    }

    private void initState() {
        appModel.setDBReference(DatabaseHandler.getDBReference(this, "data/files/databaseURL.json"));
        //TODO: read student list from database
        currentController.controllerLogic();
    }


    public void draw() {
        background(255);
        currentView.show();
    }

    public void mouseEvent(MouseEvent e) {
        currentController.handleEvent(e);
    }

    public void selectTextFile(File selection) {
        if (selection == null) {
            println("No file selected");

        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (InfoParser.checkFileExtension(filePath, ".txt")) {
                // textHandler.setFileName(selection.getName());
            } else {
            }
        }
    }

    public void selectJSONFile(File selection) {
        if (selection == null) {
            println("No file selected");
        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (InfoParser.checkFileExtension(filePath, ".json")) {
            } else {
            }
        }
    }

    public void selectOutputFolder(File folder) {
        if (folder == null) {
            println("No folder selected");
        } else {
            String folderPath = folder.getAbsolutePath();
            println("user selected folder:" + folderPath);
            if (folder.isDirectory()) {
                currentController.onContextMenuClosed(folder);
            }
        }
        currentController.setClosedContextMenu(true);
    }
}
