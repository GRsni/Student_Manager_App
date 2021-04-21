package uca.esi.dni.ui;

import org.apache.commons.lang3.SystemUtils;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.event.MouseEvent;
import uca.esi.dni.views.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ItemList extends BaseElement {
    private final Set<String> items = new HashSet<>();

    private final int visibleItems;
    private static final int ITEM_HEIGHT = View.getHeightUnitSize() / 2;

    private int backgroundColor;
    private int textColor;

    private final TextField title;
    private int scrollIndex = 0;
    private int maxScroll;

    public ItemList(PApplet parent, Rectangle rectangle, String title) {
        this(parent, rectangle, title, View.COLORS.ACCENT, View.COLORS.PRIMARY, View.COLORS.WHITE);

    }

    public ItemList(PApplet parent, Rectangle rectangle, String title, int backgroundColor, int titleBackgroundColor, int textColor) {
        super(parent, new Rectangle(rectangle.x, rectangle.y + View.getHeightUnitSize(), rectangle.w, rectangle.h - View.getHeightUnitSize()));
        this.visibleItems = calculateNumberOfVisibleItems();
        this.maxScroll = this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.title = new TextField(parent, new Rectangle(rectangle.x, rectangle.y, w, View.getHeightUnitSize()), title, "");
        this.title.setContentColor(textColor);
        this.title.setBackgroundColor(titleBackgroundColor);
        this.title.setIsHeader(true);
        this.title.setCentered(true);
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

    public void setTitleFont(PFont font) {
        this.title.setFont(font);
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

    public void removeList(Set<String> list) {
        items.removeAll(list);
        updateMaxScroll();
    }

    @Override
    public void display() {
        renderContainer();
        title.display();
        renderItems();
        renderScrollBar();
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

        for (int i = scrollIndex; i < itemList.size() && itemsDisplayed <= visibleItems; i++, itemsDisplayed++) {
            int yOffset = ITEM_HEIGHT * itemsDisplayed;
            String content = itemList.get(i);
            Rectangle rectangle = new Rectangle(pos.x, pos.y + yOffset, w, h / visibleItems);
            TextField textField = getItemTextField(rectangle, content);
            textField.display();
        }
        parent.pop();
    }

    private TextField getItemTextField(Rectangle rectangle, String content) {
        TextField textField = new TextField(parent, rectangle, content, "");
        textField.setContentColor(View.COLORS.BLACK);
        textField.setFontSize(fontSize);
        textField.setBackgroundColor(View.COLORS.ACCENT);
        textField.setFont(font);
        return textField;
    }

    private void renderScrollBar() {
        parent.push();
        parent.fill(View.COLORS.ACCENT_DARK);
        parent.noStroke();
        parent.rect(pos.x + w * 0.9f, pos.y, w * 0.1f, h);
        if (items.size() > visibleItems) {
            renderScroll(pos.x + w * .95f, w * 0.1f);
        }
        parent.pop();
    }

    private void renderScroll(float xCenterPos, float scrollBarWidth) {
        parent.push();
        parent.fill(View.COLORS.BLACK);
        parent.rectMode(PConstants.CORNER);
        float scrollWidth = scrollBarWidth * 0.75f;
        float scrollHeight = getScrollHeight();
        System.out.println(scrollHeight);
        float scrollYOffset = getScrollYOffset(scrollHeight);
        parent.rect(xCenterPos - scrollWidth * 0.25f, pos.y + scrollYOffset, scrollWidth * 0.5f, scrollHeight, scrollWidth / 2);
        parent.pop();
    }

    private float getScrollHeight() {
        int itemsHidden = items.size() - visibleItems;
        return Math.max(20, (h - 20) / itemsHidden);
    }

    private float getScrollYOffset(float scrollHeight) {
        return PApplet.map(scrollIndex, 0, maxScroll, 10, h - scrollHeight - 10);
    }

    private int calculateNumberOfVisibleItems() {
        return h / ITEM_HEIGHT - 1;
    }

    private void updateMaxScroll() {
        maxScroll = items.size() - visibleItems;
        if (maxScroll < 0) {
            maxScroll = 0;
        }
    }

    @Override
    public void handleInput(MouseEvent e) {
        if (SystemUtils.IS_OS_MAC_OSX) {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex - e.getCount()));
        } else {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex + e.getCount()));
        }
    }

    @Override
    public boolean inside(int x, int y) {
        return x > pos.x && x < pos.x + w && y > pos.y + title.h && y < pos.y + h;
    }
}
