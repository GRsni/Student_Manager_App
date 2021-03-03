package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;
import uca.esi.dni.views.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ItemList {
    private final Set<String> items = new HashSet<>();
    private final PApplet parent;

    private final PVector pos;
    private final int w;
    private final int h;
    private int visibleItems = 0;

    private int backgroundColor;
    private int textColor;
    private PFont font;
    private int fontSize;

    private boolean hasShadow = false;

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

    public void addList(Set<String> list) {
        items.addAll(list);
    }

    public void remove(String item) {
        items.remove(item);
    }

    public void removeList(Set<String> list) {
        items.removeAll(list);
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
        int itemsDisplayed = 0;

        parent.push();
        parent.textAlign(PConstants.LEFT, PConstants.CENTER);

        for (Iterator<String> it = items.iterator(); it.hasNext() && itemsDisplayed <= visibleItems; itemsDisplayed++) {
            float yOffset = parent.height / 16 * itemsDisplayed;
            String item = getItem(itemsDisplayed, it);
            TextField textField = new TextField(parent, pos.x, pos.y + yOffset, w, h / visibleItems, item);
            textField.setTextColor(textColor);
            textField.setFontSize(fontSize);
            textField.setBackgroundColor(View.COLORS.ACCENT);
            textField.setFont(font);
            textField.display();
        }

        parent.pop();
    }

    private String getItem(int itemsDisplayed, Iterator<String> it) {
        String item;
        if (itemsDisplayed < visibleItems) {
            item = it.next();
        } else {
            item = "...";
        }
        return item;
    }

    public void createTitle(PApplet parent, float x, float y, float w, float h, String title) {
        this.title = new TextField(parent, x, y, w, parent.height / 16, title);
        this.title.setFont(font);
    }

    private int calculateNumberOfVisibleItems() {
        return h / (parent.height / 16) - 1;
    }

}
