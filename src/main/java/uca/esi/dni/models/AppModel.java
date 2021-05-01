package uca.esi.dni.models;

import processing.data.JSONObject;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppModel implements Model {

    private final Set<Student> dbStudents = new HashSet<>();
    private final Set<Student> temporaryStudents = new HashSet<>();
    private final List<Survey> studentSurveys = new ArrayList<>();
    private File inputFile = new File("");
    private File outputFolder = new File("");
    private JSONObject settingsObject;
    private String dbReference;
    private boolean isStudentDataModified = false;
    private boolean isDataFirstLoaded = false;

    public AppModel(JSONObject settingsObject) {
        this.settingsObject = settingsObject;
        this.dbReference = settingsObject.getString("databaseURL");
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getDBReference() {
        return dbReference;
    }

    public void setDBReference(String dbReference) {
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

    public List<Survey> getStudentSurveys() {
        return studentSurveys;
    }

    public void addSurvey(Survey s) {
        studentSurveys.add(s);
    }

    public void addSurveyList(List<Survey> list) {
        studentSurveys.addAll(list);
    }

    public void removeSurveyList(List<Survey> list) {
        studentSurveys.removeAll(list);
    }

    public void removeSurvey(Survey s) {
        studentSurveys.remove(s);
    }

    public boolean isStudentDataModified() {
        return isStudentDataModified;
    }

    public void setStudentDataModified(boolean dataModified) {
        isStudentDataModified = dataModified;
    }

    public boolean isDataFirstLoaded() {
        return isDataFirstLoaded;
    }

    public void setDataFirstLoaded(boolean dataFirstLoaded) {
        isDataFirstLoaded = dataFirstLoaded;
    }

}
