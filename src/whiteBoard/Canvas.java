package whiteBoard;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel{
	
	protected ArrayList<DShape> shapeList;
	protected DShape selectedShape;
	protected int shapeIdNum;
	
	Canvas(){
		super();
		shapeIdNum = 1;
		shapeList = new ArrayList<DShape>();
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
	
	public static DShape newShapeFromDsm(DShapeModel dsm){
		DShape shape = null;
		if(dsm instanceof DRectModel)
			shape = new DRect();
		else if(dsm instanceof DOvalModel)
			shape = new DOval();
		
		shape.setShapeModel(dsm);
		return shape;
	}
	
	protected void paintComponent(){
		for(DShape shape : shapeList){
			shape.draw(getGraphics());
		}
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

}
