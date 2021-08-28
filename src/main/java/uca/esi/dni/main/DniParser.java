package uca.esi.dni.main;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.handlers.json.JSONHandler;
import uca.esi.dni.handlers.Util;
import uca.esi.dni.logger.AppLogger;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Dni parser.
 */
public class DniParser extends PApplet {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The constant DATA_BACKUP_FILEPATH.
     */
    public static final String DATA_BACKUP_FILEPATH = "data/files/student_data_backup.json";
    /**
     * The constant SETTINGS_FILEPATH.
     */
    public static final String SETTINGS_FILEPATH = "data/files/settings.json";
    /**
     * The constant PROPERTIES_FILEPATH.
     */
    public static final String PROPERTIES_FILEPATH = "data/files/app.properties";

    /**
     * The App model.
     */
    private AppModel appModel;
    /**
     * The Current view.
     */
    private View currentView;
    /**
     * The Current controller.
     */
    private Controller currentController;

    /**
     * The Icon.
     */
    private PImage icon;

    /**
     * The W.
     */
    private int w;
    /**
     * The H.
     */
    private int h;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    /**
     * Sets .
     */
    @Override
    public void settings() {
        PVector windowSize = calcWindowSize();
        size((int) windowSize.x, (int) windowSize.y);
        w = width;
        h = height;
        icon = loadImage("data/icons/server-storage_filled.png");
    }

    /**
     * Calc window size p vector.
     *
     * @return the p vector
     */
    private PVector calcWindowSize() {
        float screenWidth = Math.max(1024, displayWidth / 2);
        float screenHeight = screenWidth * 9 / 16;
        return new PVector(screenWidth, screenHeight);
    }

    /**
     * Sets .
     */
    @Override
    public void setup() {
        AppLogger.setup();
        LOGGER.setLevel(Level.INFO);

        surface.setTitle("Manual de laboratorio: Gestor de usuarios v" + getAppVersion());
        surface.setResizable(true);
        surface.setIcon(icon);
        registerMethods();


        LOGGER.info("[General information]: Started app.");
        initMVCObjects();
        setupFileSystem();
    }

    /**
     * Gets app version.
     *
     * @return the app version
     */
    private String getAppVersion() {
        Properties properties = new Properties();
        String version = "";
        try {
            properties.load(Util.class.getClassLoader().getResourceAsStream(PROPERTIES_FILEPATH));
            version = properties.getProperty("version");
        } catch (IOException e) {
            LOGGER.warning("[Error loading app version]: " + e.getMessage());
        }
        return version;
    }

    /**
     * Register methods.
     */
    private void registerMethods() {
        registerMethod("mouseEvent", this);
        registerMethod("keyEvent", this);
        registerMethod("pre", this);
    }

    /**
     * Init mvc objects.
     */
    private void initMVCObjects() {
        appModel = new AppModel(JSONHandler.loadJSONObject(SETTINGS_FILEPATH));
        initViewConstants();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
    }

    /**
     * Init view constants.
     */
    private void initViewConstants() {
        View.setWidthUnitSize(width / 16);
        View.setHeightUnitSize(height / 16);
        View.loadFonts(this);
    }

    /**
     * Sets file system.
     */
    private void setupFileSystem() {
        File studentData = new File(DATA_BACKUP_FILEPATH);
        if (!studentData.exists()) {
            saveJSONObject(new JSONObject(), DATA_BACKUP_FILEPATH);
        }
    }

    /**
     * Gets app model.
     *
     * @return the app model
     */
    public AppModel getAppModel() {
        return appModel;
    }

    /**
     * Sets current view.
     *
     * @param currentView the current view
     */
    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    /**
     * Gets current controller.
     *
     * @return the current controller
     */
    public Controller getCurrentController() {
        return currentController;
    }

    /**
     * Sets current controller.
     *
     * @param currentController the current controller
     */
    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    /**
     * Draw.
     */
    @Override
    public void draw() {
        background(255);
        currentView.show();
    }

    /**
     * Mouse event.
     *
     * @param e the event
     */
    public void mouseEvent(MouseEvent e) {
        currentController.handleMouseEvent(e);
    }

    /**
     * Key event.
     *
     * @param e the event
     */
    public void keyEvent(KeyEvent e) {
        currentController.handleKeyEvent(e);
    }

    /**
     * Pre.
     */
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

    /**
     * Select input file.
     *
     * @param selection the selection
     */
    public void selectInputFile(File selection) {
        if (selection == null) {
            currentController.onContextMenuClosed(new File(""));
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

    /**
     * Select output folder.
     *
     * @param folder the folder
     */
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

    /**
     * Exit.
     */
    @Override
    public void exit() {
        currentController.onAppClosing();
        super.exit();
    }
}
