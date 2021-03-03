package uca.esi.dni.controllers;

import uca.esi.dni.models.Model;
import uca.esi.dni.views.View;

public abstract class Controller {
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public abstract void controllerLogic();
}
