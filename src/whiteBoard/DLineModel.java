package whiteBoard;

import java.awt.Point;

public class DLineModel extends DShapeModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9182000261369149823L;
	
	Point startPt;
	Point endPt;
	
	public DLineModel(){
		super();
		startPt = new Point();
		endPt = new Point();
	}
	
	protected Point getStartPtClone(){ return new Point(startPt.x, startPt.y); }
	
	protected Point getEndPtClone(){ return new Point(endPt.x, endPt.y); }
	
	protected void setStartPt(Point newStart){
		startPt = (Point) newStart.clone();
		setRect(DShape.newRectWithPts(startPt, endPt));
	}
	
	protected void setEndPt(Point newEnd){
		endPt = (Point) newEnd.clone();
		setRect(DShape.newRectWithPts(startPt, endPt));
	}
	
	protected void setModel(DShapeModel newDsm){
		startPt = ((DLineModel) newDsm).startPt;
		endPt = ((DLineModel) newDsm).endPt;
		super.setModel(newDsm);
	}
	
	

}
