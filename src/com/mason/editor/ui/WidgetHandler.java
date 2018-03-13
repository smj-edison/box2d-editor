package com.mason.editor.ui;

import java.util.ArrayList;
import java.util.List;

public class WidgetHandler {
    protected List<Widget> widgets = new ArrayList<>();
    
    public void draw() {
	for(Widget widget : widgets) {
	    widget.draw();
	}
    }
    
    public void mousePressed() {
	for(Widget widget : widgets) {
	    if(widget.mousePressed()) {
		break;
	    }
	}
    }
    
    public void addWidget(Widget widget) {
	widgets.add(widget);
    }
    
    public void removeWidget(Widget widget) {
	widgets.remove(widget);
    }
    
    public void addWidgetAt(Widget widget, int position) {
	widgets.add(position, widget);
    }
    
    public void removeWidgetAt(int position) {
	widgets.remove(position);
    }
}
