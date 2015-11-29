package whiteBoard;

import java.awt.Graphics;

public class DShape {
	
	private DShapeModel shape;
	
	DShape(){}
	
	protected void setShape(DShapeModel newShape){
		shape = newShape;
	}
	
	protected DShapeModel getShape(){ return shape; }
	
	/*protected void draw(Graphics g){
		g.
	}*/

}
