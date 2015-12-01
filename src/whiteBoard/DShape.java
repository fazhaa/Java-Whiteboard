package whiteBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class DShape {
	
	public static int KNOB_SIZE = 8;
	protected DShapeModel shapeModel;
	
	abstract void draw(Graphics g);
	
	protected static Rectangle newRectWithPts(Point first, Point second){
		Rectangle rect = new Rectangle();
		if(first.x < second.x)
			rect.x = first.x;
		else rect.x = second.x;
		if(first.y < second.y)
			rect.y = first.y;
		else rect.y = second.y;
		rect.width = Math.abs(second.x - first.x);
		rect.height = Math.abs(second.y - first.y);
		return rect;
	}
	
	protected static Rectangle getKnobRect(Point knobPt){
		return new Rectangle(knobPt.x-(KNOB_SIZE/2), knobPt.y-(KNOB_SIZE/2),
				KNOB_SIZE, KNOB_SIZE);
	}
	
	protected Point getOppKnob(int knobInd){
		return calcKnobs().get((knobInd + 2 ) % 4);
	}
	
	protected DShapeModel getShapeModel(){ return shapeModel; }
	
	//public ArrayList<Point> getKnobs(){ return calcKnobs(); }
	
	protected void setShapeModel(DShapeModel newShape){
		shapeModel = newShape;
	}
	
	protected ArrayList<Point> calcKnobs(){
		ArrayList<Point> knobPoints = new ArrayList<Point>();
		Rectangle bounds = getShapeModel().getRect();
		knobPoints.add(new Point(bounds.x+1, bounds.y+1));
		knobPoints.add(new Point(bounds.x+1, bounds.y+bounds.height-1));
		knobPoints.add(new Point(bounds.x+bounds.width-1, bounds.y+bounds.height-1));
		knobPoints.add(new Point(bounds.x+bounds.width-1, bounds.y+1));
		return knobPoints;
	}
	
	protected void knobResizer(Point stationaryKnob, Point selectKnobFirstPos, Point deltaKnob){
		Rectangle bounds = new Rectangle();
		bounds.width = (stationaryKnob.x-selectKnobFirstPos.x)+ deltaKnob.x;
		bounds.height = (stationaryKnob.y-selectKnobFirstPos.y)+ deltaKnob.y;
		Point newKnobPos = new Point(selectKnobFirstPos.x+deltaKnob.x, 
				selectKnobFirstPos.y+deltaKnob.y);
		getShapeModel().setRect(newRectWithPts(stationaryKnob, newKnobPos));
	}
	
	protected Point knobSelecter(Point click, Point selectedKnob){
		int num = 0;
		ArrayList<Point> currKnobs = calcKnobs();
		for(Point knob : currKnobs){
			Rectangle knobRect = getKnobRect(knob);
			if(knobRect.contains(click)){
				selectedKnob.x = knob.x;
				selectedKnob.y = knob.y;
				Point anchPt = getOppKnob(num);
				return anchPt;
			}
			++num;
		}
		return null;
	}
	
	protected void drawKnobs(Graphics g){
		g.setColor(Color.BLACK);
		ArrayList<Point> pts = calcKnobs();
		for(Point pt : pts)
			drawSingleKnob(g, pt);
	}
	
	protected void drawSingleKnob(Graphics g, Point pt){
		Rectangle rect = getKnobRect(pt);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
	
	

}
