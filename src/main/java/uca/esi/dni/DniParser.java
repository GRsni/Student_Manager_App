package uca.esi.dni;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.file.UtilParser;
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
        registerMethod("keyEvent", this);
        initMVCObjects();
    }

    private void initMVCObjects() {
        appModel = new AppModel();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
    }

    public void draw() {
        background(255);
        currentView.show();
    }

    public void mouseEvent(MouseEvent e) {
        currentController.handleMouseEvent(e);
    }

    public void keyEvent(KeyEvent e) {
        currentController.handleKeyEvent(e);
    }

    public void selectInputFile(File selection) {
        if (selection == null) {
            println("No file selected");
        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (UtilParser.checkFileExtension(filePath, "txt") || UtilParser.checkFileExtension(filePath, "csv")) {
                currentController.onContextMenuClosed(selection);
            }
        }
        currentController.setClosedContextMenu(true);
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
