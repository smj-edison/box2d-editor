package com.mason.editor.boundingbox;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

import processing.core.PApplet;

public class BoundingBoxHandler {
    protected List<BoundingBox> boundingBoxes = new ArrayList<>();
    protected int currentId = 0;
    protected AtomicBoolean drawOutline = new AtomicBoolean(true);
    
    public void addBoundingBox(BoundingBox bb) {
	bb.bbhPosition = boundingBoxes.size();
	boundingBoxes.add(bb);
    }
    
    public void addBoundingBox(BoundingBox bb, int position) {
	bb.bbhPosition = position;
	boundingBoxes.set(position, bb);
    }
    
    public void disableOthers(int index) {
	for(BoundingBox bb : boundingBoxes) {
	    if(bb != null) {
		if(bb.bbhPosition == index) {
	            continue;
	        }
	        
	        bb.selected = false;
	    }
	}
    }
    
    public boolean mousePress(PApplet screen, float tx, float ty, float scale) {
	ListIterator<BoundingBox> backwards = boundingBoxes.listIterator(boundingBoxes.size());
	
	while(backwards.hasPrevious()) {
	    BoundingBox bb = backwards.previous();
	    
	    if(bb != null && bb.selected && bb.mousePressed(screen, tx, ty, scale)) {
		return true;
	    }
	}
	
	while(backwards.hasPrevious()) {
	    BoundingBox bb = backwards.previous();
	    
	    if(bb != null && !bb.selected && bb.mousePressed(screen, tx, ty, scale)) {
		return true;
	    }
	}
	
	return false;
    }
    
    public void mouseDrag(PApplet screen, float tx, float ty, float scale) {
	for(BoundingBox bb : boundingBoxes) {
	    if(bb != null && bb.selected) {
		bb.mouseDragged(screen, tx, ty, scale);
	    }
	}
    }
    
    public void mouseRelease() {
	for(BoundingBox bb : boundingBoxes) {
	    if(bb != null) {
		bb.mouseReleased();
	    }
	}
    }
    
    public void draw(PApplet screen, float tx, float ty, float scale) {
	for(BoundingBox bb : boundingBoxes) {
	    if(bb != null && bb.shouldBeDrawn(-tx, -ty, -tx + screen.width / scale, -ty + screen.height / scale)) {
	        bb.drawShape(screen);
	    }
	}
	
	drawOutline.set(true);
	
	ListIterator<BoundingBox> backwards = boundingBoxes.listIterator(boundingBoxes.size());
	
	while(backwards.hasPrevious()) {
	    BoundingBox bb = backwards.previous();
	    
	    if(bb != null && bb.drawBoundingBox(screen, tx, ty, scale, drawOutline)) {
		return;
	    }
	}
	
	while(backwards.hasPrevious()) {
	    BoundingBox bb = backwards.previous();
	    
	    if(bb != null && bb.shouldBeDrawn(-tx, -ty, -tx + screen.width / scale, -ty + screen.height / scale) && !bb.selected && bb.drawBoundingBox(screen, tx, ty, scale, drawOutline)) {
		return;
	    }
	}
    }
}
