package whiteBoard;

import java.awt.Graphics;

public class DRect extends DShape{
	
	DRect(){
		shape = new DRectModel();
	}

	protected void setShape(DRectModel newShape){
		shape = newShape;
	}
	
	protected void draw(Graphics g){
		g.drawRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
		g.setColor(shape.getColor());
		g.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}
}
