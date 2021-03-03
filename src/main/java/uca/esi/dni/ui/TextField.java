package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;

public class TextField {
    private final PApplet parent;

    private PVector pos;
    private float w;
    private float h;

    private int backgroundColor;
    private boolean hasBackground = false;
    private int textColor;
    private PFont font;
    private int fontSize = 10;
    private String content;
    private int padding = 4;

    private boolean hasShadow = false;

    public TextField(PApplet parent, float x, float y, float w, float h, String content) {
        this.parent = parent;
        this.pos = new PVector(x, y);
        this.w = w;
        this.h = h;
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

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
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
