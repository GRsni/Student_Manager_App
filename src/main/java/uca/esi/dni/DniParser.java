package uca.esi.dni;

import processing.core.PApplet;
import processing.core.PImage;
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

    private static PImage icon;

    public static void main(String[] args) {
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    public void settings() {
        size(displayWidth / 2, displayHeight / 2);
        icon = loadImage("data/icons/server-storage_filled.png");
    }

    public void setup() {
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        surface.setResizable(false);
        surface.setIcon(icon);
        registerMethod("mouseEvent", this);
        registerMethod("keyEvent", this);

        AppLogger.setup();
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("[General information]: Started app.");
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
            System.err.println("[Error while selecting input file]: No file selected.");
            LOGGER.warning("[Error while selecting input file]: No file selected.");
            currentController.addWarning("Archivo no seleccionado.", 150, false);
        } else {
            String filePath = selection.getAbsolutePath();
            LOGGER.info("[General information]: File selected: " + filePath);
            if (UtilParser.checkFileExtension(filePath, "csv")) {
                currentController.onContextMenuClosed(selection);
            } else {
                System.err.println("[Error in selected file]: File selected is not a CSV file.");
                LOGGER.warning("[Error in selected file]: File selected is not a CSV file.");
            }
        }
        currentController.setClosedContextMenu(true);
    }

    public void selectOutputFolder(File folder) {
        if (folder == null) {
            System.err.println("[Error while selecting output folder]: No folder selected.");
            LOGGER.warning("[Error while selecting output folder]: No folder selected.");
            currentController.addWarning("Carpeta no seleccionada.", 150, false);
        } else {
            String folderPath = folder.getAbsolutePath();
            LOGGER.info("[General information]: File selected: " + folderPath);
            if (folder.isDirectory()) {
                currentController.onContextMenuClosed(folder);
            } else {
                System.err.println("[Error in selected directory]: Path selected is not a directory.");
                LOGGER.warning("[Error in selected directory]: Path selected is not a directory.");
            }
        }
        currentController.setClosedContextMenu(true);
    }

    public void stop() {
        LOGGER.info("[General information]: Closing app");
    }
}
