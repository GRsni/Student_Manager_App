package uca.esi.dni.models;

import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AppModel implements Model {
    private final Set<Student> dbStudents = new HashSet<>();
    private final Set<Student> temporaryStudents = new HashSet<>();
    private File inputFile = new File("");
    private File outputFolder = new File("");
    private JSONObject settingsObject;
    private String dbReference;

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getdbReference() {
        return dbReference;
    }

    public void setdbReference(String dbReference) {
        this.dbReference = dbReference;
    }

    public File getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    public JSONObject getSettingsObject() {
        return settingsObject;
    }

    public void setSettingsObject(JSONObject settingsObject) {
        this.settingsObject = settingsObject;
    }

    public Set<Student> getDbStudents() {
        return dbStudents;
    }

    public void addDBStudentList(Set<Student> list) {
        dbStudents.addAll(list);
    }

    public void removeDBStudentList(Set<Student> list) {
        dbStudents.removeAll(list);
    }

    public void addDBStudent(Student s) {
        dbStudents.add(s);
    }

    public void removeDBStudent(Student s) {
        dbStudents.remove(s);
    }

    public Set<Student> getTemporaryStudents() {
        return temporaryStudents;
    }

    public void addTemporaryStudent(Student s) {
        temporaryStudents.add(s);
    }

    public void addTemporaryStudentList(Set<Student> list) {
        temporaryStudents.addAll(list);
    }

    public void removeTemporaryStudentList(Set<Student> list) {
        temporaryStudents.removeAll(list);
    }

    public void removeTemporaryStudent(Student s) {
        temporaryStudents.remove(s);
    }

}
