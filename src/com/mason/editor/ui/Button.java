package com.mason.editor.ui;

import com.mason.editor.font.UIFont;

import processing.core.PApplet;

public class Button extends RectangularWidget {
    public String buttonText = "";
    public int textSize = 12;
        
    public Button(String id, PApplet screen, int x, int y, int width, int height, String buttonText, int textSize, UIFont font) {
    	super(id, screen, x, y, width, height, font);
    	
    	this.buttonText = buttonText;
    	this.textSize = textSize;
    }
    
    public Button(String id, PApplet screen, int x, int y, int width, int height, String buttonText, int textSize) {
    	super(id, screen, x, y, width, height, UIFont.normal.clone());
    	
    	this.buttonText = buttonText;
    	this.textSize = textSize;
    }
    
    public Button(String id, PApplet screen, int x, int y, int width, int height, String buttonText) {
    	super(id, screen, x, y, width, height, UIFont.normal.clone());
    	
    	this.buttonText = buttonText;
    }
    
    public Button(String id, PApplet screen, int x, int y, int width, int height) {
    	super(id, screen, x, y, width, height, UIFont.normal.clone());
    }
    
    @Override
    public void draw() {
	font.applyFont(screen);
	screen.rect(x,  y, width, height, font.roundedness);
	screen.fill(font.stroke);
	screen.textAlign(PApplet.CENTER, PApplet.CENTER);
	screen.textSize(textSize);
	screen.text(buttonText, x + width / 2, y + height / 2);
    }
    
    @Override
    public boolean mousePressed() {
	if(screen.mouseX > x && screen.mouseX < x + width && screen.mouseY > y && screen.mouseY < y + height) {
	    triggerEvent("mousedown", this);
	    return true;
	}
	
	return false;
    }
}
