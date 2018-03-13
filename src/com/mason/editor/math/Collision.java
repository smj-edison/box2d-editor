package com.mason.editor.math;

import processing.core.PVector;

public class Collision {
    public static boolean isInPolygon(float x, float y, PVector...poly) {
	boolean isIn = false;
	
	for(int i = 0, j = poly.length - 1; i < poly.length; j = i++) {
	    float xi = poly[i].x, yi = poly[i].y;
	    float xj = poly[j].x, yj = poly[j].y;
	    
	    boolean intersect = yi > y != yj > y && x < (xj - xi) * (y - yi) / (yj - yi) + xi;
	    
	    if(intersect) {
	        isIn = !isIn;
	    }
	}
	
	return isIn;
    }
    
    public static boolean mouseNodeCollide(float mouseX, float mouseY, float x, float y, float SCALE, float size) {
	size /= 2;
	
	return dist(mouseX / SCALE, mouseY / SCALE, x, y) * SCALE < size;
    }
    
    public static float distSquare(float x1, float y1, float x2, float y2) {
	x1 -= x2;
	y1 -= y2;
	
	return x1 * x1 + y1 * y1;
    }
    
    public static double dist(float x1, float y1, float x2, float y2) {
	x1 -= x2;
	y1 -= y2;
	
	return Math.sqrt(x1 * x1 + y1 * y1);
    }
}
