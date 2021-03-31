package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PImage;

public class ModalCard extends BaseElement {
    private final PImage card;

    public ModalCard(PApplet parent, Rectangle rectangle, PImage card) {
        super(parent, rectangle);
        this.card = card;
        card.resize(w, h);
    }

    @Override
    public void display() {
        parent.image(card, 0, 0);
    }
}
