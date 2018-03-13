package com.mason.editor.boundingbox;

public class Settings {
    public int pointSize;
    public int adjustPointSize;
    public int rotationArmLength;
    public int lineThickness;
    
    public int nodeColor;
    public int lineColor;
    public int adjustNodeColor;
    
    public Settings(int pointSize, int adjustPointSize, int rotationArmLength, int lineThickness, int nodeColor, int lineColor, int adjustNodeColor) {
	this.pointSize = pointSize;
	this.adjustPointSize = adjustPointSize;
	this.rotationArmLength = rotationArmLength;
	this.lineThickness = lineThickness;
	
	this.nodeColor = nodeColor;
	this.lineColor = lineColor;
	this.adjustNodeColor = adjustNodeColor;
    }
}
