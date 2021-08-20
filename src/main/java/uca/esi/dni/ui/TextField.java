package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import uca.esi.dni.views.View;

/**
 * The type Text field.
 */
public class TextField extends BaseElement {

    /**
     * The Background color.
     */
    private int backgroundColor;
    /**
     * The Has background.
     */
    private boolean hasBackground = false;
    /**
     * The Content color.
     */
    private int contentColor = View.COLORS.BLACK;
    /**
     * The Hint color.
     */
    private int hintColor = View.COLORS.ACCENT_DARK;
    /**
     * The Content.
     */
    private String content;
    /**
     * The Hint.
     */
    private final String hint;

    /**
     * The Has max length.
     */
    private boolean hasMaxLength = false;
    /**
     * The Max length.
     */
    private int maxLength;
    /**
     * The constant PADDING.
     */
    private static final int PADDING = 4;
    /**
     * The Is clickable.
     */
    private boolean isClickable = false;
    /**
     * The Is focused.
     */
    private boolean isFocused = false;
    /**
     * The Is header.
     */
    private boolean isHeader = false;
    /**
     * The Is centered.
     */
    private boolean isCentered = false;
    /**
     * The Is scrollable.
     */
    private boolean isScrollable = false;
    /**
     * The Is cuttable.
     */
    private boolean isCuttable = false;


    /**
     * Instantiates a new Text field.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     * @param content   the content
     * @param hint      the hint
     */
    public TextField(PApplet parent, Rectangle rectangle, String content, String hint) {
        super(parent, rectangle);
        this.content = content;
        this.hint = hint;
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
     * @param x the x
     * @param y the y
     */
    public void setPos(float x, float y) {
        this.pos = new PVector(x, y);
    }

    /**
     * Gets w.
     *
     * @return the w
     */
    public float getW() {
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
    public float getH() {
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
     * Gets content color.
     *
     * @return the content color
     */
    public int getContentColor() {
        return contentColor;
    }

    /**
     * Sets content color.
     *
     * @param contentColor the content color
     */
    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    /**
     * Gets hint color.
     *
     * @return the hint color
     */
    public int getHintColor() {
        return hintColor;
    }

    /**
     * Sets hint color.
     *
     * @param hintColor the hint color
     */
    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
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
        hasBackground = true;
    }

    /**
     * Gets max length.
     *
     * @return the max length
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets max length.
     *
     * @param maxLength the max length
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        hasMaxLength = true;
    }

    /**
     * Is clickable boolean.
     *
     * @return the boolean
     */
    public boolean isClickable() {
        return isClickable;
    }

    /**
     * Sets clickable.
     *
     * @param clickable the clickable
     */
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    /**
     * Is focused boolean.
     *
     * @return the boolean
     */
    public boolean isFocused() {
        return isFocused;
    }

    /**
     * Sets focused.
     *
     * @param focused the focused
     */
    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    /**
     * Is header boolean.
     *
     * @return the boolean
     */
    public boolean isHeader() {
        return isHeader;
    }

    /**
     * Sets is header.
     *
     * @param header the header
     */
    public void setIsHeader(boolean header) {
        isHeader = header;
    }

    /**
     * Is centered boolean.
     *
     * @return the boolean
     */
    public boolean isCentered() {
        return isCentered;
    }

    /**
     * Sets centered.
     *
     * @param centered the centered
     */
    public void setCentered(boolean centered) {
        isCentered = centered;
    }

    /**
     * Is scrollable boolean.
     *
     * @return the boolean
     */
    public boolean isScrollable() {
        return isScrollable;
    }

    /**
     * Sets scrollable.
     *
     * @param scrollable the scrollable
     */
    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }

    /**
     * Is cuttable boolean.
     *
     * @return the boolean
     */
    public boolean isCuttable() {
        return isCuttable;
    }

    /**
     * Sets cuttable.
     *
     * @param cuttable the cuttable
     */
    public void setCuttable(boolean cuttable) {
        isCuttable = cuttable;
    }

    /**
     * Display.
     */
    public void display() {
        renderBackground();
        renderContent();
        renderCursor();
    }

    /**
     * Render background.
     */
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

    /**
     * Render content.
     */
    private void renderContent() {
        parent.push();
        parent.textSize(fontSize);
        parent.textFont(font);
        if (!isCentered) {
            parent.textAlign(PConstants.LEFT, PConstants.CENTER);
        } else {
            parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        }
        String text = getContentTextAndFill();
        text = getFinalCutScrollText(text);
        parent.text(text, pos.x + getXOffset(), pos.y, w, h);
        parent.pop();
    }

    /**
     * Gets content text and fill.
     *
     * @return the content text and fill
     */
    private String getContentTextAndFill() {
        String text;
        if (!content.isEmpty() || isFocused) {
            parent.fill(contentColor);
            text = content;
        } else {
            parent.fill(hintColor);
            text = hint;
        }
        return text;
    }

    /**
     * Gets final cut scroll text.
     *
     * @param text the text
     * @return the final cut scroll text
     */
    private String getFinalCutScrollText(String text) {
        if (parent.textWidth(text) > w) {
            if (isCuttable) {
                text = text.substring(0, maxNumCharacters());
                text += "...";
            } else if (isScrollable) {
                text = text.substring(text.length() - maxNumCharacters());
            }
        }
        return text;
    }

    /**
     * Gets x offset.
     *
     * @return the x offset
     */
    private float getXOffset() {
        if (isCentered) {
            return 0;
        } else {
            return PADDING;
        }
    }

    /**
     * Max num characters int.
     *
     * @return the int
     */
    private int maxNumCharacters() {
        return w / (font.getSize() / 2);
    }

    /**
     * Render cursor.
     */
    private void renderCursor() {
        parent.push();
        if (isFocused && (parent.frameCount >> 5 & 1) == 0) {
            parent.stroke(View.COLORS.BLACK);
            parent.strokeWeight(3);
            parent.strokeCap(PConstants.SQUARE);
            parent.textFont(font);
            float xCursorPos = getCursorXPos();
            float xOffset = pos.x + PADDING + xCursorPos;
            parent.line(xOffset, pos.y + PADDING, xOffset, pos.y + h - PADDING);
        }
        parent.pop();
    }

    /**
     * Gets cursor x pos.
     *
     * @return the cursor x pos
     */
    private float getCursorXPos() {
        return Math.max(0, Math.min(w, parent.textWidth(content)));
    }


    /**
     * Add char to content.
     *
     * @param k the k
     */
    public void addCharToContent(char k) {
        if (hasMaxLength) {
            if (content.length() < maxLength) {
                content += k;
            }
        } else {
            content += k;
        }
    }

    /**
     * Remove character.
     */
    public void removeCharacter() {
        content = content.substring(0, Math.max(0, content.length() - 1));
    }

    /**
     * Modify counter.
     *
     * @param number the number
     */
    public void modifyCounter(int number) {
        modifyCounter(Integer.toString(number));
    }

    /**
     * Modify counter.
     *
     * @param number the number
     */
    public void modifyCounter(float number) {
        modifyCounter(Float.toString(number));
    }

    /**
     * Modify counter.
     *
     * @param end the end
     */
    public void modifyCounter(String end) {
        content = content.substring(0, content.lastIndexOf(':') + 2) + end;
    }
}
