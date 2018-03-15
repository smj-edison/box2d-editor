package com.mason.editor.shape;

public interface Shape {
    public void mousePressed(int mouseX, int mouseY);
    
    public void mouseReleased(int mouseX, int mouseY);
    
    public void mouseDragged(int mouseX, int mouseY);
    
    public void mouseMoved(int mouseX, int mouseY);
    
    public void keyPressed(int keyCode, char key);
}
