package com.mason.editor.font;

import processing.core.PApplet;

public class UIFont {
    public int color;
    public int stroke;
    public int strokeWeight;
    public int roundedness;
    
    public static final UIFont normal = new UIFont(color(255, 255, 255), color(0), 2, 3);
    
    public UIFont(int color, int stroke, int strokeWeight, int roundedness) {
	this.color = color;
	this.stroke = stroke;
	this.strokeWeight = strokeWeight;
	this.roundedness = roundedness;
    }
    
    public UIFont() {
	this.color = normal.color;
	this.stroke = normal.stroke;
	this.strokeWeight = normal.strokeWeight;
    }
    
    public UIFont clone() {
	return new UIFont(color, stroke, strokeWeight, roundedness);
    }
    
    public void applyFont(PApplet screen) {
	screen.fill(color);
	screen.stroke(stroke);
	screen.strokeWeight(strokeWeight);
    }
    
    public static int color(int r, int g, int b, int a) {
	a = Math.max(Math.min(a, 255), 0);
	r = Math.max(Math.min(r, 255), 0);
	g = Math.max(Math.min(g, 255), 0);
	b = Math.max(Math.min(b, 255), 0);
	
	return (a << 24) + (r << 16) + (g << 8) + b;
    }
    
    public static int color(int r, int g, int b) {
	return color(r, g, b, 255);
    }
    
    public static int color(int s, int a) {
	return color(s, s, s, a);
    }
    
    public static int color(int s) {
	return color(s, s, s, 255);
    }
}
