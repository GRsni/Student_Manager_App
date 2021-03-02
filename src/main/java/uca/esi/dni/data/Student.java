package uca.esi.dni.data;

public class Student {
    private String ID;
    private String hashID;
    private int numP;

    public Student(String ID, String hashID, int numP) {
        this.ID = ID;
        this.hashID = hashID;
        this.numP = numP;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHashID() {
        return hashID;
    }

    public void setHashID(String hashID) {
        this.hashID = hashID;
    }

    public int getNumP() {
        return numP;
    }

    public void setNumP(int numP) {
        this.numP = numP;
    }
}
