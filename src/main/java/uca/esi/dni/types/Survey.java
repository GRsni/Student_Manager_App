package uca.esi.dni.types;

import processing.data.JSONObject;

import java.util.Arrays;

public class Survey {
    private final String id;
    private final boolean likeTheApp;
    private final boolean learningFromApp;
    private final boolean useOutside;
    private final int[] likert;
    private final String suggestion;

    public enum LIKERT_FIELDS {
        EASE_OF_USE,
        LEARNING_HELP,
        GRASP,
        UI_LIKING,
        UX_FEEL;

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

    public String getId() {
        return id;
    }

    public boolean isLikeTheApp() {
        return likeTheApp;
    }

    public boolean isLearningFromApp() {
        return learningFromApp;
    }

    public boolean isUseOutside() {
        return useOutside;
    }

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

    public String getSuggestion() {
        return suggestion;
    }

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
