package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * The type Base element.
 */
public abstract class BaseElement {

    /**
     * The Parent.
     */
    protected final PApplet parent;
    /**
     * The Pos.
     */
    protected PVector pos;
    /**
     * The W.
     */
    protected int w;
    /**
     * The H.
     */
    protected int h;

    /**
     * The Clicked.
     */
    protected boolean clicked = false;
    /**
     * The Hover.
     */
    protected boolean hover = false;
    /**
     * The Is visible.
     */
    protected boolean isVisible = true;

    /**
     * The Font.
     */
    protected PFont font;
    /**
     * The Font size.
     */
    protected int fontSize = 10;

    /**
     * The type Rectangle.
     */
    public static class Rectangle {
        /**
         * The X.
         */
        public final float x;
        /**
         * The Y.
         */
        public final float y;
        /**
         * The W.
         */
        public final int w;
        /**
         * The H.
         */
        public final int h;

        /**
         * Instantiates a new Rectangle.
         *
         * @param x the x
         * @param y the y
         * @param w the w
         * @param h the h
         */
        public Rectangle(float x, float y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

    }


    /**
     * Instantiates a new Base element.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     */
    protected BaseElement(PApplet parent, Rectangle rectangle) {
        this.parent = parent;
        this.pos = new PVector(rectangle.x, rectangle.y);
        this.w = rectangle.w;
        this.h = rectangle.h;
    }

    /**
     * Gets font.
     *
     * @return the font
     */
    public PFont getFont() {
        return font;
    }

    /**
     * Sets font.
     *
     * @param font the font
     */
    public void setFont(PFont font) {
        this.font = font;
    }

    /**
     * Gets font size.
     *
     * @return the font size
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets font size.
     *
     * @param fontSize the font size
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Display.
     */
    public abstract void display();

    /**
     * Inside boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean inside(int x, int y) {
        return x > pos.x && x < pos.x + w && y > pos.y && y < pos.y + h;
    }

    /**
     * Is clicked boolean.
     *
     * @return the boolean
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Is clicked.
     *
     * @param val the val
     */
    public void isClicked(boolean val) {
        clicked = val;
    }

    /**
     * Is hover boolean.
     *
     * @return the boolean
     */
    public boolean isHover() {
        return hover;
    }

    /**
     * Is hover.
     *
     * @param val the val
     */
    public void isHover(boolean val) {
        hover = val;
    }

    /**
     * Is visible boolean.
     *
     * @return the boolean
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets visible.
     *
     * @param visible the visible
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Handle input.
     *
     * @param e the e
     */
    public void handleInput(MouseEvent e) {
    }

    /**
     * Resize.
     *
     * @param newPos    the new pos
     * @param newWidth  the new width
     * @param newHeight the new height
     */
    public void resize(PVector newPos, int newWidth, int newHeight) {
        this.pos = newPos;
        this.w = newWidth;
        this.h = newHeight;
    }

}
