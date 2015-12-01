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
	
	protected void paintComponent(){
		for(DShape shape : shapeList){
			shape.draw(getGraphics());
		}
	}
	
	protected void addShape(DShapeModel dsm){
		shapeList.add(new DShape(dsm));
		paintComponent();
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
