package uca.esi.dni.views;

import processing.core.PApplet;
import uca.esi.dni.data.Student;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.ArrayList;

public class StatsView extends View {

    public StatsView(PApplet parent) {
        super(parent);
        onCreate();
    }

    @Override
    protected void onCreate() {

    }

    @Override
    protected void createElements() {

    }

    @Override
    public void update(ArrayList<Student> dbList, ArrayList<Student> modList, File inputFile, String dbReference, ArrayList<Warning> warnings) {

    }
}
