package whiteBoard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel{
	
	protected ArrayList<DShape> shapeList;
	protected DShape selectedShape;
	protected int shapeIdNum;
	protected Repositioner position;
	
	//Listener ArrayLists
	protected ArrayList<ShapeListListener> shapeListListeners;
	protected ArrayList<SelectionListener> selectionListeners;
	
	//internal interface to keep track of shapes
	protected static interface ShapeListListener{
		public void shapeAdded(DShape shape);
		public void shapeRemoved(DShape shape);
	}
	
	public void addShapeListListener(ShapeListListener listener){
		shapeListListeners.add(listener);
	}
	
	public boolean removeShapeListListener(ShapeListListener listener){
		return shapeListListeners.remove(listener);
	}
	
	//internal interface to keep track of selection possibilities
	protected static interface SelectionListener{
		public void selectionChanged(DShape newShape, DShape oldShape);
	}
	
	public void addSelectionListener(SelectionListener listener){
		selectionListeners.add(listener);
	}
	
	public boolean removeSelectionListener(SelectionListener listener){
		return selectionListeners.remove(listener);
	}
	
	//nested anonymous class to keep track of repositioning/resizing mouse events
	protected class Repositioner{
		Point startPt;
		Point resizeStationaryKnob;
		Point resizeCursorKnob;
		Point startShapeLocation;
		
		public boolean isRepositioning(){
			return startPt != null;
		}
		
		public boolean isResizing(){
			return isRepositioning() && resizeStationaryKnob != null;
		}
		
		public boolean isMovingShape(){
			return isRepositioning() && resizeStationaryKnob == null;
		}
	}
	
	
	Canvas(){
		super();
		shapeIdNum = 1;
		shapeList = new ArrayList<DShape>();
		shapeListListeners = new ArrayList<ShapeListListener>();
		selectionListeners = new ArrayList<SelectionListener>();
		setPreferredSize(new Dimension(400, 400)); //set preferred size to 400x400
		setLayout(new BorderLayout());
		setBackground(new Color(255, 255, 255)); //set background to white
		startMouseListeners();
	}
	
	protected void setSelectedShape(DShape newSelected){
		DShape oldSelected = selectedShape;
		if(newSelected != selectedShape){
			selectedShape = newSelected;
			repaint();
			
			for(SelectionListener listener : selectionListeners)
				listener.selectionChanged(selectedShape, oldSelected);
		}
	}
	
	protected ArrayList<DShape> getShapesList(){ return shapeList;}
	
	protected DShape getShapeByID(int id){
		for(DShape shape : shapeList){
			if(shape.getShapeModel().getShapeID() == id)
				return shape;
		}
		return null;
	}
	
	protected DShape findShape(Point pt){
		DShape match = null;
		for(DShape shape : shapeList){
			if(shape.getShapeModel().getRect().contains(pt))
				match = shape;
		}
		return match;
	}
	
	public static DShape newShapeFromDsm(DShapeModel dsm){
		DShape shape = null;
		if(dsm instanceof DRectModel)
			shape = new DRect();
		else if(dsm instanceof DOvalModel)
			shape = new DOval();
		
		shape.setShapeModel(dsm);
		return shape;
	}
	
	
	
	protected void addShape(DShapeModel dsm){
		DShape shape = newShapeFromDsm(dsm);
		if(dsm.getShapeID() == -1)
			dsm.setShapeID(shapeIdNum++);
		shapeList.add(shape);
		dsm.addDsmListener(new DShapeModel.dsmListener(){

			@Override
			public void dsmChanged(DShapeModel dsm) {
				repaint();
			}
		});
		setSelectedShape(shape);
		repaint();
		for(ShapeListListener listener : shapeListListeners)
			listener.shapeAdded(shape);
	}
	
	protected void removeShape(DShape shape){
		if(shapeList.remove(shape)){
			repaint();
			for(ShapeListListener listener : shapeListListeners)
				listener.shapeRemoved(shape);
		}
	}

	public Rectangle randomBoundsGenerator() {
		Rectangle boundsRect = new Rectangle();
		boundsRect.x = (int)Math.floor(Math.random() * getWidth());
		boundsRect.y = (int)Math.floor(Math.random() * getHeight());
		int maxWidth = getWidth() - boundsRect.x;
		int maxHeight = getWidth() - boundsRect.x;
		boundsRect.width = (int)Math.floor(Math.random() * maxWidth);
		boundsRect.height = (int)Math.floor(Math.random() * maxHeight);
		return boundsRect;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		for(DShape shape : shapeList){
			shape.draw(g);
			if(shape == selectedShape)
				shape.drawKnobs(g);
		}
	}
	
	protected int getDsmIndexNum(DShapeModel dsm){
		int index = 0;
		for(DShape shape : getShapesList()){
			if(shape.getShapeModel() == dsm)
				break;
			++index;
		}
		return index;
	}
	
	protected void startMouseListeners(){
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e){
				setSelectedShape(findShape(e.getPoint()));
			}
			public void mouseEntered(MouseEvent e){};
			public void mouseExited(MouseEvent e){};
			public void mouseReleased(MouseEvent e){ position = null; }
			public void mousePressed(MouseEvent e){
				if(selectedShape != null){
					position = new Repositioner();
					position.startPt = (Point) e.getPoint().clone();
					position.resizeCursorKnob = new Point();
					position.resizeStationaryKnob = 
							selectedShape.knobSelector(position.startPt,position.resizeCursorKnob);
					if(position.resizeStationaryKnob != null){} //do nothing, wait until resize complete
					else if(selectedShape.getShapeModel().getRect().contains(e.getPoint()))
						position.startShapeLocation = selectedShape.getShapeModel().getRect().getLocation();
					else{
						position = null;
						selectedShape = null;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {};
			public void mouseDragged(MouseEvent e){
				if(position != null && position.isRepositioning()){
					Point deltaPosition = new Point
							(e.getPoint().x-position.startPt.x, e.getPoint().y-position.startPt.y);
					if(position.isResizing()){
						selectedShape.knobResizer(position.resizeStationaryKnob,
								position.resizeCursorKnob, deltaPosition);
					}
					else if(position.isMovingShape()){
						Point newLoc = new Point
								(position.startShapeLocation.x + deltaPosition.x,
										position.startShapeLocation.y + deltaPosition.y);
						selectedShape.getShapeModel().setLocation(newLoc);
					}
				}
			}
		});
	}

}
