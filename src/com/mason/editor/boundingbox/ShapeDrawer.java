package com.mason.editor.boundingbox;

import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public interface ShapeDrawer {
    public void draw(PApplet screen, List<PVector> points);
}
