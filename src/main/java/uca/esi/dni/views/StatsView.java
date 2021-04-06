package uca.esi.dni.views;

import processing.core.PApplet;
import uca.esi.dni.types.Student;

import java.io.File;
import java.util.Set;

public class StatsView extends View {

    public StatsView(PApplet parent) {
        super(parent);
        onCreate();
    }

    @Override
    protected void onCreate() {
        background = parent.loadImage("data/background/stats_back.png");

        background.resize(parent.width, parent.height); //resize background image to app size to improve performance

        createElements();
    }

    @Override
    protected void createElements() {

    }

    @Override
    public void update(Set<Student> dbList, Set<Student> modList, File inputFile) {

    }
}
