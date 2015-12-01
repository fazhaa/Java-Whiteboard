package whiteBoard;

import java.awt.Graphics;

public class DOval extends DShape{
	
	DOval(){
		shape = new DOvalModel();
	}
	
	protected void draw(Graphics g){
		g.drawRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
		g.setColor(shape.getColor());
		g.fillOval(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}

}
