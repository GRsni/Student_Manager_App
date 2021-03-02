package uca.esi.dni.models;

import uca.esi.dni.views.View;

import java.util.ArrayList;

public abstract class Model {
    private final ArrayList<View> views = new ArrayList<>();

    public void attach(View view) {
        views.add(view);
    }

    public void detach(View view) {
        views.remove(view);
    }

    public void notifyViews() {
        for (View v : views) {
            v.update();
        }
    }

}
