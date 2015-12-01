package whiteBoard;

import java.awt.Graphics;

public class DShape {
	
	protected DShapeModel shape;
	
	DShape(){
		shape = new DShapeModel();
	}
	
	DShape(DShapeModel dsm){
		shape = dsm;
	}
	
	protected void setShape(DShapeModel newShape){
		shape = newShape;
	}
	
	protected DShapeModel getShape(){ return shape; }
	
	protected void draw(Graphics g){
		g.drawRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
		g.setColor(shape.getColor());
	}

}
