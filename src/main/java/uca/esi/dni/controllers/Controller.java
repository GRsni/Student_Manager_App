package uca.esi.dni.controllers;

import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.file.DatabaseHandler;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.MainView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import java.io.File;

public abstract class Controller {

    public enum VIEW_STATES {
        main,
        edit,
        stats
    }

    protected static DatabaseHandler dbHandler = new DatabaseHandler();
    protected final AppModel model;
    protected final View view;
    protected final DniParser parent;

    public volatile boolean closedContextMenu = false;


    public Controller(DniParser parent, AppModel model, View view) {
        this.parent = parent;
        this.model = model;
        this.view = view;
    }

    public boolean isClosedContextMenu() {
        return closedContextMenu;
    }

    public void setClosedContextMenu(boolean closedContextMenu) {
        this.closedContextMenu = closedContextMenu;
    }

    public abstract void controllerLogic();

    public abstract void handleEvent(MouseEvent e);

    public abstract void onContextMenuClosed(File file);

    public void changeState(VIEW_STATES state) {
        //TODO: maybe extract into factory method
        switch (state) {
            case edit:
                DniParser.currentView = new EditView(parent);
                DniParser.currentController = new EditController(parent, DniParser.appModel, DniParser.currentView);
                break;
            case stats:
                DniParser.currentView = new StatsView(parent);
                DniParser.currentController = new StatsController(parent, DniParser.appModel, DniParser.currentView);
                break;
            case main:
                DniParser.currentView = new MainView(parent);
                DniParser.currentController = new MainController(parent, DniParser.appModel, DniParser.currentView);
                break;
        }
        loadInitialState();
    }

    private void loadInitialState() {
        DniParser.currentController.controllerLogic();
    }

}
