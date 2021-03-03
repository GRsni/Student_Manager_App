package uca.esi.dni.views;

import processing.core.PApplet;


public abstract class View {

    protected PApplet parent;
    protected int widthUnitSize;
    protected int heightUnitSize;

    protected View(PApplet parent) {
        this.parent = parent;
        this.widthUnitSize = parent.width / 16;
        this.heightUnitSize = parent.height / 16;
    }

    protected View() {
    }

    public abstract void update();

    public abstract void show();

    public abstract void handleInput(processing.event.MouseEvent e);

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
