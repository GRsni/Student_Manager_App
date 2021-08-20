package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PConstants;
import uca.esi.dni.views.View;

/**
 * The type Warning.
 */
public class Warning extends TextField {

    /**
     * The enum Duration.
     */
    public enum DURATION {
        /**
         * Shortest duration.
         */
        SHORTEST,
        /**
         * Short duration.
         */
        SHORT,
        /**
         * Medium duration.
         */
        MEDIUM,
        /**
         * Long duration.
         */
        LONG
    }

    /**
     * The enum Type.
     */
    public enum TYPE {
        /**
         * Info type.
         */
        INFO,
        /**
         * Warning type.
         */
        WARNING,
        /**
         * Severe type.
         */
        SEVERE
    }

    /**
     * The Fadeout.
     */
    private int fadeout;
    /**
     * The Max life.
     */
    private final int maxLife;

    /**
     * The constant BACKGROUND_COLOR_INFO.
     */
    private static final int BACKGROUND_COLOR_INFO = View.COLORS.PRIMARY;
    /**
     * The constant BACKGROUND_COLOR_WARNING.
     */
    private static final int BACKGROUND_COLOR_WARNING = View.COLORS.SECONDARY_DARK;
    /**
     * The constant BACKGROUND_COLOR_SEVERE.
     */
    private static final int BACKGROUND_COLOR_SEVERE = View.COLORS.RED;

    /**
     * The constant LINE_COLOR_INFO.
     */
    private static final int LINE_COLOR_INFO = View.COLORS.PRIMARY_DARK;
    /**
     * The constant LINE_COLOR_WARNING.
     */
    private static final int LINE_COLOR_WARNING = View.COLORS.ACCENT_DARK;
    /**
     * The constant LINE_COLOR_SEVERE.
     */
    private static final int LINE_COLOR_SEVERE = View.COLORS.RED_DARK;

    /**
     * The Line color.
     */
    private int lineColor;

    /**
     * Instantiates a new Warning.
     *
     * @param parent        the parent
     * @param rectangle     the rectangle
     * @param contentString the content string
     * @param duration      the duration
     * @param type          the type
     */
    public Warning(PApplet parent, Rectangle rectangle, String contentString, DURATION duration, TYPE type) {
        super(parent, rectangle, contentString, "");

        this.fadeout = getWarningDurationTime(duration);
        this.maxLife = fadeout;
        getWarningColors(type);
        setCentered(true);
    }

    /**
     * Gets warning colors.
     *
     * @param type the type
     */
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

    /**
     * Gets warning duration time.
     *
     * @param duration the duration
     * @return the warning duration time
     */
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

    /**
     * Display.
     */
    @Override
    public void display() {
        super.display();
        renderLifeBar();
    }


    /**
     * Render life bar.
     */
    private void renderLifeBar() {
        parent.push();
        parent.stroke(lineColor);
        parent.strokeWeight(h * 0.1f);
        parent.strokeCap(PConstants.SQUARE);
        parent.line(pos.x, pos.y + h, pos.x + calcLineBarLength(), pos.y + h);
        parent.pop();
    }

    /**
     * Update.
     */
    public void update() {
        fadeout--;
    }

    /**
     * Calc line bar length float.
     *
     * @return the float
     */
    private float calcLineBarLength() {
        return PApplet.map(fadeout, maxLife, 0, w, 0);
    }

    /**
     * To destroy boolean.
     *
     * @return the boolean
     */
    public boolean toDestroy() {
        return fadeout <= 0;
    }

}
