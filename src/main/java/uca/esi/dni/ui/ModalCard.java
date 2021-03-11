package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ModalCard extends BaseElement {
    private final PImage card;

    public ModalCard(PApplet parent, int x, int y, int w, int h, PImage card) {
        super(parent, new PVector(x, y), w, h);
        this.card = card;
        card.resize(w, h);
    }

    @Override
    public void display() {
        parent.image(card, 0, 0);
    }
}
