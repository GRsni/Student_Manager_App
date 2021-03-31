package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import uca.esi.dni.views.View;

public class Button extends BaseElement {

    private final int cornerR;
    private boolean active;

    private PImage icon;
    private boolean isIconLoaded = false;

    private int color=View.COLORS.PRIMARY;
    private int clickColor=View.COLORS.PRIMARY_DARK;
    private int hoverColor=View.COLORS.ACCENT_DARK;

    private final TextField content;
    private int iconSize = 0;

    public Button(PApplet parent, Rectangle rectangle, int corner, String content, boolean active) {
        super(parent, rectangle);
        this.content = new TextField(parent, new Rectangle(rectangle.x + w * .1f, rectangle.y + h * .1f, w / 10 * 8, h / 10 * 8), content, "");
        this.cornerR = corner;
        this.active = active;
        this.content.setContentColor(View.COLORS.WHITE);
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
        content.setW(w / 10 * 7);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
    }

    public int getTextColor() {
        return content.getContentColor();
    }

    public void setTextColor(int textColor) {
        this.content.setContentColor(textColor);
    }

    public int getClickColor() {
        return clickColor;
    }

    public void setClickColor(int clickColor) {
        this.clickColor = clickColor;
    }

    @Override
    public PFont getFont() {
        return content.getFont();
    }

    @Override
    public void setFont(PFont font) {
        this.content.setFont(font);
    }

    @Override
    public int getFontSize() {
        return content.getFontSize();
    }

    @Override
    public void setFontSize(int fontSize) {
        this.content.setFontSize(fontSize);
    }

    public int getContentBackgroundColor() {
        return content.getBackgroundColor();
    }

    public void setContentBackgroundColor(int color) {
        content.setBackgroundColor(color);
    }

    public boolean getIsCentered() {
        return content.isCentered();
    }

    public void setCentered(boolean centered) {
        content.setCentered(centered);
    }

    public void display() {
        renderButton();
        renderIcon();
        content.display();
    }

    private void renderButton() {
        parent.push();
        parent.noStroke();
        if (clicked) {
            parent.fill(clickColor);
        } else {
            if (hover) {
                parent.fill(hoverColor);
            } else {
                parent.fill(color);
            }
        }
        parent.rect(pos.x, pos.y, w, h, cornerR);
        parent.pop();
    }

    private void renderIcon() {
        if (isIconLoaded) {
            parent.image(icon, pos.x + (int) (h * .2), pos.y + h / 2.0f - iconSize / 2.0f);
        }
    }

}
