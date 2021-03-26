package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import uca.esi.dni.data.Student;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class View {

    protected PApplet parent;
    public static int WIDTH_UNIT_SIZE;
    public static int HEIGHT_UNIT_SIZE;

    protected final Map<String, BaseElement> elements = new HashMap<>();
    protected final Map<String, BaseElement> elementsOverModal = new HashMap<>();
    protected final ArrayList<Warning> warnings = new ArrayList<>();

    protected PImage background;
    protected boolean modalActive = false;

    private static boolean loadedFonts = false;
    protected static PFont FONT_BIG;
    protected static PFont FONT_SMALL;

    protected View(PApplet parent) {
        this.parent = parent;
        WIDTH_UNIT_SIZE = parent.width / 16;
        HEIGHT_UNIT_SIZE = parent.height / 16;
        if (!loadedFonts) {
            FONT_BIG = parent.loadFont("data/fonts/Calibri-30.vlw");
            FONT_SMALL = parent.loadFont("data/fonts/Calibri-14.vlw");
            loadedFonts = true;
        }
    }

    protected View() {
    }

    protected abstract void onCreate();

    protected abstract void createElements();

    public abstract void update(Set<Student> dbList, Set<Student> modList, File inputFile, String dbReference);

    public void show() {
        parent.image(background, 0, 0);
        for (String s : elements.keySet()) {
            if (elements.get(s).isVisible()) {
                elements.get(s).display();
            }
        }
        for (String s : elementsOverModal.keySet()) {
            if (elementsOverModal.get(s).isVisible()) {
                elementsOverModal.get(s).display();
            }
        }
        if (!isModalActive()) {
            if (warnings.size() > 0) {
                warnings.get(0).display();
                warnings.get(0).update();
                if (warnings.get(0).toDestroy()) {
                    warnings.remove(0);
                }

            }
        }
    }

    public static Warning generateWarning(PApplet parent, String contentString, int fadeout, boolean isGood) {
        Warning warning = new Warning(parent, WIDTH_UNIT_SIZE * 9, HEIGHT_UNIT_SIZE * 0.5f, WIDTH_UNIT_SIZE * 6, HEIGHT_UNIT_SIZE, contentString, fadeout, isGood);
        warning.setFont(FONT_BIG);
        warning.setFontSize(10);
        warning.setFontColor(COLORS.WHITE);
        return warning;
    }

    public Set<String> getElementKeys() {
        return elements.keySet();
    }

    public Set<String> getModalElementKeys() {
        return elementsOverModal.keySet();
    }

    public BaseElement getUIElement(String key) {
        return elements.get(key);
    }

    public BaseElement getUIModalElement(String key) {
        return elementsOverModal.get(key);
    }

    public ArrayList<Warning> getWarnings() {
        return warnings;
    }

    public boolean isModalActive() {
        return modalActive;
    }

    public void setModalActive(boolean modalActive) {
        this.modalActive = modalActive;
    }

    public static class COLORS {
        public final static int PRIMARY = 0xff008577;
        public final static int PRIMARY_DARK = 0xff00665c;
        public final static int SECONDARY = 0xfff5b000;
        public final static int SECONDARY_DARK = 0xffdba724;
        public final static int ACCENT_DARK = 0xffaaaaaa;
        public final static int ACCENT = 0xffcccccc;
        public final static int WHITE = 0xffffffff;
        public final static int BLACK = 0xff000000;
    }
}
