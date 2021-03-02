package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import uca.esi.dni.DniParser;
import uca.esi.dni.views.View;

public class Button {
    private final PApplet parent;
    private PVector pos;
    private int w, h;
    private final int cornerR;
    private boolean clicked = false;
    private boolean active = true;

    private PImage icon;
    private boolean isIconLoaded = false;

    private int color;
    private int clickColor;

    private final TextField content;
    private int iconSize = 0;

    public Button(PApplet parent, float x, float y, int w, int h, int corner, String content, boolean active) {
        this.parent = parent;
        this.pos = new PVector(x, y);
        this.content = new TextField(parent, x + w * .1f, y + h * .1f, w * .8f, h * .8f, content);
        this.w = w;
        this.h = h;
        this.cornerR = corner;
        this.active = active;
    }

    public TextField getContent() {
        return content;
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(PVector pos) {
        this.pos = pos;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PImage getIcon() {
        return icon;
    }

    public void setIcon(PImage icon) {
        this.icon = icon;
        this.isIconLoaded = true;
        this.iconSize = h / 2;
        this.icon.resize(iconSize, iconSize); //resizing icon to improve performance
        adjustTextFieldWithIcon(iconSize);
    }

    private void adjustTextFieldWithIcon(int iconSize) {
        PVector textOffset = new PVector(iconSize, 0);
        content.setPos(content.getPos().x + textOffset.x, content.getPos().y + textOffset.y);
        content.setW(w * .7f);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTextColor() {
        return content.getTextColor();
    }

    public void setTextColor(int textColor) {
        this.content.setTextColor(textColor);
    }

    public int getClickColor() {
        return clickColor;
    }

    public void setClickColor(int clickColor) {
        this.clickColor = clickColor;
    }

    public PFont getFont() {
        return content.getFont();
    }

    public void setFont(PFont font) {
        this.content.setFont(font);
    }

    public int getFontSize() {
        return content.getFontSize();
    }

    public void setFontSize(int fontSize) {
        this.content.setFontSize(fontSize);
    }

    public int getContentBackgroundColor() {
        return content.getBackgroundColor();
    }

    public void setContentBackgroundColor(int color) {
        content.setBackgroundColor(color);
    }

    public void show() {
        renderButton();
        renderIcon();
        content.display();
    }

    private void renderButton() {
        parent.push();
        parent.noStroke();
        if (clicked) {
            parent.fill(View.COLORS.ACCENT);
        } else {
            parent.fill(color);
        }
        parent.rect(pos.x, pos.y, w, h, cornerR);
        parent.pop();
    }

    private void renderIcon() {
        if (isIconLoaded) {
            parent.image(icon, pos.x + (int) (h * .2), pos.y + h / 2 - iconSize / 2);
        }
    }

    public boolean inside(float x, float y) {
        return x > pos.x - w / 2 && x < pos.x + w / 2 && y > pos.y - h / 2 && y < pos.y + h / 2;
    }

    public void isClicked(boolean state) {
        clicked = state;
    }

    public boolean isClicked() {
        return clicked;
    }
}
