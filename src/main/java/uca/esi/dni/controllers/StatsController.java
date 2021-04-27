package uca.esi.dni.controllers;

import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.file.Util;
import uca.esi.dni.main.DniParser;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.types.DatabaseResponseException;
import uca.esi.dni.types.JSONParsingException;
import uca.esi.dni.types.Survey;
import uca.esi.dni.ui.Warning;
import uca.esi.dni.views.View;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.MAIN;

public class StatsController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public StatsController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);
        asyncLoadSurveysFromDB();
    }

    @Override
    public void controllerLogic() {
        view.update(model.getDbStudents(), model.getTemporaryStudents(), model.getInputFile(), model.getStudentSurveys());
    }

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

    private void makeScreenShot() {
        parent.saveFrame("data/capturas/Captura-###.png");
        addWarning("Captura guardada en /data/capturas.", Warning.DURATION.MEDIUM, Warning.TYPE.INFO);
        LOGGER.info("[General information]: Captura de pantalla realizada.");
    }

    @Override
    public void handleKeyEvent(KeyEvent e) {
        //No need for key event handling in this view
    }

    @Override
    public void onContextMenuClosed(File file) {
        //There are no context menus in this view
    }


    private void asyncLoadSurveysFromDB() {
        addWarning("Cargando.", Warning.DURATION.SHORTEST, Warning.TYPE.INFO);
        Runnable runnable = () -> {
            try {
                String surveysURL = DatabaseHandler.getDatabaseDirectoryURL(model.getDBReference(), "Surveys");
                String responseSurveys = dbHandler.getDataFromDB(surveysURL);
                JSONObject surveysJSONObject = Util.parseJSONObject(responseSurveys);
                List<Survey> surveyList = Util.generateSurveyListFromJSONObject(surveysJSONObject);
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
}
