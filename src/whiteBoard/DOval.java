package whiteBoard;

import java.awt.Graphics;
import java.awt.Rectangle;

public class DOval extends DShape{

	@Override
	void draw(Graphics g) {
		g.setColor(getShapeModel().getColor());
		Rectangle position = ((DOvalModel)getShapeModel()).getRect();
		g.fillOval(position.x, position.y, position.width, position.height);
	}
	

}
