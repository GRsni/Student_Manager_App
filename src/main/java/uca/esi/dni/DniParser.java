package uca.esi.dni;

import processing.core.PApplet;
import processing.event.MouseEvent;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.EditController;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.controllers.StatsController;
import uca.esi.dni.file.FileManager;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.models.Model;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import java.io.File;
import java.util.ArrayList;

public class DniParser extends PApplet {

    public static Model appModel;
    public static View currentView;
    public static Controller currentController;

    public static FileManager manager;
    private static final ArrayList<Warning> warnings = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    public void settings() {
        size(displayWidth / 2, displayHeight / 2);
        // size(300, 200);
    }

    public void setup() {
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        manager = new FileManager(this);
        surface.setResizable(false);
        registerMethod("mouseEvent", this);
        initState();
    }

    public void initState() {
        appModel = new AppModel();
        currentView = new MainView(this);
        currentController = new MainController(this, appModel, currentView);
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
            if (checkFileExtension(filePath, ".txt")) {
                // textHandler.setFileName(selection.getName());
                manager.setParserTextFile(selection);
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
            if (checkFileExtension(filePath, ".json")) {
                // JSONHandler.setFileName(selection.getName());
                manager.setParserJSONFile(selection);
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
                manager.setOutputFolder(folder);
                //folderHandler.setFileName(folder.getName());
            }
        }
    }

    private boolean checkFileExtension(String file, String ext) {
        int lastIndex = file.lastIndexOf(".");
        if (lastIndex == -1) {
            return false;
        } else {
            String extension = file.substring(lastIndex);
            return extension.equals(ext);
        }
    }

    public static boolean fontExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }
}
