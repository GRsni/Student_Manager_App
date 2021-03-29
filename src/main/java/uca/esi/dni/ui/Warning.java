package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import uca.esi.dni.views.View;

public class Warning extends BaseElement {

    public enum DURATION {
        SHORT,
        MEDIUM,
        LONG
    }

    private final TextField content;
    private int fadeout;
    private final int max_life;

    private final boolean isGood;

    private static final int backgroundColorGood = View.COLORS.PRIMARY;
    private static final int backgroundColorBad = View.COLORS.SECONDARY_DARK;

    private static final int lineColorGood = View.COLORS.PRIMARY_DARK;
    private static final int lineColorBad = View.COLORS.ACCENT_DARK;

    public Warning(PApplet parent, float x, float y, int w, int h, String contentString, DURATION duration, boolean good) {
        super(parent, new PVector(x, y), w, h);
        this.content = new TextField(parent, x, y, w, h, contentString, "");
        this.isGood = good;
        if (good) {
            this.content.setBackgroundColor(backgroundColorGood);
        } else {
            this.content.setBackgroundColor(backgroundColorBad);
        }
        this.content.setCentered(true);
        this.fadeout = getWarningDurationTime(duration);
        this.max_life = fadeout;
    }

    public int getWarningDurationTime(DURATION duration) {
        switch (duration) {
            case SHORT:
                return 150;
            case MEDIUM:
                return 200;
            case LONG:
                return 250;
            default:
                return 100;
        }
    }

    public void setFont(PFont font) {
        this.content.setFont(font);
    }

    public void setFontSize(int fontSize) {
        this.content.setFontSize(fontSize);
    }

    public void setFontColor(int fontColor) {
        this.content.setContentColor(fontColor);
    }

    @Override
    public void display() {
        renderBox();
        renderLifeBar();
        content.display();
    }

    private void renderBox() {
        parent.push();
        parent.noStroke();
        if (isGood) {
            parent.fill(backgroundColorGood);
        } else {
            parent.fill(backgroundColorBad);
        }
        parent.rect(pos.x, pos.y, w, h);
        parent.pop();
    }

    private void renderLifeBar() {
        parent.push();
        parent.strokeWeight(2);
        if (isGood) {
            parent.stroke(lineColorGood);
        } else {
            parent.stroke(lineColorBad);
        }
        parent.strokeWeight(h * 0.2f);
        parent.strokeCap(PApplet.SQUARE);
        parent.line(pos.x, pos.y + h, pos.x + calcLineBarLength(), pos.y + h);
        parent.pop();
    }

    public void update() {
        fadeout--;
    }

    private float calcLineBarLength() {
        return PApplet.map(fadeout, max_life, 0, w, 0);
    }

    public boolean toDestroy() {
        return fadeout <= 0;
    }

}
