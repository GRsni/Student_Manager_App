package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import uca.esi.dni.views.View;

public class TextField extends BaseElement {

    private int backgroundColor;
    private boolean hasBackground = false;
    private int contentColor = View.COLORS.BLACK;
    private int hintColor = View.COLORS.ACCENT_DARK;
    private String content;
    private final String hint;
    private final int padding = 4;
    private boolean isClickable = false;
    private boolean isFocused = false;
    private boolean isHeader = false;

    private boolean hasShadow = false;

    public TextField(PApplet parent, float x, float y, int w, int h, String content, String hint) {
        super(parent, new PVector(x, y), w, h);
        this.content = content;
        this.hint = hint;
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

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
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

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean header) {
        isHeader = header;
    }

    public void display() {
        parent.push();
        renderBackground();
        renderContent();
        renderCursor();
        parent.pop();
    }

    private void renderBackground() {
        parent.push();
        if (hasBackground) {
            parent.noStroke();
            parent.fill(backgroundColor);
            if (isHeader) {
                parent.rect(pos.x, pos.y, w, h, 4, 4, 0, 0);
            } else {
                parent.rect(pos.x, pos.y, w, h);
            }
        }
        parent.pop();
    }

    private void renderContent() {
        parent.push();
        parent.textSize(fontSize);
        parent.textFont(font);
        parent.textAlign(PConstants.LEFT, PConstants.CENTER);
        if (!content.isEmpty() || isFocused) {
            parent.fill(contentColor);
            parent.text(content, pos.x + padding, pos.y, w, h);
        } else {
            parent.fill(hintColor);
            parent.text(hint, pos.x + padding, pos.y, w, h);
        }
        parent.pop();
    }

    private void renderCursor() {
        parent.push();
        if (isFocused) {
            if ((parent.frameCount >> 5 & 1) == 0) {

                parent.stroke(View.COLORS.BLACK);
                parent.strokeWeight(3);
                parent.strokeCap(PConstants.SQUARE);
                parent.textFont(font);
                float xOffset = pos.x + padding + parent.textWidth(content);
                parent.line(xOffset, pos.y + padding, xOffset, pos.y + h - padding);
            }
        }
        parent.pop();
    }


    public void addCharToContent(char k) {
        content += k;
    }

    public void removeCharacter() {
        content = content.substring(0, Math.max(0, content.length() - 1));
    }

    public void modifyCounter(int number) {
        modifyCounter(Integer.toString(number));
    }

    public void modifyCounter(float number) {
        modifyCounter(Float.toString(number));
    }

    public void modifyCounter(String end) {
        content = content.substring(0, content.lastIndexOf(':') + 2) + end;
    }
}
