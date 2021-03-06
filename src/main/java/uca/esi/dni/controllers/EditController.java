package uca.esi.dni.controllers;

import processing.event.MouseEvent;
import uca.esi.dni.DniParser;
import uca.esi.dni.data.Student;
import uca.esi.dni.models.AppModel;
import uca.esi.dni.ui.BaseElement;
import uca.esi.dni.ui.TextField;
import uca.esi.dni.views.View;

import java.io.File;

import static processing.event.MouseEvent.*;
import static uca.esi.dni.controllers.Controller.VIEW_STATES.main;

public class EditController extends Controller {

    public EditController(DniParser parent, AppModel model, View view) {
        super(parent, model, view);
    }

    @Override
    public void controllerLogic() {

    }

    @Override
    public void handleEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        switch (e.getAction()) {
            case CLICK:
                if (view.getUIElement(0).inside(x, y)) {
                    //TODO: Add student ID to aux list
                    TextField idTF = (TextField) view.getUIElement(7);
                    TextField emailTF = (TextField) view.getUIElement(8);
                    model.addModifiedStudent(new Student(idTF.getContent(), emailTF.getContent(), 0));
                } else if (view.getUIElement(1).inside(x, y)) {
                    //TODO: choose file context menu
                } else if (view.getUIElement(2).inside(x, y)) {
                    //change view to mainView
                    changeState(main);
                } else if (view.getUIElement(3).inside(x, y)) {
                    //TODO: Add aux list to DB list
                } else if (view.getUIElement(4).inside(x, y)) {
                    //TODO: Remove aux list from DB list
                } else if (view.getUIElement(5).inside(x, y)) {
                    //TODO: Empty DB list
                } else if (view.getUIElement(7).inside(x, y)) {
                    //TODO: Edit ID TextField text
                } else if (view.getUIElement(8).inside(x, y)) {
                    //TODO: Edit email TextField text
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
                    element.isHover(element.inside(x, y));

                }
                break;
            case PRESS:
                for (int i = 0; i < 3; i++) {
                    BaseElement element = view.getUIElement(i);
                    if (element.inside(x, y)) {
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
    public void onContextMenuClosed(File file) {

    }
}
