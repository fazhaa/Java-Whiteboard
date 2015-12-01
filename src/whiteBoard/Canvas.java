package whiteBoard;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel{
	
	protected ArrayList<DShape> shapeList;
	protected DShape selectedShape;
	protected int shapeIdNum;
	
	protected ArrayList<ShapeListListener> shapeListListeners;
	
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
	
	Canvas(){
		super();
		shapeIdNum = 1;
		shapeList = new ArrayList<DShape>();
		shapeListListeners = new ArrayList<ShapeListListener>();
		setPreferredSize(new Dimension(400, 400)); //set preferred size to 400x400
		setLayout(new BorderLayout());
		setBackground(new Color(255, 255, 255)); //set background to white
	}
	
	protected void setSelectedShape(DShape newSelected){
		DShape oldSelected = selectedShape;
		if(newSelected != selectedShape){
			selectedShape = newSelected;
			repaint();
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
	
	protected DShape findShape(Point pt){
		DShape match = null;
		for(DShape shape : shapeList){
			if(shape.getShapeModel().getRect().contains(pt))
				match = shape;
		}
		return match;
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

}
