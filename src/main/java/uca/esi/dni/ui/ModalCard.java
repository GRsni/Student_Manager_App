package uca.esi.dni.ui;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * The type Modal card.
 */
public class ModalCard extends BaseElement {
    /**
     * The Card.
     */
    private final PImage card;

    /**
     * Instantiates a new Modal card.
     *
     * @param parent    the parent
     * @param rectangle the rectangle
     * @param card      the card
     */
    public ModalCard(PApplet parent, Rectangle rectangle, PImage card) {
        super(parent, rectangle);
        this.card = card;
        card.resize(w, h);
    }

    /**
     * Display.
     */
    @Override
    public void display() {
        parent.image(card, 0, 0);
    }
}
