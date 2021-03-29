package uca.esi.dni.models;

import processing.data.JSONObject;
import uca.esi.dni.data.Student;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AppModel extends Model {
    private final Set<Student> DBStudents = new HashSet<>();
    private final Set<Student> temporaryStudents = new HashSet<>();
    private File inputFile = new File("");
    private File outputFolder = new File("");
    private JSONObject settingsObject;
    private String DBReference;

    public AppModel() {
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getDBReference() {
        return DBReference;
    }

    public void setDBReference(String DBReference) {
        this.DBReference = DBReference;
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

    public Set<Student> getDBStudents() {
        return DBStudents;
    }

    public void addDBStudentList(Set<Student> list) {
        DBStudents.addAll(list);
    }

    public void removeDBStudentList(Set<Student> list) {
        DBStudents.removeAll(list);
    }

    public void addDBStudent(Student s) {
        DBStudents.add(s);
    }

    public void removeDBStudent(Student s) {
        DBStudents.remove(s);
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
