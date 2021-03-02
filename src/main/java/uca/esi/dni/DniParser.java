package uca.esi.dni;

import org.checkerframework.checker.units.qual.A;
import processing.core.*;
import uca.esi.dni.file.FileManager;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.ui.Button;
import uca.esi.dni.ui.InputButton;
import uca.esi.dni.views.MainView;

import java.io.File;
import java.util.ArrayList;

public class DniParser extends PApplet {

    public static PFont font_small, font_big;
    private InputButton textHandler, JSONHandler, folderHandler;

    private static PImage DBIcon;
    private static PImage txtIcon;
    private static PImage ExcelIcon;
    private static PImage graphIcon;
    private static PImage background;

    private AppModel appModel;
    private MainView mainView;

    private static Button createNewFile, addStudents, extractDataFile, exitButton;
    public static FileManager manager;
    private static final ArrayList<Warning> warnings = new ArrayList<>();

    public static void main(String[] args) {
        PApplet.main(new String[]{DniParser.class.getName()});
    }

    public void settings() {
        size(displayWidth / 2, displayHeight / 2);
        // size(300, 200);
        preloadImages();
    }

    public void setup() {
        surface.setTitle("Manual de laboratorio: Gestor de datos");
        manager = new FileManager(this);
        initFonts();
        initButtons();
        surface.setResizable(true);
        appModel = new AppModel();
        mainView = new MainView(appModel, this);
    }

    private void initButtons() {
        textHandler = new InputButton(this, new PVector(30, 350), "", "Archivo .txt");
        JSONHandler = new InputButton(this, new PVector(30, 400), "", "Archivo .json");
        folderHandler = new InputButton(this, new PVector(30, 450), "", "Carpeta");

    }

    private void initFonts() {
        if (fontExists("data/Calibri-14.vlw")) {
            font_small = loadFont("data/Calibri-14.vlw");
        }
        if (fontExists("data/Calibri-30.vlw")) {
            font_big = loadFont("data/Calibri-30.vlw");
        }
    }

    private void preloadImages() {
        DBIcon = loadImage("data/icons/server-storage.png");
        txtIcon = loadImage("data/icons/txt-file-symbol.png");
        ExcelIcon = loadImage("data/icons/excel.png");
        graphIcon = loadImage("data/icons/statistics.png");
        background = loadImage("data/background/main_back.png");
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

        mainView.show();

    }

    private void drawMenu() {
        push();
        fill(0);
        textAlign(CENTER, TOP);
        textSize(30);
        textFont(font_big);
        text("Aplicacion auxiliar", width / 2f, 30);
        pop();

        drawButtons();
    }

    private void drawButtons() {
        textHandler.show();
        JSONHandler.show();
        folderHandler.show();

        createNewFile.show();
        addStudents.show();
        extractDataFile.show();
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

    public void mouseClicked() {

    }

    public void mousePressed() {

    }

    public void mouseReleased() {

    }

    public void selectTextFile(File selection) {
        if (selection == null) {
            println("No file selected");
            addNewWarning("Archivo no seleccionado.", 100);
        } else {
            String filePath = selection.getAbsolutePath();
            println("user selected: " + filePath);
            if (checkFileExtension(filePath, ".txt")) {
                textHandler.setFileName(selection.getName());
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
                JSONHandler.setFileName(selection.getName());
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
                folderHandler.setFileName(folder.getName());
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
