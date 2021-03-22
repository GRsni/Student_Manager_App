package uca.esi.dni.ui;

import org.apache.commons.lang3.SystemUtils;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;
import uca.esi.dni.views.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static uca.esi.dni.views.View.HEIGHT_UNIT_SIZE;

public class ItemList extends BaseElement {
    private final Set<String> items = new HashSet<>();

    private final int visibleItems;
    private final int itemHeight = HEIGHT_UNIT_SIZE / 2;

    private int backgroundColor;
    private int textColor;

    private final TextField title;
    private int scrollIndex = 0;
    private int maxScroll;

    public ItemList(PApplet parent, float x, float y, int w, int h, String title) {
        this(parent, x, y, w, h, title, View.COLORS.ACCENT, View.COLORS.PRIMARY, View.COLORS.WHITE);

    }

    public ItemList(PApplet parent, float x, float y, int w, int h, String title, int backgroundColor, int titleBackgroundColor, int textColor) {
        super(parent, new PVector(x, y + HEIGHT_UNIT_SIZE), w, h - HEIGHT_UNIT_SIZE);
        this.visibleItems = calculateNumberOfVisibleItems();
        this.maxScroll =
                this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.title = new TextField(parent, x, y, w, HEIGHT_UNIT_SIZE, title, "");
        this.title.setContentColor(textColor);
        this.title.setBackgroundColor(titleBackgroundColor);
        this.title.setIsHeader(true);
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
        this.title.setContentColor(textColor);
    }

    public PFont getFont() {
        return font;
    }

    public void setFont(PFont font) {
        this.font = font;
    }

    public void setTitleFont(PFont font) {
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

    public Set<String> getContentList() {
        return items;
    }

    public void addItem(String item) {
        items.add(item);
        updateMaxScroll();
    }

    public void addList(Set<String> list) {
        items.addAll(list);
        updateMaxScroll();
    }

    public void remove(String item) {
        items.remove(item);
        updateMaxScroll();
    }

    public void removeList(ArrayList<String> list) {
        items.removeAll(list);
        updateMaxScroll();
    }

    @Override
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
        ArrayList<String> itemList = new ArrayList<>(items);

        for (int i = scrollIndex; i < itemList.size() && itemsDisplayed <= visibleItems; i++) {
            float yOffset = itemHeight * itemsDisplayed;
            String item = itemList.get(i);
            TextField textField = new TextField(parent, pos.x, pos.y + yOffset, w, h / visibleItems, item, "");
            textField.setContentColor(View.COLORS.BLACK);
            textField.setFontSize(fontSize);
            textField.setBackgroundColor(View.COLORS.ACCENT);
            textField.setFont(font);
            textField.display();
            itemsDisplayed++;
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

    private int calculateNumberOfVisibleItems() {
        return h / itemHeight - 1;
    }

    private void updateMaxScroll() {
        maxScroll = items.size() - visibleItems;
        if (maxScroll < 0) {
            maxScroll = 0;
        }
    }

    public void handleInput(MouseEvent e) {
        if (SystemUtils.IS_OS_MAC_OSX) {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex - e.getCount()));
        } else {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex + e.getCount()));
        }
    }

    public void selectItemField(int x, int y) {

    }

    @Override
    public boolean inside(int x, int y) {
        return x > pos.x && x < pos.x + w && y > pos.y + title.h && y < pos.y + h;
    }
}
