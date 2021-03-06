package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import uca.esi.dni.views.View;

public class TextField extends BaseElement {

    private int backgroundColor;
    private boolean hasBackground = false;
    private int textColor = View.COLORS.BLACK;
    private String content;
    private int padding = 4;
    private boolean isClickable = false;

    private boolean hasShadow = false;

    public TextField(PApplet parent, float x, float y, int w, int h, String content) {
        super(parent, new PVector(x, y), w, h);
        this.content = content;
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(float x, float y) {
        this.pos = new PVector(x, y);
    }

    public float getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        hasBackground = true;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public void display() {
        parent.push();
        if (hasBackground) {
            parent.noStroke();
            parent.fill(backgroundColor);
            parent.rect(pos.x, pos.y, w, h);
        }
        parent.textSize(fontSize);
        parent.textFont(font);
        parent.fill(textColor);
        parent.textAlign(PConstants.LEFT, PConstants.CENTER);
        parent.text(content, pos.x + padding, pos.y, w, h);
        parent.pop();
    }
}
