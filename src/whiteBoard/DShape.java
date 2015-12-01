package whiteBoard;

import java.awt.Graphics;

public abstract class DShape {
	
	protected DShapeModel shapeModel;
	abstract void draw(Graphics g);
	
	protected DShapeModel getShapeModel(){ return shapeModel; }
	
	protected void setShapeModel(DShapeModel newShape){
		shapeModel = newShape;
	}

}
