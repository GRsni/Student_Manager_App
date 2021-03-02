package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;

public class ItemList {
    private final ArrayList<String> items = new ArrayList<>();
    private PApplet parent;

    private PVector pos;
    private int w, h;
    private int visibleItems = 0;

    private int backgroundColor;
    private int textColor;
    private PFont font;
    private int fontSize;

    private TextField title;

    public ItemList(PApplet parent, float x, float y, int w, int h, String title) {
        this.parent = parent;
        this.pos = new PVector(x, y + parent.height / 16);
        this.w = w;
        this.h = h - parent.height / 16;
        this.visibleItems = calculateNumberOfVisibleItems();
        createTitle(parent, x, y, w, h, title);

    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTitleBackgroundColor(int backgroundColor) {
        this.title.setBackgroundColor(backgroundColor);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.title.setTextColor(textColor);
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
        this.title.setFont(font);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setTitleFontSize(int fontSize) {
        this.title.setFontSize(fontSize);
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void remove(String item) {
        items.remove(item);
    }

    public void display() {
        renderContainer();
        title.display();
        renderItems();
    }

    private void renderContainer() {
        parent.push();
        parent.fill(backgroundColor);
        parent.noStroke();
        parent.rect(pos.x, pos.y, w, h);
        parent.pop();
    }

    public void renderItems() {
        parent.push();
        parent.textAlign(PConstants.LEFT, PConstants.CENTER);
        if (items.size() > 0) {
            for (int i = 0; i < items.size() && i < visibleItems; i++) {
                float yOffset = parent.height / 16 * i;
                TextField item = new TextField(parent, pos.x, pos.y + yOffset, w, h / visibleItems, items.get(i));
                item.setTextColor(textColor);
                item.setFontSize(fontSize);
                item.setFont(font);
                item.display();
            }
        }
        parent.pop();
    }

    public void createTitle(PApplet parent, float x, float y, float w, float h, String title) {
        this.title = new TextField(parent, x, y, w, parent.height / 16, title);
        this.title.setFont(font);
    }

    private int calculateNumberOfVisibleItems() {
        return h / (parent.height / 16);
    }

}
