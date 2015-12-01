package whiteBoard;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;


public class DShapeModel implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4348923638727655656L;
	protected int xLoc;
	protected int yLoc;
	protected int width;
	protected int height;
	protected Rectangle bounds;
	private Color boundCol;
	
	DShapeModel(){
		xLoc = 0;
		yLoc = 0;
		width = 0;
		height = 0;
		bounds = new Rectangle(xLoc, yLoc, width, height);
		setColor(Color.GRAY);
	}
	
	DShapeModel(int newX, int newY, int newWidth, int newHeight){
		xLoc = newX;
		yLoc = newY;
		width = newWidth;
		height = newHeight;
		bounds = new Rectangle(xLoc, yLoc, width, height);
		setColor(Color.GRAY);
	}
	
	public int getX(){ return xLoc; }
	
	public int getY(){ return yLoc; }
	
	public int getWidth(){ return width; }
	
	public int getHeight(){ return height; }
	
	public Rectangle getRect(){ return bounds; }
	
	public Color getColor(){ return boundCol; }
	
	public void setX(int newX){ xLoc = newX; }
	
	public void setY(int newY){ yLoc = newY; }
	
	public void setWidth(int newWidth){ width = newWidth; }
	
	public void setHeight(int newHeight){ height = newHeight; }
	
	public void setRect(Rectangle newRect){ bounds = newRect; }
	
	public void setColor(Color newCol){ boundCol = newCol; }
	
}
