package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import uca.esi.dni.views.View;

public class Warning extends TextField {

    public enum DURATION {
        SHORTEST,
        SHORT,
        MEDIUM,
        LONG
    }

    private int fadeout;
    private final int maxLife;

    private final boolean isGood;

    private static final int BACKGROUND_COLOR_GOOD = View.COLORS.PRIMARY;
    private static final int BACKGROUND_COLOR_BAD = View.COLORS.SECONDARY_DARK;

    private static final int LINE_COLOR_GOOD = View.COLORS.PRIMARY_DARK;
    private static final int LINE_COLOR_BAD = View.COLORS.ACCENT_DARK;

    public Warning(PApplet parent, Rectangle rectangle, String contentString, DURATION duration, boolean good) {
        super(parent, rectangle, contentString, "");

        this.fadeout = getWarningDurationTime(duration);
        this.maxLife = fadeout;
        this.isGood = good;
        if (good) {
            setBackgroundColor(BACKGROUND_COLOR_GOOD);
        } else {
            setBackgroundColor(BACKGROUND_COLOR_BAD);
        }
        setCentered(true);
    }

    public int getWarningDurationTime(DURATION duration) {
        switch (duration) {
            case SHORTEST:
                return 75;
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

    @Override
    public void display() {
        renderBox();
        renderLifeBar();
        super.display();
    }

    private void renderBox() {
        parent.push();
        parent.noStroke();
        if (isGood) {
            parent.fill(BACKGROUND_COLOR_GOOD);
        } else {
            parent.fill(BACKGROUND_COLOR_BAD);
        }
        parent.rect(pos.x, pos.y, w, h);
        parent.pop();
    }

    private void renderLifeBar() {
        parent.push();
        parent.strokeWeight(2);
        if (isGood) {
            parent.stroke(LINE_COLOR_GOOD);
        } else {
            parent.stroke(LINE_COLOR_BAD);
        }
        parent.strokeWeight(h * 0.2f);
        parent.strokeCap(PConstants.SQUARE);
        parent.line(pos.x, pos.y + h, pos.x + calcLineBarLength(), pos.y + h);
        parent.pop();
    }

    public void update() {
        fadeout--;
    }

    private float calcLineBarLength() {
        return PApplet.map(fadeout, maxLife, 0, w, 0);
    }

    public boolean toDestroy() {
        return fadeout <= 0;
    }

}
