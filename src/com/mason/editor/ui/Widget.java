package com.mason.editor.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.mason.editor.event.Action;
import com.mason.editor.font.UIFont;

import processing.core.PApplet;

public abstract class Widget {
    private HashMap<String, Set<Action>> eventListeners = new HashMap<>();
    protected UIFont font;
    protected PApplet screen;
    protected String id;
    
    public String getID() {
	return this.id;
    }
    
    public Widget(String id, PApplet screen, UIFont font) {
	this.id = id;
	this.screen = screen;
	this.font = font;
    }
    
    public void addEventListener(String event, Action action) {
	if(!eventListeners.containsKey(event)) {
	    eventListeners.put(event, new HashSet<>());
	}
	
	eventListeners.get(event).add(action);
    }
    
    public boolean removeEventListener(String event, Action action) {
	if(eventListeners.containsKey(event)) {
	    return eventListeners.get(event).remove(action);
	}
	
	return false;
    }
    
    protected boolean triggerEvent(String event, Widget widget) {
	if(eventListeners.containsKey(event)) {
	    for(Action a : eventListeners.get(event)) {
		a.trigger(widget);
	    }
	}
	
	return false;
    }
    
    public abstract void draw();
    
    public boolean mousePressed() {
	return false;
    };
    
    public boolean mouseClicked() {
	return false;
    };
    
    public boolean mouseReleased() {
	return false;
    };
}
