package uca.esi.dni.types;

import processing.data.JSONObject;

import java.util.Arrays;

/**
 * The type Survey.
 */
public class Survey {
    /**
     * The Id.
     */
    private final String id;
    /**
     * The Like the app.
     */
    private final boolean likeTheApp;
    /**
     * The Learning from app.
     */
    private final boolean learningFromApp;
    /**
     * The Use outside.
     */
    private final boolean useOutside;
    /**
     * The Likert.
     */
    private final int[] likert;
    /**
     * The Suggestion.
     */
    private final String suggestion;

    /**
     * The enum Likert fields.
     */
    public enum LIKERT_FIELDS {
        /**
         * Ease of use likert fields.
         */
        EASE_OF_USE,
        /**
         * Learning help likert fields.
         */
        LEARNING_HELP,
        /**
         * Grasp likert fields.
         */
        GRASP,
        /**
         * Ui liking likert fields.
         */
        UI_LIKING,
        /**
         * Ux feel likert fields.
         */
        UX_FEEL;

        /**
         * To string string.
         *
         * @return the string
         */
        @Override
        public String toString() {
            switch (this){
                case EASE_OF_USE:
                    return "Facilidad de uso.";
                case LEARNING_HELP:
                    return "Ayuda en el aprendizaje.";
                case GRASP:
                    return "Entendimiento de la aplicación.";
                case UI_LIKING:
                    return "Interfaz de usuario.";
                case UX_FEEL:
                    return "Experiencia de usuario.";
                default:
                    return "";

            }
        }
    }

    /**
     * Instantiates a new Survey.
     *
     * @param id         the id
     * @param jsonObject the json object
     * @throws JSONParsingException the json parsing exception
     */
    public Survey(String id, JSONObject jsonObject) throws JSONParsingException {
        try {
            this.id = id;
            this.likert = jsonObject.getJSONArray("likert").getIntArray();
            this.likeTheApp = jsonObject.getBoolean("like");
            this.learningFromApp = jsonObject.getBoolean("learning");
            this.useOutside = jsonObject.getBoolean("outside");
            this.suggestion = jsonObject.getString("suggestion");
        } catch (RuntimeException e) {
            throw new JSONParsingException(e.getMessage());
        }

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Is like the app boolean.
     *
     * @return the boolean
     */
    public boolean isLikeTheApp() {
        return likeTheApp;
    }

    /**
     * Is learning from app boolean.
     *
     * @return the boolean
     */
    public boolean isLearningFromApp() {
        return learningFromApp;
    }

    /**
     * Is use outside boolean.
     *
     * @return the boolean
     */
    public boolean isUseOutside() {
        return useOutside;
    }

    /**
     * Gets yes no answer.
     *
     * @param field the field
     * @return the yes no answer
     */
    public boolean getYesNoAnswer(String field) {
        switch (field) {
            case "like":
                return likeTheApp;
            case "learning":
                return learningFromApp;
            case "outside":
                return useOutside;
            default:
                return false;
        }
    }

    /**
     * Gets likert value.
     *
     * @param field the field
     * @return the likert value
     */
    public int getLikertValue(LIKERT_FIELDS field) {
        switch (field) {
            case EASE_OF_USE:
                return likert[0];
            case LEARNING_HELP:
                return likert[1];
            case GRASP:
                return likert[2];
            case UI_LIKING:
                return likert[3];
            case UX_FEEL:
                return likert[4];
            default:
                return 0;
        }
    }

    /**
     * Gets suggestion.
     *
     * @return the suggestion
     */
    public String getSuggestion() {
        return suggestion;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Survey{" +
                "id='" + id + '\'' +
                ", likeTheApp=" + likeTheApp +
                ", learningFromApp=" + learningFromApp +
                ", useOutside=" + useOutside +
                ", likert=" + Arrays.toString(likert) +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }
}
