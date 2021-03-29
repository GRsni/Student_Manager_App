package uca.esi.dni.main;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.file.EmailHandler;
import uca.esi.dni.file.Util;
import uca.esi.dni.logger.AppLogger;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.View;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DniParser extends PApplet {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public final static String DATA_BACKUP_FILEPATH = "data/files/student_data_backup.json";
    public final static String SETTINGS_FILEPATH = "data/files/settings.json";

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
        setupFileSystem();
    }

    private void initMVCObjects() {
        appModel = new AppModel();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
    }

    private void setupFileSystem() {
        File studentData = new File(DATA_BACKUP_FILEPATH);
        if (!studentData.exists()) {
            saveJSONObject(new JSONObject(), DATA_BACKUP_FILEPATH);
        }
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
            currentController.addWarning("Archivo no seleccionado.", Warning.DURATION.SHORT, false);
        } else {
            String filePath = selection.getAbsolutePath();
            LOGGER.info("[General information]: File selected: " + filePath);
            if (Util.checkFileExtension(filePath, "csv")) {
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
            currentController.addWarning("Carpeta no seleccionada.", Warning.DURATION.SHORT, false);
            currentController.onContextMenuClosed(new File(""));
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

    public void exit() {
        currentController.addWarning("Cerrando aplicación.", Warning.DURATION.SHORT, true);
        if (appModel.getDBStudents().size() > 0) {
            EmailHandler.sendBackupEmail(DATA_BACKUP_FILEPATH);
        }
        LOGGER.info("[General information]: Closing app");
        super.exit();
    }
}
