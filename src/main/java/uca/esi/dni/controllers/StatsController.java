package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.handlers.db.DatabaseHandler;
import uca.esi.dni.handlers.json.JSONHandler;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.types.JSONParsingException;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.MAIN;

/**
 * The type Stats controller.
 */
public class StatsController extends Controller {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Instantiates a new Stats controller.
     *
     * @param parent the parent
     * @param model  the model
     * @param view   the view
     */
    public StatsController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);
        onCreate();
    }

    /**
     * On create.
     */
    @Override
    protected void onCreate() {
        asyncLoadSurveysFromDB();
    }

    /**
     * Handle mouse event.
     *
     * @param e the e
     */
    @Override
    public void handleMouseEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement("backB").inside(x, y)) {
                    changeState(MAIN);
                } else if (view.getUIElement("screenCapB").inside(x, y)) {
                    makeScreenShot();
                }
                removeClick();
                controllerLogic();
                break;
            case MouseEvent.MOVE:
                checkHover(x, y);
                break;
            case PRESS:
                checkPress(x, y);
                break;
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
            default:
                break;
        }
    }

    /**
     * Make screen shot.
     */
    private void makeScreenShot() {
        parent.saveFrame("data/capturas/Captura-###.png");
        addWarning("Captura guardada en /data/capturas.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
        LOGGER.info("[General information]: Captura de pantalla realizada.");
    }

    /**
     * Handle key event.
     *
     * @param e the e
     */
    @Override
    public void handleKeyEvent(KeyEvent e) {
        //No need for key event handling in this view
    }

    /**
     * On context menu closed.
     *
     * @param file the file
     */
    @Override
    public void onContextMenuClosed(File file) {
        //There are no context menus in this view
    }


    /**
     * Async load surveys from db.
     */
    private void asyncLoadSurveysFromDB() {
        addWarning("Cargando.", Warning.DURATION.SHORTEST, Warning.TYPE.INFO);
        Runnable runnable = () -> {
            try {
                String surveysURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Surveys");
                String responseSurveys = dbHandler.getData(surveysURL);
                JSONObject surveysJSONObject = JSONHandler.parseJSONObject(responseSurveys);
                List<Survey> surveyList = generateSurveyListFromJSONObject(surveysJSONObject);
                model.getStudentSurveys().clear();
                model.addSurveyList(surveyList);
                addWarning("Cargados datos de encuestas.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
                String toLog = "[Cargados datos de encuestas]: " + surveyList.size();
                LOGGER.info(toLog);
                controllerLogic();

            } catch (IOException | DatabaseResponseException e) {
                LOGGER.warning("[Error loading data from DB]: " + e.getMessage());
                addWarning("Error leyendo la base de datos.", Warning.DURATION.SHORT, Warning.TYPE.INFO);
            } catch (JSONParsingException e) {
                LOGGER.severe("[Error generating survey list]: " + e.getMessage());
                addWarning("Error cargando lista de encuestas", Warning.DURATION.SHORT, Warning.TYPE.SEVERE);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Generate survey list from json object list.
     *
     * @param surveysJSONObject the surveys json object
     * @return the list
     * @throws JSONParsingException the json parsing exception
     */
    private List<Survey> generateSurveyListFromJSONObject(JSONObject surveysJSONObject)
            throws JSONParsingException {
        List<Survey> surveyList = new ArrayList<>();
        List<String> keys = JSONHandler.getJSONObjectKeys(surveysJSONObject);
        for (String surveyId : keys) {
            surveyList.add(new Survey(surveyId, surveysJSONObject.getJSONObject(surveyId)));
        }
        return surveyList;
    }
}
