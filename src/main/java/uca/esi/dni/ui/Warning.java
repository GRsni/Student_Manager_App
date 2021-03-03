package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PVector;
import uca.esi.dni.DniParser;
import uca.esi.dni.views.View;

public class Warning {
    DniParser parent;
    String content;
    PVector pos;
    float w, h;
    int fadeout, max_life;

    public Warning(DniParser parent, String content, PVector pos, float w, float h, int fadeout) {
        this.parent = parent;
        this.content = content;
        this.fadeout = fadeout;
        this.max_life = fadeout;
        this.pos = pos;
        this.w = w;
        this.h = h;
    }

    public Warning(DniParser parent, String content, int fadeout) {
        this(parent, content, new PVector(parent.width / 2f, parent.height / 2f), parent.textWidth(content) + 15, 35,
                fadeout);
    }

    public void show() {
        renderBox();
        renderLifeBar();
        renderContent();
    }

    private void renderBox() {
        parent.push();
        parent.rectMode(PApplet.CENTER);
        parent.stroke(View.COLORS.PRIMARY);
        parent.fill(View.COLORS.PRIMARY);
        parent.rect(pos.x, pos.y, w, h);
        parent.pop();
    }

    private void renderLifeBar() {
        parent.push();
        parent.strokeWeight(2);
        parent.stroke(View.COLORS.PRIMARY_DARK);
        parent.strokeWeight(5);
        parent.strokeCap(PApplet.SQUARE);
        float yOffset = h / 2 - 2;
        parent.line(pos.x - w / 2, pos.y + yOffset, pos.x + calcLineBarLength() - w / 2, pos.y + yOffset);
        parent.pop();
    }

    public void renderContent() {
        parent.push();
        parent.fill(255);
        parent.textAlign(PApplet.CENTER, PApplet.BOTTOM);
        parent.textSize(14);
        parent.text(content, parent.width / 2f, parent.height / 2f + 10);
        parent.pop();
    }

    public void update() {
        fadeout--;
    }

    private float calcLineBarLength() {
        return PApplet.map(fadeout, max_life, 0, w, 0);
    }

    public boolean toDestroy() {
        return fadeout <= 0;
    }
}
