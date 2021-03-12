package uca.esi.dni;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.file.UtilParser;
import uca.esi.dni.logger.AppLogger;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.View;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DniParser extends PApplet {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

        AppLogger.setup();
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Started app.");

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
            System.out.println("No file selected");
            LOGGER.info("No file selected");
        } else {
            String filePath = selection.getAbsolutePath();
            System.out.println("user selected: " + filePath);
            LOGGER.info("File selected: " + filePath);
            if (UtilParser.checkFileExtension(filePath, "csv")) {
                currentController.onContextMenuClosed(selection);
            } else {
                System.out.println("File selected is not a CSV file");
                LOGGER.info("File selected is not a CSV file");
            }
        }
        currentController.setClosedContextMenu(true);
    }

    public void selectOutputFolder(File folder) {
        if (folder == null) {
            System.out.println("No folder selected");
            LOGGER.info("No folder selected");
        } else {
            String folderPath = folder.getAbsolutePath();
            System.out.println("user selected: " + folderPath);
            LOGGER.info("File selected: " + folderPath);
            if (folder.isDirectory()) {
                currentController.onContextMenuClosed(folder);
            }
        }
        currentController.setClosedContextMenu(true);
    }
}
