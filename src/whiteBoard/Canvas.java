package whiteBoard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

import javax.swing.*;

import java.beans.*;
import java.io.*;
import java.net.*;

public class Canvas extends JPanel{
	
	protected ArrayList<DShape> shapeList;
	protected DShape selectedShape;
	protected int shapeIdNum;
	protected Repositioner position;
	
	//Listener ArrayLists
	protected ArrayList<ShapeListListener> shapeListListeners;
	protected ArrayList<SelectionListener> selectionListeners;
	
	//System variables
	private java.util.List<ObjectOutputStream> outStreams =
            new ArrayList<ObjectOutputStream>();
	boolean dirty = false;
	static String port_default = "8001";
	static String host_default = "127.0.0.1";
	private ClientHandler clientHandler;
    private ServerAccepter serverAccepter;
    String mode = "";
    String[] cmdList = {"add", "remove", "front", "back", "change"};
	
	//internal interface to keep track of shapes
	protected static interface ShapeListListener{
		public void shapeAdded(DShape shape);
		public void shapeRemoved(DShape shape);
	}
	
	public void addShapeListListener(ShapeListListener listener){
		shapeListListeners.add(listener);
	}
	
	public boolean removeShapeListListener(ShapeListListener listener){
		return shapeListListeners.remove(listener);
	}
	
	//internal interface to keep track of selection possibilities
	protected static interface SelectionListener{
		public void selectionChanged(DShape newShape, DShape oldShape);
	}
	
	public void addSelectionListener(SelectionListener listener){
		selectionListeners.add(listener);
	}
	
	public boolean removeSelectionListener(SelectionListener listener){
		return selectionListeners.remove(listener);
	}
	
	//nested anonymous class to keep track of repositioning/resizing mouse events
	protected class Repositioner{
		Point startPt;
		Point resizeStationaryKnob;
		Point resizeCursorKnob;
		Point startShapeLocation;
		
		public boolean isRepositioning(){
			return startPt != null;
		}
		
		public boolean isResizing(){
			return isRepositioning() && resizeStationaryKnob != null;
		}
		
		public boolean isMovingShape(){
			return isRepositioning() && resizeStationaryKnob == null;
		}
	}
	
	
	Canvas(){
		super();
		shapeIdNum = 1;
		shapeList = new ArrayList<DShape>();
		shapeListListeners = new ArrayList<ShapeListListener>();
		selectionListeners = new ArrayList<SelectionListener>();
		setPreferredSize(new Dimension(400, 400)); //set preferred size to 400x400
		setLayout(new BorderLayout());
		setBackground(new Color(255, 255, 255)); //set background to white
		startMouseListeners();
	}
	
	protected void setSelectedShape(DShape newSelected){
		DShape oldSelected = selectedShape;
		if(newSelected != selectedShape){
			selectedShape = newSelected;
			repaint();
			
			for(SelectionListener listener : selectionListeners)
				listener.selectionChanged(selectedShape, oldSelected);
		}
	}
	
	protected ArrayList<DShape> getShapesList(){ return shapeList;}
	
	protected DShape getShapeByID(int id){
		for(DShape shape : shapeList){
			if(shape.getShapeModel().getShapeID() == id)
				return shape;
		}
		return null;
	}
	
	protected DShape findShape(Point pt){
		DShape match = null;
		for(DShape shape : shapeList){
			if(shape.getShapeModel().getRect().contains(pt))
				match = shape;
		}
		if(match == null)//un-select shape if clicked on white space ; remove knobs
		{
			selectedShape = null;
			repaint();
		}
		return match;
	}
	
	public static DShape newShapeFromDsm(DShapeModel dsm){
		DShape shape = null;
		if(dsm instanceof DRectModel)
			shape = new DRect();
		else if(dsm instanceof DOvalModel)
			shape = new DOval();
		else if(dsm instanceof DLineModel)
			shape = new DLine();
		else if(dsm instanceof DTextModel)
			shape = new DText();
		
		shape.setShapeModel(dsm);
		return shape;
	}
	
	
	
	protected void addShape(DShapeModel dsm){
		DShape shape = newShapeFromDsm(dsm);
		if(dsm.getShapeID() == -1)
			dsm.setShapeID(shapeIdNum++);
		shapeList.add(shape);
		dsm.addDsmListener(new DShapeModel.dsmListener(){

			@Override
			public void dsmChanged(DShapeModel dsm) {
				repaint();
			}
		});
		setSelectedShape(shape);
		repaint();
		for(ShapeListListener listener : shapeListListeners)
			listener.shapeAdded(shape);
	}
	
	protected void removeShape(DShape shape){
		if(shapeList.remove(shape)){
			repaint();
			for(ShapeListListener listener : shapeListListeners)
				listener.shapeRemoved(shape);
		}
	}
	
	protected void shapeToFront(DShape shape){
		if(shapeList.contains(shape))
		{
			System.out.println(shapeList.indexOf(shape));
			shapeList.remove(shape);
			shapeList.add(shape);
			System.out.println(shapeList.indexOf(shape));
			repaint();
		}
	}
	
	protected void shapeToBack(DShape shape){
		if(shapeList.contains(shape))
		{
			System.out.println(shapeList.indexOf(shape));
			for(int i = shapeList.indexOf(shape); i > 0; i--)
			{
				shapeList.set(i, shapeList.get(i-1));
			}
			shapeList.set(0, shape);
			repaint();
		}
	}

	public Rectangle randomBoundsGenerator() {
		Rectangle boundsRect = new Rectangle();
		boundsRect.x = (int)Math.floor(Math.random() * getWidth());
		boundsRect.y = (int)Math.floor(Math.random() * getHeight());
		int maxWidth = getWidth() - boundsRect.x;
		int maxHeight = getWidth() - boundsRect.x;
		boundsRect.width = (int)Math.floor(Math.random() * maxWidth);
		boundsRect.height = (int)Math.floor(Math.random() * maxHeight);
		return boundsRect;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		for(DShape shape : shapeList){
			shape.draw(g);
			if(shape == selectedShape)
				shape.drawKnobs(g);
		}
	}
	
	protected int getDsmIndexNum(DShapeModel dsm){
		int index = 0;
		for(DShape shape : getShapesList()){
			if(shape.getShapeModel() == dsm)
				break;
			++index;
		}
		return index;
	}
	
	protected void startMouseListeners(){
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e){
				setSelectedShape(findShape(e.getPoint()));
			}
			public void mouseEntered(MouseEvent e){};
			public void mouseExited(MouseEvent e){};
			public void mouseReleased(MouseEvent e){ position = null; }
			public void mousePressed(MouseEvent e){
				if(selectedShape != null){
					position = new Repositioner();
					position.startPt = (Point) e.getPoint().clone();
					position.resizeCursorKnob = new Point();
					position.resizeStationaryKnob = 
							selectedShape.knobSelector(position.startPt,position.resizeCursorKnob);
					if(position.resizeStationaryKnob != null){} //do nothing, wait until resize complete
					else if(selectedShape.getShapeModel().getRect().contains(e.getPoint()))
						position.startShapeLocation = selectedShape.getShapeModel().getRect().getLocation();
					else{
						position = null;
						selectedShape = null;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {};
			public void mouseDragged(MouseEvent e){
				if(position != null && position.isRepositioning()){
					Point deltaPosition = new Point
							(e.getPoint().x-position.startPt.x, e.getPoint().y-position.startPt.y);
					if(position.isResizing()){
						selectedShape.knobResizer(position.resizeStationaryKnob,
								position.resizeCursorKnob, deltaPosition);
					}
					else if(position.isMovingShape()){
						Point newLoc = new Point
								(position.startShapeLocation.x + deltaPosition.x,
										position.startShapeLocation.y + deltaPosition.y);
						selectedShape.getShapeModel().setLocation(newLoc);
					}
				}
			}
		});
	}
	
	private void clear() {
		shapeList.clear();
		dirty = false;
		repaint();
	}
	
	// Storage, Networking related methods	
		public void save(File file){
			try {
				XMLEncoder xmlOut = new XMLEncoder(
			            new BufferedOutputStream(
			            new FileOutputStream(file)));
				
				DShape[] shapes = shapeList.toArray(new DShape[0]); 
				DShapeModel[] shapeModels = new DShapeModel[shapes.length];
				for (int i = 0; i < shapes.length; i++){
					shapeModels[i] = shapes[i].shapeModel;
				}
				
				xmlOut.writeObject(shapeModels);
				xmlOut.close();
				dirty = false;
			}
			catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
		
		public void open(File file) {
			DShapeModel[] shapeModels = null;
			try {
	            // Create an XMLDecoder around the file
	            XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(
	            new FileInputStream(file)));
	            shapeModels = (DShapeModel[]) xmlIn.readObject();
	            xmlIn.close();
	            clear();
	            for(DShapeModel dm:shapeModels) {
	                addShape(dm);
	            }
	            dirty = false;
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
		// Send the new changes from server to all the clients
		public synchronized void sendToAllRemotes(int cmdIndex, DShapeModel target) {
			OutputStream memStream = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(memStream);
			
			encoder.writeObject(target);
			encoder.close();
			String xmlString = memStream.toString();
			//System.out.println(xmlString);
			// Push xmlString of DShapeModel object to all clients
			Iterator<ObjectOutputStream> it = outStreams.iterator();
			while (it.hasNext()) {
				ObjectOutputStream out = it.next();
				try {
					out.writeObject(new String(cmdList[cmdIndex]));
					out.flush();
					out.writeObject(xmlString);
					out.flush();
				}
				catch (Exception ex) {
					ex.printStackTrace();
					it.remove();
					// Cute use of iterator and exceptions --
					// drop that socket from list if have probs with it
				}
			}	        
		}
		// Send the current settings to the new added client
		public synchronized void sendAllToRemote(ObjectOutputStream outStr) {
			//Convert all the DShapeModel objects into a xmlString
			//There are potential redundancies in here
			OutputStream memStream = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(memStream);
			DShape[] shapes = shapeList.toArray(new DShape[0]);
			DShapeModel[] shapeModels = new DShapeModel[shapes.length];
			for (int i = 0; i < shapes.length; i++){
				shapeModels[i] = shapes[i].shapeModel;
			}
			encoder.writeObject(shapeModels);
			encoder.close();
			String xmlString = memStream.toString();
			// Push xmlString of DShapeModel objects to all clients
			try	{
				outStr.writeObject(xmlString);
				outStr.flush();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
		
		public synchronized void addOutStream(ObjectOutputStream outStr) {
	    	outStreams.add(outStr);
	    }
		
		public void doServer() {
	    	//status.setText("Start server");
	    	mode = new String("Server");
	        String result = JOptionPane.showInputDialog("Run server on port", port_default);
	        if (result!=null) {
	            System.out.println("server: start");
	            serverAccepter = new ServerAccepter(Integer.parseInt(result.trim()));
	            serverAccepter.start();
	        }
	  	}
	 
	  	public void doClient() {
	    	//status.setText("Start client");
	    	mode = new String("Client");
	        String result = JOptionPane.showInputDialog("Connect to host:port", 
	        											host_default + ":" + port_default);
	        if (result != null) {
	            String[] parts = result.split(":");
	            System.out.println("client: start");
	            clientHandler = new ClientHandler(parts[0].trim(), Integer.parseInt(parts[1].trim()));
	            clientHandler.start();
	        }
	 	} 
	
	/**
     * Internal class to handle connection, and function for Client.
     * Client will run in Read-only mode, so it only listens to changes from Server.
     */
  	private class ClientHandler extends Thread {
  		private String host;
  		private int port;
  		//DotModel[] dotArray = null;
  		ClientHandler(String host, int port) {
  			this.host = host;
  			this.port = port;
  		}
	
  		public void run(){
  			try {
  				Socket toServerSock = new Socket(host, port);
  				ObjectInputStream fromServer = new ObjectInputStream(toServerSock.getInputStream());
  				System.out.println("client connected " + host + port);
  				//Update the whiteboard from server
  				clear();
  				String xmlString = (String) fromServer.readObject();
  				XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
  				DShapeModel[] ShapeModels = (DShapeModel[]) decoder.readObject();
  				decoder.close();
  				for (DShapeModel dsm:ShapeModels){
  					addShape(dsm);
  				}
  				//Listen to new changes from the server
  				while (true) {
  					String cmd = (String) fromServer.readObject();
  					xmlString = (String) fromServer.readObject();
  					decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
  					DShapeModel newDSM = (DShapeModel) decoder.readObject();
  					decoder.close();
  					//TODO: Base on the cmd String, modify the newDSM correctly
  				}
  			}
  			catch(Exception ex) {
  				ex.printStackTrace();
  			}
  		}
  	 }
 
	/**
	 * Internal class to handle connections, and function for Server.
	 * Server is write-only, so it won't handle any changes from the clients.
	 */
  	private class ServerAccepter extends Thread {
	  	private int port;
	  	ServerAccepter(int port){
		this.port = port;
	  	}
	  	public void run() {
	  		try {
	  			ServerSocket serverSocket = new ServerSocket(port);
	  			while(true) {
	  				Socket toClientSock = null;
	  				toClientSock = serverSocket.accept();
	  				System.out.println("Server: client connected");
	  				ObjectOutputStream newClientOut = new ObjectOutputStream(toClientSock.getOutputStream());
	  				//Send the current config to the newly added Client
	  				sendAllToRemote(newClientOut);
	  				//Establish new output stream to the new added client
	  				addOutStream(newClientOut);
	  			}
	  		}
	  		catch (IOException ex) {
			ex.printStackTrace();
	  		}
	  	}
  	}
}
