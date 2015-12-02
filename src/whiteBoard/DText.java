package whiteBoard;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.LineMetrics;

public class DText extends DShape{

	@Override
	void draw(Graphics g) {
		g.setColor(getShapeModel().getColor());
		g.setFont(getFont(g));
		Point offset = getOffset(g.getFont(), g);
		Point pos = new Point(offset.x+getShapeModel().getRect().x, offset.y+getShapeModel().getRect().y);
		
		Shape tempShape = g.getClip();
		g.setClip(getShapeModel().getRect());
		g.drawString(((DTextModel)getShapeModel()).getText(), pos.x, pos.y);
		g.setClip(tempShape);
	}
	
	protected Point getOffset(Font font, Graphics g){
		Point offset = new Point();
		LineMetrics lMet = g.getFontMetrics(font).getLineMetrics(((DTextModel)getShapeModel()).getText(), g);
		offset.y = (int) -lMet.getDescent();
		offset.y += getShapeModel().getRect().height;
		return offset;
	}
	
	protected Font getFont(Graphics g){
		Rectangle boundsRect = getShapeModel().getRect();
		Font font = Font.decode(((DTextModel) getShapeModel()).getFont());
		font = font.deriveFont(1.0f);
		for(float size=1; true; size=(size*1.1f)+1){
			Font possibleFont = font.deriveFont(size);
			FontMetrics met = g.getFontMetrics(font);
			Rectangle possibleBoundsRect = met.getStringBounds
					(((DTextModel)getShapeModel()).getText(), g).getBounds();
			if(possibleBoundsRect.height > getShapeModel().getRect().height ||
					possibleBoundsRect.width > getShapeModel().getRect().width)
				break;
			font = possibleFont;
		}
		return font;
	}

}
