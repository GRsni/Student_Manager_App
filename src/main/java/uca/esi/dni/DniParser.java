package uca.esi.dni;

import processing.core.PApplet;
import uca.esi.dni.controllers.Controller;
import uca.esi.dni.controllers.MainController;
import uca.esi.dni.file.FileManager;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.models.Model;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.View;

import processing.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class DniParser extends PApplet {

    private Model appModel;
    private View currentView;
    private Controller currentController;

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
        this.registerMethod("mouseEvent", this);
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        manager = new FileManager(this);
        surface.setResizable(true);
        appModel = new AppModel();
        currentView = new MainView(this);
        currentController = new MainController(appModel, currentView);
    }

    public void draw() {
        background(255);
        /*image(background, 0, 0, width, height);
        drawMenu();

        showWarnings();
        updateWarnings();
        fill(0);
        text(mouseX + " : " + mouseY, mouseX, mouseY);
        text(width + " : " + height, 100, 50);*/

        currentView.show();

    }

    private void drawMenu() {
        push();
        fill(0);
        textAlign(CENTER, TOP);
        textSize(30);
        text("Aplicacion auxiliar", width / 2f, 30);
        pop();

    }

    public void addNewWarning(String content, int fadeout) {
        warnings.add(new Warning(this, content, fadeout));
    }

    private void showWarnings() {
        if (warnings.size() > 0) {
            warnings.get(0).show();
        }
    }

    private void updateWarnings() {
        if (warnings.size() > 0) {
            Warning a = warnings.get(0);
            a.update();
            if (a.toDestroy()) {
                warnings.remove(a);
            }
        }
    }

    public void mouseEvent(processing.event.MouseEvent e) {
        currentView.handleInput(e);
    }

    public void selectTextFile(File selection) {
        if (selection == null) {
            println("No file selected");
            addNewWarning("Archivo no seleccionado.", 100);
        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (checkFileExtension(filePath, ".txt")) {
                // textHandler.setFileName(selection.getName());
                manager.setParserTextFile(selection);
            } else {
                addNewWarning("Error al leer el archivo de texto.", 150);
            }
        }
    }

    public void selectJSONFile(File selection) {
        if (selection == null) {
            println("No file selected");
            addNewWarning("Archivo no seleccionado.", 100);
        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (checkFileExtension(filePath, ".json")) {
                // JSONHandler.setFileName(selection.getName());
                manager.setParserJSONFile(selection);
            } else {
                addNewWarning("Error al leer el archivo .json de la base de datos.", 150);
            }
        }
    }

    public void selectOutputFolder(File folder) {
        if (folder == null) {
            println("No folder selected");
            addNewWarning("Carpeta no seleccionada.", 100);
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
