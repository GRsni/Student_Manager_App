package uca.esi.dni.views;

import processing.core.PApplet;
import uca.esi.dni.controllers.Controller;

public abstract class View {

    private Controller controller;
    protected PApplet parent;

    protected View(PApplet parent) {
        this.parent = parent;
    }

    protected View() {
    }

    protected void contextInterface() {
        controller.algorithm();
    }

    public abstract void update();

    public static class COLORS {
        public final static int PRIMARY = 0xff008577;
        public final static int PRIMARY_DARK = 0xff003d37;
        public final static int SECONDARY = 0xfff5b000;
        public final static int SECONDARY_DARK = 0xffdcaa2c;
        public final static int ACCENT_DARK = 0xffaaaaaa;
        public final static int ACCENT = 0xffcccccc;
        public final static int WHITE = 0xffffffff;
        public final static int BLACK = 0xff000000;
    }
}
