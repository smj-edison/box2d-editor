package com.mason.editor.boundingbox;

import java.util.concurrent.atomic.AtomicBoolean;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class BoundingBox {
    public int bbhPosition = 0;
    public boolean selected = false;
    
    public abstract void drawShape(PApplet screen);
    
    public abstract boolean mousePressed(PApplet screen, float tx, float ty, float scale);
    
    public abstract boolean mouseDragged(PApplet screen, float tx, float ty, float scale);
    
    public abstract void mouseReleased();

    public abstract boolean drawBoundingBox(PApplet screen, float tx, float ty, float scale, AtomicBoolean drawOutline);
    
    public abstract float getX();
    
    public abstract float getY();
    
    public abstract float getWidth();
    
    public abstract float getHeight();
    
    public abstract PVector getPosition();
    
    public abstract void setPosition(PVector position);
    
    public abstract void setPosition(float x, float y);
    
    public abstract void setRotation(float angle, float scale);
    
    public abstract float getAABBWidth();

    public abstract float getAABBHeight();
    
    public abstract PVector getAABBLeftUp();
    
    public abstract PVector getAABBRightDown();
    
    public abstract void calculate(float scale);
    
    public abstract boolean shouldBeDrawn(float x1, float x2, float y1, float y2);
}
