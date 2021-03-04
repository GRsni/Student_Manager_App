package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

public abstract class BaseElement {

    protected final PApplet parent;
    protected PVector pos;
    protected int w, h;

    protected boolean clicked = false;
    protected boolean hover = false;

    protected PFont font;
    protected int fontSize = 10;

    public BaseElement(PApplet parent, PVector pos, int w, int h) {
        this.parent = parent;
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public abstract void display();

    public boolean inside(int x, int y) {
        return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void isClicked(boolean val) {
        clicked = val;
    }

    public boolean isHover() {
        return hover;
    }

    public void isHover(boolean val) {
        hover = val;
    }

    public void handleInput(MouseEvent e) {
    }

}
