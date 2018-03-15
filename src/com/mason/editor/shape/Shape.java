package com.mason.editor.shape;

import processing.core.PApplet;

public interface Shape {
    public void mousePressed(int mouseX, int mouseY);
    
    public void mouseReleased(int mouseX, int mouseY);
    
    public void mouseDragged(int mouseX, int mouseY);
    
    public void mouseMoved(int mouseX, int mouseY);
    
    public void keyPressed(int keyCode, char key, byte[] keysDown);
    
    public void keyTyped(int keyCode, char key, byte[] keysDown);
    
    public void keyReleased(int keyCode, char key, byte[] keysDown);
    
    public void draw(PApplet screen);
    
    public void setMode(ShapeMode mode);
}
