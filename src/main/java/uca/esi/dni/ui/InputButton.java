package uca.esi.dni.ui;

import processing.core.PVector;
import processing.core.PApplet;
import uca.esi.dni.main.DniParser;

public class InputButton {
    private final DniParser parent;
    private String filename = "";
    private final PVector pos;
    private final Button inputB;

    public InputButton(DniParser parent, PVector pos, String title, String content) {
        PVector buttonPos = new PVector(pos.x + 80, pos.y + 15);
        inputB = new Button(parent, buttonPos.x, buttonPos.y, 120, 30, 0, content, true);
        this.pos = pos;
        this.parent = parent;
    }

    public InputButton(DniParser parent, PVector pos, String title, String content, String iconFilename) {
        PVector buttonPos = new PVector(pos.x + 80, pos.y + 15);
        inputB = new Button(parent, buttonPos.x, buttonPos.y, 120, 30, 0, content, true);
        this.pos = pos;
        this.parent = parent;
    }

    public void display() {
        inputB.display();
        renderFileName();
    }

    private void renderFileName() {
        renderContainerBox();
        parent.push();
        parent.textAlign(PApplet.LEFT, PApplet.CENTER);
        parent.textSize(14);
        parent.text(filename, pos.x - 5 + inputB.getW() + 10, pos.y + inputB.getH() / 2.0f - 2);
        parent.pop();
    }

    private void renderContainerBox() {
        parent.push();
        parent.fill(170);
        parent.noStroke();
        parent.rect(pos.x + inputB.getW(), pos.y, 200, 30);
        parent.pop();
    }

    public void isClicked(boolean state) {
        inputB.isClicked(state);
    }

    public boolean isClicked() {
        return inputB.isClicked();
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String getFileName() {
        return filename;
    }
}
