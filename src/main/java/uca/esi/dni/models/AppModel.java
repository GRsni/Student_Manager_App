package uca.esi.dni.models;

import uca.esi.dni.data.Student;
import uca.esi.dni.ui.Warning;

import java.io.File;
import java.util.ArrayList;

public class AppModel extends Model {
    private final ArrayList<Student> DBStudents = new ArrayList<>();
    private final ArrayList<Student> modifiedStudents = new ArrayList<>();
    private File inputFile;
    private String DBReference;
    private final ArrayList<Warning> warnings = new ArrayList<>();

    public AppModel(){}

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

    public void addDBStudent(Student s) {
        DBStudents.add(s);
    }

    public void removeDBStudent(Student s) {
        DBStudents.remove(s);
    }

    public Student getDBStudent(int index) {
        return DBStudents.get(index);
    }

    public void addModifiedStudent(Student s) {
        modifiedStudents.add(s);
    }

    public void removeModifiedStudent(Student s) {
        modifiedStudents.remove(s);
    }

    public Student getModifiedStudent(int index) {
        return modifiedStudents.get(index);
    }

    public void addWarning(Warning w){
        warnings.add(w);
    }

    public void removeWarning(Warning w){
        warnings.remove(w);
    }

    public Warning getWarning(int index){
        return warnings.get(index);
    }
}
