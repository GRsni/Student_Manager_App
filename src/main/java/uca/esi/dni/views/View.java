package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.*;


public abstract class View {

    protected PApplet parent;
    protected static int widthUnitSize;
    protected static int heightUnitSize;

    protected final Map<String, BaseElement> elements = new HashMap<>();
    protected final Map<String, BaseElement> elementsOverModal = new HashMap<>();
    protected final ArrayList<Warning> warnings = new ArrayList<>();

    protected PImage background;
    protected boolean modalActive = false;

    private static boolean loadedFonts = false;
    protected static PFont fontBig;
    protected static PFont fontSmall;

    protected View(PApplet parent) {
        this.parent = parent;
    }

    protected View() {
    }

    public static int getWidthUnitSize() {
        return widthUnitSize;
    }

    public static void setWidthUnitSize(int widthUnitSize) {
        View.widthUnitSize = widthUnitSize;
    }

    public static int getHeightUnitSize() {
        return heightUnitSize;
    }

    public static void setHeightUnitSize(int heightUnitSize) {
        View.heightUnitSize = heightUnitSize;
    }

    public static void loadFonts(PApplet parent) {
        if (!loadedFonts) {
            if (parent.width > 1024) {
                fontBig = parent.loadFont("data/fonts/Calibri-30.vlw");
                fontSmall = parent.loadFont("data/fonts/Calibri-14.vlw");
            } else {
                fontBig = parent.loadFont("data/fonts/Calibri-26.vlw");
                fontSmall = parent.loadFont("data/fonts/Calibri-12.vlw");
            }

            loadedFonts = true;
        }
    }

    protected abstract void onCreate();

    public void reload() {
        elements.clear();
        elementsOverModal.clear();
        onCreate();
    }

    protected abstract void createElements();

    public abstract void update(Set<Student> dbList, Set<Student> modList, File inputFile, List<Survey> surveys);

    public void show() {
        parent.image(background, 0, 0);
        for (BaseElement element : elements.values()) {
            if (element.isVisible()) {
                element.display();
            }
        }
        for (BaseElement element : elementsOverModal.values()) {
            if (element.isVisible()) {
                element.display();
            }
        }
        if (!isModalActive() && !warnings.isEmpty()) {
            warnings.get(0).display();
            warnings.get(0).update();
            if (warnings.get(0).toDestroy()) {
                warnings.remove(0);
            }
        }
    }

    public static Warning generateWarning(PApplet parent, String contentString, Warning.DURATION duration, Warning.TYPE type) {
        Warning warning = new Warning(parent, new BaseElement.Rectangle(widthUnitSize * 9f, heightUnitSize * 0.5f, widthUnitSize * 6, heightUnitSize),
                contentString, duration, type);
        warning.setFont(fontBig);
        warning.setFontSize(10);
        warning.setContentColor(COLORS.WHITE);
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

    public List<Warning> getWarnings() {
        return warnings;
    }

    public boolean isModalActive() {
        return modalActive;
    }

    public void setModalActive(boolean modalActive) {
        this.modalActive = modalActive;
    }

    public static class COLORS {
        public static final int PRIMARY = 0xff008577;
        public static final int PRIMARY_DARK = 0xff00665c;
        public static final int SECONDARY = 0xfff5b000;
        public static final int SECONDARY_DARK = 0xff9a7519;
        public static final int ACCENT_DARK = 0xffaaaaaa;
        public static final int ACCENT = 0xffcccccc;
        public static final int ACCENT_LIGHT = 0xffdddddd;
        public static final int WHITE = 0xffffffff;
        public static final int BLACK = 0xff000000;
        public static final int RED = 0xffdd0000;
        public static final int RED_DARK = 0xff8a0000;

        private COLORS() {
        }
    }
}
