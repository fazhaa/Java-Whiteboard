package whiteBoard;

import java.awt.Graphics;
import java.awt.Rectangle;

public class DRect extends DShape{

	@Override
	void draw(Graphics g) {
		g.setColor(getShapeModel().getColor());
		Rectangle position = ((DRectModel)getShapeModel()).getRect();
		g.fillRect(position.x, position.y, position.width, position.height);
	}
	
	
	
	
}
