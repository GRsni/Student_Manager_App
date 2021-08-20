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


/**
 * The type View.
 */
public abstract class View {

    /**
     * The Parent.
     */
    protected PApplet parent;
    /**
     * The constant widthUnitSize.
     */
    protected static int widthUnitSize;
    /**
     * The constant heightUnitSize.
     */
    protected static int heightUnitSize;

    /**
     * The Elements.
     */
    protected final Map<String, BaseElement> elements = new HashMap<>();
    /**
     * The Elements over modal.
     */
    protected final Map<String, BaseElement> elementsOverModal = new HashMap<>();
    /**
     * The Warnings.
     */
    protected final ArrayList<Warning> warnings = new ArrayList<>();

    /**
     * The Background.
     */
    protected PImage background;
    /**
     * The Modal active.
     */
    protected boolean modalActive = false;

    /**
     * The constant loadedFonts.
     */
    private static boolean loadedFonts = false;
    /**
     * The constant fontBig.
     */
    protected static PFont fontBig;
    /**
     * The constant fontSmall.
     */
    protected static PFont fontSmall;

    /**
     * Instantiates a new View.
     *
     * @param parent the parent
     */
    protected View(PApplet parent) {
        this.parent = parent;
    }

    /**
     * Instantiates a new View.
     */
    protected View() {
    }

    /**
     * Gets width unit size.
     *
     * @return the width unit size
     */
    public static int getWidthUnitSize() {
        return widthUnitSize;
    }

    /**
     * Sets width unit size.
     *
     * @param widthUnitSize the width unit size
     */
    public static void setWidthUnitSize(int widthUnitSize) {
        View.widthUnitSize = widthUnitSize;
    }

    /**
     * Gets height unit size.
     *
     * @return the height unit size
     */
    public static int getHeightUnitSize() {
        return heightUnitSize;
    }

    /**
     * Sets height unit size.
     *
     * @param heightUnitSize the height unit size
     */
    public static void setHeightUnitSize(int heightUnitSize) {
        View.heightUnitSize = heightUnitSize;
    }

    /**
     * Load fonts.
     *
     * @param parent the parent
     */
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

    /**
     * On create.
     */
    protected abstract void onCreate();

    /**
     * Reload.
     */
    public void reload() {
        elements.clear();
        elementsOverModal.clear();
        onCreate();
    }

    /**
     * Create elements.
     */
    protected abstract void createElements();

    /**
     * Update.
     *
     * @param dbList    the db list
     * @param modList   the mod list
     * @param inputFile the input file
     * @param surveys   the surveys
     */
    public abstract void update(Set<Student> dbList, Set<Student> modList, File inputFile, List<Survey> surveys);

    /**
     * Show.
     */
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

    /**
     * Generate warning warning.
     *
     * @param parent        the parent
     * @param contentString the content string
     * @param duration      the duration
     * @param type          the type
     * @return the warning
     */
    public static Warning generateWarning(PApplet parent, String contentString, Warning.DURATION duration, Warning.TYPE type) {
        Warning warning = new Warning(parent, new BaseElement.Rectangle(widthUnitSize * 9f, heightUnitSize * 0.5f, widthUnitSize * 6, heightUnitSize),
                contentString, duration, type);
        warning.setFont(fontBig);
        warning.setFontSize(10);
        warning.setContentColor(COLORS.WHITE);
        return warning;
    }

    /**
     * Gets element keys.
     *
     * @return the element keys
     */
    public Set<String> getElementKeys() {
        return elements.keySet();
    }

    /**
     * Gets modal element keys.
     *
     * @return the modal element keys
     */
    public Set<String> getModalElementKeys() {
        return elementsOverModal.keySet();
    }

    /**
     * Gets ui element.
     *
     * @param key the key
     * @return the ui element
     */
    public BaseElement getUIElement(String key) {
        return elements.get(key);
    }

    /**
     * Gets ui modal element.
     *
     * @param key the key
     * @return the ui modal element
     */
    public BaseElement getUIModalElement(String key) {
        return elementsOverModal.get(key);
    }

    /**
     * Gets warnings.
     *
     * @return the warnings
     */
    public List<Warning> getWarnings() {
        return warnings;
    }

    /**
     * Is modal active boolean.
     *
     * @return the boolean
     */
    public boolean isModalActive() {
        return modalActive;
    }

    /**
     * Sets modal active.
     *
     * @param modalActive the modal active
     */
    public void setModalActive(boolean modalActive) {
        this.modalActive = modalActive;
    }

    /**
     * The type Colors.
     */
    public static class COLORS {
        /**
         * The constant PRIMARY.
         */
        public static final int PRIMARY = 0xff008577;
        /**
         * The constant PRIMARY_DARK.
         */
        public static final int PRIMARY_DARK = 0xff00665c;
        /**
         * The constant SECONDARY.
         */
        public static final int SECONDARY = 0xfff5b000;
        /**
         * The constant SECONDARY_DARK.
         */
        public static final int SECONDARY_DARK = 0xff9a7519;
        /**
         * The constant ACCENT_DARK.
         */
        public static final int ACCENT_DARK = 0xffaaaaaa;
        /**
         * The constant ACCENT.
         */
        public static final int ACCENT = 0xffcccccc;
        /**
         * The constant ACCENT_LIGHT.
         */
        public static final int ACCENT_LIGHT = 0xffdddddd;
        /**
         * The constant WHITE.
         */
        public static final int WHITE = 0xffffffff;
        /**
         * The constant BLACK.
         */
        public static final int BLACK = 0xff000000;
        /**
         * The constant RED.
         */
        public static final int RED = 0xffdd0000;
        /**
         * The constant RED_DARK.
         */
        public static final int RED_DARK = 0xff8a0000;

        /**
         * Instantiates a new Colors.
         */
        private COLORS() {
        }
    }
}
