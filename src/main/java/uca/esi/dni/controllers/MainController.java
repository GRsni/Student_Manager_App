package uca.esi.dni.controllers;

import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.models.Model;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.views.EditView;
import uca.esi.dni.views.StatsView;
import uca.esi.dni.views.View;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.*;

public class MainController extends Controller {

    public MainController(DniParser parent, Model model, View view) {
        super(parent, model, view);
    }

    @Override
    public void controllerLogic() {

    }

    @Override
    public void handleEvent(MouseEvent e) {
        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement(0).inside(e.getX(), e.getY())) {
                    //change view to editView
                    changeState(edit);
                } else if (view.getUIElement(1).inside(e.getX(), e.getY())) {
                    //TODO: call generateExcel
                } else if (view.getUIElement(2).inside(e.getX(), e.getY())) {
                    //change view to statsView
                    changeState(stats);
                }
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    if (element.isClicked()) {
                        element.isClicked(false);
                    }
                }
                break;
            case MOVE:
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    element.isHover(element.inside(e.getX(), e.getY()));

                }
                break;
            case PRESS:
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    if (element.inside(e.getX(), e.getY())) {
                        element.isClicked(true);
                    }
                }
            case RELEASE:
                //release mouse event is unreliable, multiple events per mouse click
                break;
            case WHEEL:
                BaseElement list = view.getUIElement(4);
                //TODO:scroll item list
        }
    }

    @Override
    public void changeState(VIEW_STATES state) {
        switch (state){
            case edit:
                DniParser.currentView=new EditView(parent);
                DniParser.currentController=new EditController(parent, DniParser.appModel, DniParser.currentView);
                break;
            case stats:
                DniParser.currentView=new StatsView(parent);
                DniParser.currentController=new StatsController(parent, DniParser.appModel, DniParser.currentView);
                break;
        }
    }
}
