package whiteBoard;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


public class DShapeModel implements Serializable{
	
	
	/**
	 * Auto generated serial version UID
	 */
	private static final long serialVersionUID = -4348923638727655656L;
	protected Rectangle boundsRect;
	protected Color boundCol;
	protected int shapeID;
	protected Collection<dsmListener> dsmEventListeners;
	
	public static interface dsmListener{
		public void dsmChanged(DShapeModel dsm);
	}
	
	DShapeModel(){
		boundsRect = new Rectangle(0, 0, 0, 0);
		boundCol = Color.GRAY;
		dsmEventListeners = new ArrayList<dsmListener>();
		shapeID = -1;
	}
	
	public Rectangle getRect(){ return boundsRect; }
	
	public Color getColor(){ return boundCol; }
	
	public int getShapeID(){ return shapeID; }
	
	public Point getLocation(){ return boundsRect.getLocation(); }
	
	public void setRect(Rectangle newRect){
		if(this.boundsRect != newRect){
			boundsRect = newRect;
			notifyListeners();
		}
	}
	
	public void setColor(Color newCol){ 
		if(this.boundCol != newCol){
			boundCol = newCol;
			notifyListeners();
		}
	}

	public void setShapeID(int newShapeID){
		shapeID = newShapeID;
	}
	
	public void setLocation(Point newLoc){
		boundsRect.x = newLoc.x;
		boundsRect.y = newLoc.y;
		notifyListeners();
	}
	
	protected void notifyListeners(){
		for(dsmListener listener : dsmEventListeners)
			listener.dsmChanged(this);
	}
	
	public void addDsmListener(dsmListener listener){
		dsmEventListeners.add(listener);
	}
	
	public boolean removeDsmListener(dsmListener listener){
		return dsmEventListeners.remove(listener);
	}
	
}
