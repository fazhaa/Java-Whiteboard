package whiteBoard;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class DLine extends DShape{

	@Override
	void draw(Graphics g) {
		g.setColor(getShapeModel().getColor());
		Point start = ((DLineModel)getShapeModel()).getStartPtClone();
		Point end = ((DLineModel)getShapeModel()).getEndPtClone();
		g.drawLine(start.x, start.y, end.x, end.y);
	}
	
	protected ArrayList<Point> calcKnobs(){
		ArrayList<Point> pts = new ArrayList<Point>();
		DLineModel line = (DLineModel)getShapeModel();
		pts.add(new Point(line.getStartPtClone().x, line.getStartPtClone().y));
		pts.add(new Point(line.getEndPtClone().x, line.getEndPtClone().y));
		return pts;
	}
	
	protected Point getOppKnob(int knobInd){ return calcKnobs().get((knobInd+1) % 2);}
	
	protected void knobResizer(Point stationaryKnob, Point selectKnobFirstPos, Point deltaKnob){
		Point startLoc = (Point) stationaryKnob.clone();
		Point endLoc = new Point( selectKnobFirstPos.x+deltaKnob.x,
				selectKnobFirstPos.y+deltaKnob.y);
		DLineModel line = (DLineModel)getShapeModel();
		line.setStartPt(startLoc);
		line.setEndPt(endLoc);
	}
	
	

}
