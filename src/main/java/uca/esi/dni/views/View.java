package uca.esi.dni.views;

import processing.core.PApplet;
import processing.core.PImage;
import uca.esi.dni.data.Student;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;


public abstract class View {

    protected PApplet parent;
    public static int WIDTH_UNIT_SIZE;
    public static int HEIGHT_UNIT_SIZE;

    protected final ArrayList<BaseElement> elements = new ArrayList<>();

    protected PImage background;

    protected View(PApplet parent) {
        this.parent = parent;
        WIDTH_UNIT_SIZE = parent.width / 16;
        HEIGHT_UNIT_SIZE = parent.height / 16;
    }

    protected View() {
    }

    protected abstract void onCreate();

    protected abstract void createElements();

    public abstract void update(Set<Student> dbList, Set<Student> modList,
                                File inputFile, String dbReference, ArrayList<Warning> warnings);

    public void show() {
        parent.image(background, 0, 0);
        for (BaseElement element : elements) {
            element.display();
        }
    }

    public int getElementsSize() {
        return elements.size();
    }

    public BaseElement getUIElement(int index) {
        return elements.get(index);
    }

    public static class COLORS {
        public final static int PRIMARY = 0xff008577;
        public final static int PRIMARY_DARK = 0xff00665c;
        public final static int SECONDARY = 0xfff5b000;
        public final static int SECONDARY_DARK = 0xffdcaa2c;
        public final static int ACCENT_DARK = 0xffaaaaaa;
        public final static int ACCENT = 0xffcccccc;
        public final static int WHITE = 0xffffffff;
        public final static int BLACK = 0xff000000;
    }
}
