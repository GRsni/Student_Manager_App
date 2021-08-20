package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import uca.esi.dni.views.View;

/**
 * The type Button.
 */
public class Button extends BaseElement {

    /**
     * The Corner r.
     */
    private final int cornerR;
    /**
     * The Active.
     */
    private boolean active;

    /**
     * The Icon.
     */
    private PImage icon;
    /**
     * The Is icon loaded.
     */
    private boolean isIconLoaded = false;

    /**
     * The Color.
     */
    private int color=View.COLORS.PRIMARY;
    /**
     * The Click color.
     */
    private int clickColor=View.COLORS.PRIMARY_DARK;
    /**
     * The Hover color.
     */
    private int hoverColor=View.COLORS.ACCENT_DARK;

    /**
     * The Content.
     */
    private final TextField content;
    /**
     * The Icon size.
     */
    private int iconSize = 0;

    /**
     * Instantiates a new Button.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     * @param corner    the corner
     * @param content   the content
     * @param active    the active
     */
    public Button(PApplet parent, Rectangle rectangle, int corner, String content, boolean active) {
        super(parent, rectangle);
        this.content = new TextField(parent, new Rectangle(rectangle.x + w * .1f, rectangle.y + h * .1f, w / 10 * 8, h / 10 * 8), content, "");
        this.cornerR = corner;
        this.active = active;
        this.content.setContentColor(View.COLORS.WHITE);
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public TextField getContent() {
        return content;
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
    public PVector getPos() {
        return pos;
    }

    /**
     * Sets pos.
     *
     * @param pos the pos
     */
    public void setPos(PVector pos) {
        this.pos = pos;
    }

    /**
     * Gets w.
     *
     * @return the w
     */
    public int getW() {
        return w;
    }

    /**
     * Sets w.
     *
     * @param w the w
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * Gets h.
     *
     * @return the h
     */
    public int getH() {
        return h;
    }

    /**
     * Sets h.
     *
     * @param h the h
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public PImage getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(PImage icon) {
        this.icon = icon;
        this.isIconLoaded = true;
        this.iconSize = h / 2;
        this.icon.resize(iconSize, iconSize); //resizing icon to improve performance
        adjustTextFieldWithIcon(iconSize);
    }

    /**
     * Adjust text field with icon.
     *
     * @param iconSize the icon size
     */
    private void adjustTextFieldWithIcon(int iconSize) {
        PVector textOffset = new PVector(iconSize, 0);
        content.setPos(content.getPos().x + textOffset.x, content.getPos().y + textOffset.y);
        content.setW(w / 10 * 7);
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Gets hover color.
     *
     * @return the hover color
     */
    public int getHoverColor() {
        return hoverColor;
    }

    /**
     * Sets hover color.
     *
     * @param hoverColor the hover color
     */
    public void setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
    }

    /**
     * Gets text color.
     *
     * @return the text color
     */
    public int getTextColor() {
        return content.getContentColor();
    }

    /**
     * Sets text color.
     *
     * @param textColor the text color
     */
    public void setTextColor(int textColor) {
        this.content.setContentColor(textColor);
    }

    /**
     * Gets click color.
     *
     * @return the click color
     */
    public int getClickColor() {
        return clickColor;
    }

    /**
     * Sets click color.
     *
     * @param clickColor the click color
     */
    public void setClickColor(int clickColor) {
        this.clickColor = clickColor;
    }

    /**
     * Gets font.
     *
     * @return the font
     */
    @Override
    public PFont getFont() {
        return content.getFont();
    }

    /**
     * Sets font.
     *
     * @param font the font
     */
    @Override
    public void setFont(PFont font) {
        this.content.setFont(font);
    }

    /**
     * Gets font size.
     *
     * @return the font size
     */
    @Override
    public int getFontSize() {
        return content.getFontSize();
    }

    /**
     * Sets font size.
     *
     * @param fontSize the font size
     */
    @Override
    public void setFontSize(int fontSize) {
        this.content.setFontSize(fontSize);
    }

    /**
     * Gets content background color.
     *
     * @return the content background color
     */
    public int getContentBackgroundColor() {
        return content.getBackgroundColor();
    }

    /**
     * Sets content background color.
     *
     * @param color the color
     */
    public void setContentBackgroundColor(int color) {
        content.setBackgroundColor(color);
    }

    /**
     * Gets is centered.
     *
     * @return the is centered
     */
    public boolean getIsCentered() {
        return content.isCentered();
    }

    /**
     * Sets centered.
     *
     * @param centered the centered
     */
    public void setCentered(boolean centered) {
        content.setCentered(centered);
    }

    /**
     * Display.
     */
    public void display() {
        renderButton();
        renderIcon();
        content.display();
    }

    /**
     * Render button.
     */
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

    /**
     * Render icon.
     */
    private void renderIcon() {
        if (isIconLoaded) {
            parent.image(icon, pos.x + (int) (h * .2), pos.y + h / 2.0f - iconSize / 2.0f);
        }
    }

}
