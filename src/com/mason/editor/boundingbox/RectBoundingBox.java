package com.mason.editor.boundingbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mason.editor.math.Collision;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class RectBoundingBox extends BoundingBox {
    private float x;
    private float y;
    private float width;
    private float height;
    
    private float oldX;
    private float oldY;
    private float oldWidth;
    private float oldHeight;
    
    private float rotation = 0;
    
    private boolean dragging = false;
    //private int draggingPoint = -1;
    private PVector pointTouched = null;
    private PVector dragNodePoint = null;
    private int dragNodeIndex = -1;
    
    private PVector leftUp;
    private PVector middleUp;
    private PVector rightUp;
    private PVector rightMiddle;
    private PVector rightDown;
    private PVector middleDown;
    private PVector leftDown;
    private PVector leftMiddle;
    
    private PVector rotationArm;
    
    protected List<PVector> points = new ArrayList<>();
    
    private Settings settings;
    
    private float cosine = 1;
    private float sine = 0;
    
    private float invcosine = 1;
    private float invsine = 0;
    
    private float lastMouseX = 0;
    private float lastMouseY = 0;
    
    private PVector AABBLeftUp;
    private PVector AABBRightDown;
    
    private float AABBWidth;
    private float AABBHeight;
    
    private ShapeDrawer shapeDrawer;
    
    public RectBoundingBox(float x, float y, float width, float height, float scale, Settings settings, ShapeDrawer shapeDrawer) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	
	oldWidth = width;
	oldHeight = height;
	
	leftUp = new PVector(x - width / 2, y - height / 2);
	middleUp = new PVector(x, y - height / 2);
	rightUp = new PVector(x + width / 2, y - height / 2);
	rightMiddle = new PVector(x + width / 2, y);
	rightDown = new PVector(x + width / 2, y + height / 2);
	middleDown = new PVector(x, y + height / 2);
	leftDown = new PVector(x - width / 2, y + height / 2);
	leftMiddle = new PVector(x - width / 2, y);
	
	//Divide by scale, so that the rotation arm is always the same length (no matter what scale)
	rotationArm = new PVector(x, middleUp.y - (settings.rotationArmLength / scale));
	
	points.add(leftUp);
	points.add(middleUp);
	points.add(rightUp);
	points.add(rightMiddle);
	points.add(rightDown);
	points.add(middleDown);
	points.add(leftDown);
	points.add(leftMiddle);
	
	points.add(rotationArm);
	
	AABBLeftUp = leftUp.copy();
	AABBRightDown = rightDown.copy();
	
	AABBWidth = width;
	AABBHeight = height;
	
	this.settings = settings;
	
	this.shapeDrawer = shapeDrawer;
    }
    
    @Override
    public float getX() {
	return x;
    }

    @Override
    public float getY() {
	return y;
    }

    @Override
    public float getWidth() {
	return width;
    }

    @Override
    public float getHeight() {
	return height;
    }

    @Override
    public float getAABBWidth() {
	return AABBWidth;
    }

    @Override
    public float getAABBHeight() {
	return AABBHeight;
    }

    @Override
    public PVector getAABBLeftUp() {
	return AABBLeftUp.copy();
    }

    @Override
    public PVector getAABBRightDown() {
	return AABBRightDown.copy();
    }

    @Override
    public PVector getPosition() {
	return new PVector(x, y);
    }

    @Override
    public void setPosition(PVector vec) {
	setPosition(vec.x, vec.y);
    }

    @Override
    public void setPosition(float x, float y) {
	float changeX = this.x - x;
	float changeY = this.y - y;
	
	for(PVector point : points) {
	    point.x += changeX;
	    point.y += changeY;
	}
	
	AABBLeftUp.x += changeX;
	AABBRightDown.x += changeX;
	    
	AABBLeftUp.y += changeY;
	AABBRightDown.y += changeY;
	
	this.x = x;
	this.y = y;
    }

    @Override
    public void setRotation(float angle, float scale) {
	rotation = angle;
	
	cosine =    (float) Math.cos( rotation);
	sine =      (float) Math.sin( rotation);
	invcosine = (float) Math.cos(-rotation);
	invsine =   (float) Math.sin(-rotation);
	
	calculate(scale);
    };
    
    public void correctHeight() {
	width *= -1;
	height = Math.abs(height);
	rotation += Math.PI;
	
	cosine *= -1;
	sine *= -1;
	invcosine *= -1;
	invsine *= -1;
	
	oldWidth *= -1;
	oldHeight *= -1;
    };
    
    @Override
    public void calculate(float scale) {
	if(height < 0) {
	    correctHeight();
	}
	
	//Preserve pointers in the array
	PVector temp;
	
	temp = rotate(-width / 2, -height / 2, cosine, sine);
	leftUp.x = temp.x;
	leftUp.y = temp.y;
	
	temp = rotate( width / 2, -height / 2, cosine, sine);
	rightUp.x = temp.x;
	rightUp.y = temp.y;
	
	temp = rotate( width / 2,  height / 2, cosine, sine);
	rightDown.x = temp.x;
	rightDown.y = temp.y;
	
	temp = rotate(-width / 2,  height / 2, cosine, sine);
	leftDown.x = temp.x;
	leftDown.y = temp.y;
	
	temp = rotate(0, -height / 2 - settings.rotationArmLength / scale, cosine, sine);
	rotationArm.x = temp.x;
	rotationArm.y = temp.y;
	
	leftUp.x    += x;
	rightUp.x   += x;
	rightDown.x += x;
	leftDown.x  += x;
	
	leftUp.y    += y;
	rightUp.y   += y;
	rightDown.y += y;
	leftDown.y  += y;
	
	rotationArm.x += x;
	rotationArm.y += y;
	
	//Quicker to take average
	middleUp.x = (leftUp.x + rightUp.x) / 2;
	middleUp.y = (leftUp.y + rightUp.y) / 2;
	
	rightMiddle.x = (rightUp.x + rightDown.x) / 2;
	rightMiddle.y = (rightUp.y + rightDown.y) / 2;
	
	middleDown.x = (rightDown.x + leftDown.x) / 2;
	middleDown.y = (rightDown.y + leftDown.y) / 2;
	
	leftMiddle.x = (leftDown.x + leftUp.x) / 2;
	leftMiddle.y = (leftDown.y + leftUp.y) / 2;
	
	AABBWidth = Math.abs(width * cosine) + Math.abs(height * sine);
	AABBHeight = Math.abs(width * sine) + Math.abs(height * cosine);
	
	AABBLeftUp.x = x - AABBWidth / 2;
	AABBLeftUp.y = y - height / 2;
	
	AABBRightDown.x = x + width / 2;
	AABBRightDown.y = y + height / 2;
    };
    
    public PVector rotate(float x, float y, float cosine, float sine) {
	return new PVector(x * cosine - y * sine, y * cosine + sine * x);
    };
    
    @Override
    public void drawShape(PApplet screen) {
	shapeDrawer.draw(screen, points);
    }
    
    //Use AtomicBoolean for a reference that a BoundingBoxHandler will pass in
    @Override
    public boolean drawBoundingBox(PApplet screen, float tx, float ty, float scale, AtomicBoolean drawOutline) {
	boolean drawTheRest = true;
	
	float x = 0;
	float y = 0;
	    
	boolean collided = false;
	    
	if(selected) {
	    screen.stroke(settings.lineColor);
	    screen.strokeWeight(settings.lineThickness / scale);
	    screen.line(leftUp.x, leftUp.y, rightUp.x, rightUp.y);
	    screen.line(rightUp.x, rightUp.y, rightDown.x, rightDown.y);
	    screen.line(rightDown.x, rightDown.y, leftDown.x, leftDown.y);
	    screen.line(leftDown.x, leftDown.y, leftUp.x, leftUp.y);
	    
	    if(dragging) {
		screen.cursor(PConstants.CROSS);
		//TODO: find all cursors
	        //setCursor(draggingPoint, rotation, width, oldWidth < 0);
	        drawTheRest = false;
	    }
	    
	    if(drawTheRest) {
		screen.line(middleUp.x, middleUp.y, rotationArm.x, rotationArm.y);
	        
	        screen.noStroke();
	        screen.fill(settings.nodeColor);
	        
	        //get active hover point
	        for(PVector point : points) {
	            screen.ellipse(point.x, point.y, settings.pointSize / scale, settings.pointSize / scale);
	            
	            x = point.x;
	            y = point.y;
	                
	            if(Collision.mouseNodeCollide(screen.mouseX, screen.mouseY, x + tx, y + ty, scale, settings.pointSize) && !collided) {
	        	screen.cursor(PConstants.CROSS);
	        	//TODO: find all cursors
	                //setCursor(i, rotation, width, oldWidth < 0);
	                collided = true;
	            }
	        }
	    }
	    
	    screen.fill(settings.adjustNodeColor);
	    screen.noStroke();
	    screen.stroke(150);
	    
	    if(Collision.isInPolygon(screen.mouseX / scale - tx, screen.mouseY / scale - ty, leftUp.copy(), rightUp.copy(), rightDown.copy(), leftDown.copy()) && drawOutline.get()) {
		screen.noFill();
	        screen.strokeWeight(5 / scale);
	        
	        if(!selected) {	            
	            screen.quad(leftUp.x, leftUp.y, rightUp.x, rightUp.y, rightDown.x, rightDown.y, leftDown.x, leftDown.y);
	            
	            drawOutline.set(false);
	        }
	        
	        return true;
	    }
	}
	
	return false;
    }
    
    public int getHoverPoint(float mouseX, float mouseY, float tx, float ty, float scale) {
	/*var found = false;
	var foundPoint = false;
	
	var x = 0;
	var y = 0;
	
	for(var i = 0; i < this.adjustablePoints.length; i++) {
	    var temp = getAdjustablePosition(this.xyPoints, this.adjustablePoints[i]);
	    
	    x = temp[0];
	    y = temp[1];
	    
	    if(mouseNodeCollide(x + tx, y + ty, scale)) {
	        this.adjustablePoints[i][2] = i + 9;
	        this.possibleNode = this.adjustablePoints[i];
	         this.dragNode = [x, y, this.adjustablePoints[i][2]];
	        
	        found = true;
	        foundPoint = true;
	    }
	}
	
	for(var i = 0; i < this.xyPoints.length; i++) {
	    if(!foundPoint && mouseNodeCollide(this.xyPoints[i][0] + tx, this.xyPoints[i][1] + ty, scale)) {
	        this.possibleNode = this.xyPoints[i];
	        this.dragNode = [this.xyPoints[i][0], this.xyPoints[i][1], this.xyPoints[i][2]];
	        found = true;
	    }
	}
	
	if(!found && this.possibleNode) {
	    this.possibleNode = null;
	    this.dragNode = null;
	}*/
	
	for(int i = 0; i < points.size(); i++) {
	    if(Collision.mouseNodeCollide(mouseX, mouseY, this.points.get(i).x + tx, this.points.get(i).y + ty, scale, settings.pointSize)) {
	        return i;
	    }
	}
	
	return -1;
    }

    @Override
    public boolean mousePressed(PApplet screen, float tx, float ty, float scale) {
	int point = getHoverPoint(screen.mouseX, screen.mouseY, tx, ty, scale);
	
	dragNodeIndex = point;
	
	
	if(point >= 0 && selected) {
	    dragNodePoint = points.get(point).copy();
	    
	    oldWidth = width;
	    oldHeight = height;
	    oldX = x;
	    oldY = y;
	    dragging = true;
	    
	    return true;
	} else if(Collision.isInPolygon(screen.mouseX / scale - tx, screen.mouseY / scale - ty, leftUp.copy(), rightUp.copy(), rightDown.copy(), leftDown.copy())) {
	    oldX = x;
	    oldY = y;
	    
	    lastMouseX = screen.mouseX + tx;
	    lastMouseY = screen.mouseY + ty;
	    
	    selected = true;
	    
	    /*if(bbh != null) {
	        bbh.disableOthers(this.id);
	    }*/
	    
	    if(selected) {
	        pointTouched = new PVector(screen.mouseX, screen.mouseY);
	        
	        oldX = x;
	        oldY = y;
	    }
	    
	    return true;
	} else {
	    selected = false;
	}
	
	return false;
    }

    @Override
    public boolean mouseDragged(PApplet screen, float tx, float ty, float scale) {
	if(pointTouched != null) {
	    float changeX = ((screen.mouseX + tx) - lastMouseX) / scale;
	    float changeY = ((screen.mouseY + ty) - lastMouseY) / scale;
	    
	    x += changeX;
	    y += changeY;
	    
	    for(PVector point : points) {
		point.x += changeX;
		point.y += changeY;
	    }
	    
	    AABBLeftUp.x += changeX;
	    AABBLeftUp.y += changeX;
	    
	    AABBRightDown.x += changeY;
	    AABBRightDown.y += changeY;
	    
	    lastMouseX = screen.mouseX + tx;
	    lastMouseY = screen.mouseY + ty;
	    
	    return true;
	} else if(dragNodePoint != null && dragging) {
	    /*if(dragNodeIndex > 8) {
	        var px = (this.x + tx - mouseX / scale) / scale;
	        var py = (this.y + ty - mouseY / scale) / scale;
	        
	            var node = this.adjustablePoints[this.dragNode[2] - 9];
	            var temp = rot(px * scale, py * scale, this.ico, this.isi);
	            
	            var toMove = node[1];
	            
	            switch(toMove) {
	                case 0:
	                    node[0] = constrain(-temp[0] / this.w, -0.5, 0.5) + 0.5;
	                break;
	                case 1:
	                    node[0] = constrain(-temp[1] / this.h, -0.5, 0.5) + 0.5;
	                break;
	                case 2:
	                    node[0] = constrain(temp[0] / this.w, -0.5, 0.5) + 0.5;
	                break;
	                case 3:
	                    node[0] = constrain(temp[1] / this.h, -0.5, 0.5) + 0.5;
	                break;
	                case 4:
	                    node[0] = constrain(-temp[0] / this.w, -0.5, 0.5) + 0.5;
	                    node[3] = constrain(-temp[1] / this.h, -0.5, 0.5) + 0.5;
	                break;
	            }
	        } else {*/
	    float px = dragNodePoint.x - screen.mouseX / scale + tx;
            float py = dragNodePoint.y - screen.mouseY / scale + ty;
            
            PVector rotateBack = rotate(px, py, invcosine, invsine);
            PVector backAgain;
	    
	    switch(dragNodeIndex) {
	        case 0:
	            backAgain = rotate(-rotateBack.x / 2, -rotateBack.y / 2, cosine, sine);
	            
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            width = oldWidth + rotateBack.x;
	            height = oldHeight + rotateBack.y;
	        break;
	        case 1:
	            backAgain = rotate(0, -rotateBack.y / 2, cosine, sine);
	            
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            height = oldHeight + rotateBack.y;
	        break;
	        case 2:
	            backAgain = rotate(-rotateBack.x / 2, -rotateBack.y / 2, cosine, sine);
	            
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            width = oldWidth - rotateBack.x;
	            height = oldHeight + rotateBack.y;
	        break;
	        case 3:
	            backAgain = rotate(-rotateBack.x / 2, 0, cosine, sine);
	            
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            width = oldWidth - rotateBack.x;
	        break;
	        case 4:
	            backAgain = rotate(-rotateBack.x / 2, -rotateBack.y / 2, cosine, sine);
	                
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            width = oldWidth - rotateBack.x;
	            height = oldHeight - rotateBack.y;
	        break;
	        case 5:
	            backAgain = rotate(0, -rotateBack.y / 2, cosine, sine);
	                
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            height = oldHeight - rotateBack.y;
	        break;
	        case 6:
	            backAgain = rotate(-rotateBack.x / 2, -rotateBack.y / 2, cosine, sine);
	                
	            x = backAgain.x + oldX;
	            y = backAgain.y + oldY;
	            width = oldWidth + rotateBack.x;
	            height = oldHeight - rotateBack.y;
	        break;
	        case 7:
	            backAgain = rotate(-rotateBack.x / 2, 0, cosine, sine);
	                
	            x = backAgain.x + oldX;
		    y = backAgain.y + oldY;
		    width = oldWidth + rotateBack.x;
	        break;
	        case 8:
	            rotation = (float) Math.atan2(screen.mouseY / scale - y - ty, screen.mouseX / scale - x - tx) + PConstants.HALF_PI;
	            
	            cosine    = (float) Math.cos( rotation);
	            sine      = (float) Math.sin( rotation);
	            invcosine = (float) Math.cos(-rotation);
	            invsine   = (float) Math.sin(-rotation);
	        break;
	    }
	    
	    calculate(scale);
	    
	    return true;
	}
	    
	return false;
    }

    @Override
    public void mouseReleased() {
	dragNodePoint = null;
	dragNodeIndex = -1;
	pointTouched = null;
	dragging = false;
    }

    
    @Override
    public boolean shouldBeDrawn(float x1, float x2, float y1, float y2) {
	return leftUp.x < x2 && rightDown.x > x1 && leftUp.y < y2 && rightDown.y > y1;
    }
}
