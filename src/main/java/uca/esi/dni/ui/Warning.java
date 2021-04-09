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

    public enum TYPE {
        INFO,
        WARNING,
        SEVERE
    }

    private int fadeout;
    private final int maxLife;

    private static final int BACKGROUND_COLOR_INFO = View.COLORS.PRIMARY;
    private static final int BACKGROUND_COLOR_WARNING = View.COLORS.SECONDARY_DARK;
    private static final int BACKGROUND_COLOR_SEVERE = View.COLORS.RED;

    private static final int LINE_COLOR_INFO = View.COLORS.PRIMARY_DARK;
    private static final int LINE_COLOR_WARNING = View.COLORS.ACCENT_DARK;
    private static final int LINE_COLOR_SEVERE = View.COLORS.RED_DARK;

    private int lineColor;

    public Warning(PApplet parent, Rectangle rectangle, String contentString, DURATION duration, TYPE type) {
        super(parent, rectangle, contentString, "");

        this.fadeout = getWarningDurationTime(duration);
        this.maxLife = fadeout;
        getWarningColors(type);
        setCentered(true);
    }

    private void getWarningColors(TYPE type) {
        switch (type) {
            case WARNING:
                lineColor = LINE_COLOR_WARNING;
                setBackgroundColor(BACKGROUND_COLOR_WARNING);
                break;
            case SEVERE:
                lineColor = LINE_COLOR_SEVERE;
                setBackgroundColor(BACKGROUND_COLOR_SEVERE);
                break;
            default:
                lineColor = LINE_COLOR_INFO;
                setBackgroundColor(BACKGROUND_COLOR_INFO);
                break;
        }
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
        super.display();
        renderLifeBar();
    }


    private void renderLifeBar() {
        parent.push();
        parent.stroke(lineColor);
        parent.strokeWeight(h * 0.1f);
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
