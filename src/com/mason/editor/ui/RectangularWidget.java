package com.mason.editor.ui;

import com.mason.editor.font.UIFont;

import processing.core.PApplet;

public abstract class RectangularWidget extends Widget {
    public int x;
    public int y;
    public int width;
    public int height;
    
    public RectangularWidget(String id, PApplet screen, int x, int y, int width, int height, UIFont font) {
	super(id, screen, font);
	
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }
    
    public abstract void draw();
}