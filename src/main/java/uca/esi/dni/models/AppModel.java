package uca.esi.dni.models;

import processing.data.JSONObject;
import uca.esi.dni.types.Student;
import uca.esi.dni.types.Survey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type App model.
 */
public class AppModel implements Model {

    /**
     * The Db students.
     */
    private final Set<Student> dbStudents = new HashSet<>();
    /**
     * The Temporary students.
     */
    private final Set<Student> temporaryStudents = new HashSet<>();
    /**
     * The Student surveys.
     */
    private final List<Survey> studentSurveys = new ArrayList<>();
    /**
     * The Input file.
     */
    private File inputFile = new File("");
    /**
     * The Output folder.
     */
    private File outputFolder = new File("");
    /**
     * The Settings object.
     */
    private JSONObject settingsObject;
    /**
     * The Db reference.
     */
    private String dbReference;
    /**
     * The Is student data modified.
     */
    private boolean isStudentDataModified = false;
    /**
     * The Is data first loaded.
     */
    private boolean isDataFirstLoaded = false;

    /**
     * Instantiates a new App model.
     *
     * @param settingsObject the settings object
     */
    public AppModel(JSONObject settingsObject) {
        this.settingsObject = settingsObject;
        this.dbReference = settingsObject.getString("databaseURL");
    }

    /**
     * Gets input file.
     *
     * @return the input file
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * Sets input file.
     *
     * @param inputFile the input file
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Gets db reference.
     *
     * @return the db reference
     */
    public String getDBReference() {
        return dbReference;
    }

    /**
     * Sets db reference.
     *
     * @param dbReference the db reference
     */
    public void setDBReference(String dbReference) {
        this.dbReference = dbReference;
    }

    /**
     * Gets output folder.
     *
     * @return the output folder
     */
    public File getOutputFolder() {
        return outputFolder;
    }

    /**
     * Sets output folder.
     *
     * @param outputFolder the output folder
     */
    public void setOutputFolder(File outputFolder) {
        this.outputFolder = outputFolder;
    }

    /**
     * Gets settings object.
     *
     * @return the settings object
     */
    public JSONObject getSettingsObject() {
        return settingsObject;
    }

    /**
     * Sets settings object.
     *
     * @param settingsObject the settings object
     */
    public void setSettingsObject(JSONObject settingsObject) {
        this.settingsObject = settingsObject;
    }

    /**
     * Gets db students.
     *
     * @return the db students
     */
    public Set<Student> getDbStudents() {
        return dbStudents;
    }

    /**
     * Add db student list.
     *
     * @param list the list
     */
    public void addDBStudentList(Set<Student> list) {
        dbStudents.addAll(list);
    }

    /**
     * Remove db student list.
     *
     * @param list the list
     */
    public void removeDBStudentList(Set<Student> list) {
        dbStudents.removeAll(list);
    }

    /**
     * Add db student.
     *
     * @param s the s
     */
    public void addDBStudent(Student s) {
        dbStudents.add(s);
    }

    /**
     * Remove db student.
     *
     * @param s the s
     */
    public void removeDBStudent(Student s) {
        dbStudents.remove(s);
    }

    /**
     * Gets temporary students.
     *
     * @return the temporary students
     */
    public Set<Student> getTemporaryStudents() {
        return temporaryStudents;
    }

    /**
     * Add temporary student.
     *
     * @param s the s
     */
    public void addTemporaryStudent(Student s) {
        temporaryStudents.add(s);
    }

    /**
     * Add temporary student list.
     *
     * @param list the list
     */
    public void addTemporaryStudentList(Set<Student> list) {
        temporaryStudents.addAll(list);
    }

    /**
     * Remove temporary student list.
     *
     * @param list the list
     */
    public void removeTemporaryStudentList(Set<Student> list) {
        temporaryStudents.removeAll(list);
    }

    /**
     * Remove temporary student.
     *
     * @param s the s
     */
    public void removeTemporaryStudent(Student s) {
        temporaryStudents.remove(s);
    }

    /**
     * Gets student surveys.
     *
     * @return the student surveys
     */
    public List<Survey> getStudentSurveys() {
        return studentSurveys;
    }

    /**
     * Add survey.
     *
     * @param s the s
     */
    public void addSurvey(Survey s) {
        studentSurveys.add(s);
    }

    /**
     * Add survey list.
     *
     * @param list the list
     */
    public void addSurveyList(List<Survey> list) {
        studentSurveys.addAll(list);
    }

    /**
     * Remove survey list.
     *
     * @param list the list
     */
    public void removeSurveyList(List<Survey> list) {
        studentSurveys.removeAll(list);
    }

    /**
     * Remove survey.
     *
     * @param s the s
     */
    public void removeSurvey(Survey s) {
        studentSurveys.remove(s);
    }

    /**
     * Is student data modified boolean.
     *
     * @return the boolean
     */
    public boolean isStudentDataModified() {
        return isStudentDataModified;
    }

    /**
     * Sets student data modified.
     *
     * @param dataModified the data modified
     */
    public void setStudentDataModified(boolean dataModified) {
        isStudentDataModified = dataModified;
    }

    /**
     * Is data first loaded boolean.
     *
     * @return the boolean
     */
    public boolean isDataFirstLoaded() {
        return isDataFirstLoaded;
    }

    /**
     * Sets data first loaded.
     *
     * @param dataFirstLoaded the data first loaded
     */
    public void setDataFirstLoaded(boolean dataFirstLoaded) {
        isDataFirstLoaded = dataFirstLoaded;
    }

}
