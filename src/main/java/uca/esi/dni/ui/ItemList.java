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


/**
 * The type Item list.
 */
public class ItemList extends BaseElement {
    /**
     * The Items.
     */
    private final Set<String> items = new HashSet<>();

    /**
     * The Visible items.
     */
    private final int visibleItems;
    /**
     * The constant ITEM_HEIGHT.
     */
    private static final int ITEM_HEIGHT = View.getHeightUnitSize() / 2;

    /**
     * The Background color.
     */
    private int backgroundColor;
    /**
     * The Text color.
     */
    private int textColor;

    /**
     * The Title.
     */
    private final TextField title;
    /**
     * The Scroll index.
     */
    private int scrollIndex = 0;
    /**
     * The Max scroll.
     */
    private int maxScroll;

    /**
     * Instantiates a new Item list.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     * @param title     the title
     */
    public ItemList(PApplet parent, Rectangle rectangle, String title) {
        this(parent, rectangle, title, View.COLORS.ACCENT, View.COLORS.PRIMARY, View.COLORS.WHITE);

    }

    /**
     * Instantiates a new Item list.
     *
     * @param parent               the parent
     * @param rectangle            the rectangle
     * @param title                the title
     * @param backgroundColor      the background color
     * @param titleBackgroundColor the title background color
     * @param textColor            the text color
     */
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

    /**
     * Gets background color.
     *
     * @return the background color
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets background color.
     *
     * @param backgroundColor the background color
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Sets title background color.
     *
     * @param backgroundColor the background color
     */
    public void setTitleBackgroundColor(int backgroundColor) {
        this.title.setBackgroundColor(backgroundColor);
    }

    /**
     * Gets text color.
     *
     * @return the text color
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * Sets text color.
     *
     * @param textColor the text color
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.title.setContentColor(textColor);
    }

    /**
     * Sets title font.
     *
     * @param font the font
     */
    public void setTitleFont(PFont font) {
        this.title.setFont(font);
    }

    /**
     * Sets title font size.
     *
     * @param fontSize the font size
     */
    public void setTitleFontSize(int fontSize) {
        this.title.setFontSize(fontSize);
    }

    /**
     * Gets content list.
     *
     * @return the content list
     */
    public Set<String> getContentList() {
        return items;
    }

    /**
     * Add item.
     *
     * @param item the item
     */
    public void addItem(String item) {
        items.add(item);
        updateMaxScroll();
    }

    /**
     * Add list.
     *
     * @param list the list
     */
    public void addList(Set<String> list) {
        items.addAll(list);
        updateMaxScroll();
    }

    /**
     * Remove.
     *
     * @param item the item
     */
    public void remove(String item) {
        items.remove(item);
        updateMaxScroll();
    }

    /**
     * Remove list.
     *
     * @param list the list
     */
    public void removeList(Set<String> list) {
        items.removeAll(list);
        updateMaxScroll();
    }

    /**
     * Display.
     */
    @Override
    public void display() {
        renderContainer();
        title.display();
        renderItems();
        renderScrollBar();
    }

    /**
     * Render container.
     */
    private void renderContainer() {
        parent.push();
        parent.fill(backgroundColor);
        parent.noStroke();
        parent.rect(pos.x, pos.y, w, h);
        parent.pop();
    }

    /**
     * Render items.
     */
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

    /**
     * Gets item text field.
     *
     * @param rectangle the rectangle
     * @param content   the content
     * @return the item text field
     */
    private TextField getItemTextField(Rectangle rectangle, String content) {
        TextField textField = new TextField(parent, rectangle, content, "");
        textField.setContentColor(View.COLORS.BLACK);
        textField.setFontSize(fontSize);
        textField.setBackgroundColor(View.COLORS.ACCENT);
        textField.setFont(font);
        return textField;
    }

    /**
     * Render scroll bar.
     */
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

    /**
     * Render scroll.
     *
     * @param xCenterPos     the x center pos
     * @param scrollBarWidth the scroll bar width
     */
    private void renderScroll(float xCenterPos, float scrollBarWidth) {
        parent.push();
        parent.fill(View.COLORS.BLACK);
        parent.rectMode(PConstants.CORNER);
        float scrollWidth = scrollBarWidth * 0.75f;
        float scrollHeight = getScrollHeight();
        float scrollYOffset = getScrollYOffset(scrollHeight);
        parent.rect(xCenterPos - scrollWidth * 0.25f, pos.y + scrollYOffset, scrollWidth * 0.5f, scrollHeight, scrollWidth / 2);
        parent.pop();
    }

    /**
     * Gets scroll height.
     *
     * @return the scroll height
     */
    private float getScrollHeight() {
        int itemsHidden = items.size() - visibleItems;
        return Math.max(20, (h - 20) / itemsHidden);
    }

    /**
     * Gets scroll y offset.
     *
     * @param scrollHeight the scroll height
     * @return the scroll y offset
     */
    private float getScrollYOffset(float scrollHeight) {
        return PApplet.map(scrollIndex, 0, maxScroll, 10, h - scrollHeight - 10);
    }

    /**
     * Calculate number of visible items int.
     *
     * @return the int
     */
    private int calculateNumberOfVisibleItems() {
        return h / ITEM_HEIGHT - 1;
    }

    /**
     * Update max scroll.
     */
    private void updateMaxScroll() {
        maxScroll = items.size() - visibleItems;
        if (maxScroll < 0) {
            maxScroll = 0;
        }
    }

    /**
     * Handle input.
     *
     * @param e the e
     */
    @Override
    public void handleInput(MouseEvent e) {
        if (SystemUtils.IS_OS_MAC_OSX) {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex - e.getCount()));
        } else {
            scrollIndex = Math.min(maxScroll, Math.max(0, scrollIndex + e.getCount()));
        }
    }

    /**
     * Inside boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    @Override
    public boolean inside(int x, int y) {
        return x > pos.x && x < pos.x + w && y > pos.y + title.h && y < pos.y + h;
    }
}
