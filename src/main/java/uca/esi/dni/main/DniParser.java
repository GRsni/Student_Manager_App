package uca.esi.dni.main;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
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
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static final String DATA_BACKUP_FILEPATH = "data/files/student_data_backup.json";
    public static final String SETTINGS_FILEPATH = "data/files/settings.json";

    private AppModel appModel;
    private View currentView;
    private Controller currentController;

    private PImage icon;

    private int w;
    private int h;

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    @Override
    public void settings() {
        PVector windowSize = calcWindowSize();
        size((int) windowSize.x, (int) windowSize.y);
        w = width;
        h = height;
        icon = loadImage("data/icons/server-storage_filled.png");
    }

    private PVector calcWindowSize() {
        float screenWidth = Math.max(1024, displayWidth / 2);
        float screenHeight = screenWidth * 9 / 16;
        return new PVector(screenWidth, screenHeight);
    }

    @Override
    public void setup() {
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        surface.setResizable(true);
        surface.setIcon(icon);
        registerMethods();

        AppLogger.setup();
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("[General information]: Started app.");
        initMVCObjects();
        setupFileSystem();
    }

    private void registerMethods() {
        registerMethod("mouseEvent", this);
        registerMethod("keyEvent", this);
        registerMethod("pre", this);
    }

    private void initMVCObjects() {
        appModel = new AppModel();
        initViewConstants();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
    }

    private void initViewConstants() {
        View.setWidthUnitSize(width / 16);
        View.setHeightUnitSize(height / 16);
        View.loadFonts(this);
    }

    private void setupFileSystem() {
        File studentData = new File(DATA_BACKUP_FILEPATH);
        if (!studentData.exists()) {
            saveJSONObject(new JSONObject(), DATA_BACKUP_FILEPATH);
        }
    }

    public AppModel getAppModel() {
        return appModel;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public Controller getCurrentController() {
        return currentController;
    }

    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    @Override
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

    public void pre() {
        if (w != width || h != height) {
            // Sketch window has resized
            w = width;
            h = height;
            //Change View size constants
            initViewConstants();
            currentView.reload();
            currentController.controllerLogic();
        }
    }

    public void selectInputFile(File selection) {
        if (selection == null) {

            LOGGER.warning("[Error while selecting input file]: No file selected.");
            currentController.addWarning("Archivo no seleccionado.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
        } else {
            String filePath = selection.getAbsolutePath();
            if (Util.checkFileExtension(filePath, "csv")) {
                currentController.onContextMenuClosed(selection);
            } else {
                LOGGER.warning("[Error in selected file]: File selected is not a CSV file.");
            }
        }
        currentController.setClosedContextMenu(true);
    }

    public void selectOutputFolder(File folder) {
        if (folder == null) {
            LOGGER.warning("[Error while selecting output folder]: No folder selected.");
            currentController.addWarning("Carpeta no seleccionada.", Warning.DURATION.SHORT, Warning.TYPE.WARNING);
            currentController.onContextMenuClosed(new File(""));
        } else {
            if (folder.isDirectory()) {
                currentController.onContextMenuClosed(folder);
            } else {
                LOGGER.warning("[Error in selected directory]: Path selected is not a directory.");
            }
        }
        currentController.setClosedContextMenu(true);
    }

    @Override
    public void exit() {
        currentController.addWarning("Cerrando aplicación.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
        if (appModel.isStudentDataModified() && !appModel.getDbStudents().isEmpty()) {
            EmailHandler.sendBackupEmail(DATA_BACKUP_FILEPATH);
        }
        LOGGER.info("[General information]: Closing app");
        super.exit();
    }
}
