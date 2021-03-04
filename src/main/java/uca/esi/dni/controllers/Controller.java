package uca.esi.dni.controllers;

import processing.core.PApplet;
import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.models.Model;
import uca.esi.dni.views.View;

public abstract class Controller {

    public enum VIEW_STATES{
        main,
        edit,
        stats
    }
    protected final Model model;
    protected final View view;
    protected final DniParser parent;

    public Controller(DniParser parent, Model model, View view) {
        this.parent = parent;
        this.model = model;
        this.view = view;
    }

    public abstract void controllerLogic();

    public abstract void handleEvent(MouseEvent e);

    public abstract void changeState(VIEW_STATES state);

}
