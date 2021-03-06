package whiteBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.*;

import java.io.*;
@SuppressWarnings("unchecked")
public class WhiteBoard extends JFrame{
	
	
	/**
	 * Default generated serial version UID
	 */
	private static final long serialVersionUID = -3690401036973293224L;
	public static final String[] COL_NAMES = new String[] {"x", "y", "Width", "Height" };
	public static final String[] FONT_NAMES = new String[] {"Font Select"};
	
	private Object[][] rowData;
	private Box shapesBox;
	private Box colorsBox;
	private Box textBox;
	private Box layersBox;
	
	private JButton rect;
	private JButton oval;
	private JButton line;
	private JButton text;
	private JButton moveToFront;
	private JButton moveToBack;
	private JButton removeShape;
	private JTextField textField;
	private JComboBox textSelect;
	//System buttons
	private JMenuBar mb;
	private JMenu 	file;
	private JMenu	network;
	private	JMenuItem	neww;
	private JMenuItem	save;
	private JMenuItem	open;
	private JMenuItem	savePNG;
	private	JMenuItem	startServer;
	private	JMenuItem	startClient;
	private	JMenuItem	exit;
	private static JFileChooser	fileChooser;
	private int returnVal;

	/**
	 * Color Chooser
	 */
	private JButton setColor;
	private JFrame	colorFrame;
	private JColorChooser	colorChooser;
	private JButton colorOk;
	//private Vector<Vector<Integer>> rowData = new Vector<Vector<Integer>>();
	//private Vector<String> colNames = new Vector<String>();

	private JTable coordTable;
	private Canvas theCanvas;
	
	
	//anonymous inner class to represent the table
	public class ShapeTableModel extends AbstractTableModel{
		
		public static final int X = 0;
		public static final int Y = 1;
		public static final int WIDTH = 2;
		public static final int HEIGHT = 3;
		
		public ShapeTableModel() {
			super();
			
			final ShapeTableModel sTmod = this;
			theCanvas.addShapeListListener(new Canvas.ShapeListListener(){
				DShapeModel.dsmListener listener;
				public void shapeAdded(DShape shape){
					fireTableChanged(new TableModelEvent(sTmod));
					shape.getShapeModel().addDsmListener(listener = 
							new DShapeModel.dsmListener() {
						public void dsmChanged(DShapeModel dsm){
							int num = theCanvas.getDsmIndexNum(dsm);
							fireTableRowsUpdated(num, num+1);
						}
					});
				}
				
				public void shapeRemoved(DShape shape) {
					shape.getShapeModel().removeDsmListener(listener);
					listener = null;
					fireTableChanged(new TableModelEvent(sTmod));
				}
			});
			
		}
		
		public int getColumnCount(){
			return HEIGHT + 1;
		}
		
		public int getRowCount(){
			return theCanvas.getShapesList().size();
		}
		
		public Object getValueAt(int row, int col){
			DShape shape = theCanvas.getShapesList().get(row);
			if(col == X) return shape.getShapeModel().getRect().x;
			else if(col == Y) return shape.getShapeModel().getRect().y;
			else if(col == WIDTH) return shape.getShapeModel().getRect().width;
			else if(col == HEIGHT) return shape.getShapeModel().getRect().height;
			
			return null;
		}

	}

	
	@SuppressWarnings("rawtypes")
	public WhiteBoard(){
		
		super("White Board");
		shapesBox = new Box(BoxLayout.X_AXIS);
		colorsBox = new Box(BoxLayout.X_AXIS);
		textBox = new Box(BoxLayout.X_AXIS);
		layersBox = new Box(BoxLayout.X_AXIS);
		theCanvas = new Canvas();
		shapesBox.setName("Add");
		shapesBox.add(rect = new JButton("Rect"));
		shapesBox.add(oval = new JButton("Oval"));
		shapesBox.add(line = new JButton("Line"));
		shapesBox.add(text = new JButton("Text"));
		
		/* File Menu*/
		mb = new JMenuBar();
		
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		network = new JMenu("Network");
		
		file.add(neww = new JMenuItem("New"));
		neww.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		file.addSeparator();
		file.add(save = new JMenuItem("Save"));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		file.add(open = new JMenuItem("Open"));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		file.add(savePNG = new JMenuItem("Save PNG"));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		file.addSeparator();
		file.add(exit = new JMenuItem("Exit"));
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);// TODO Auto-generated method stub
				
			}
		});
		
		network.add(startServer = new JMenuItem("Start Server"));
		network.add(startClient = new JMenuItem("Start Client"));
		
		mb.add(file);
		mb.add(network);
		setJMenuBar(mb);
		
		/* File Menu*/
		 
		
		/*Color Chooser*/
		colorsBox.add(setColor = new JButton("Set Color"));
		colorFrame = new JFrame("Choose Color");
		colorFrame.setLayout(new BorderLayout());
		colorFrame.add(colorChooser = new JColorChooser(), BorderLayout.WEST);
		colorFrame.add(colorOk = new JButton("OK"), BorderLayout.EAST);
		colorFrame.setVisible(false);
		colorFrame.pack();
		/*Color Chooser*/
		//colorsBox.setMinimumSize(new Dimension(400, 50));
		
		textBox.add(textField = new JTextField("White Board!"));
		textSelect = new JComboBox();
		String[] availFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for(String font : availFonts)
			textSelect.addItem(font);
		for(String fontName : FONT_NAMES)
			textSelect.setSelectedItem(fontName);
		textBox.add(textSelect);
		textField.setMaximumSize(new Dimension(255, 20));
		textSelect.setMaximumSize(new Dimension(255, 20));
		//textBox.setMinimumSize(new Dimension(400, 40));
		
		layersBox.add(moveToFront = new JButton("Move To Front"));
		layersBox.add(moveToBack = new JButton("Move To Back"));
		layersBox.add(removeShape = new JButton("Remove Shape"));
		//layersBox.setMinimumSize(new Dimension(400, 50));
		
		
		coordTable = new JTable(rowData, COL_NAMES);
		coordTable.setModel(new ShapeTableModel());
		JScrollPane coordsPane = new JScrollPane(coordTable);
		
		setBoxAlignment(shapesBox);
		setBoxAlignment(colorsBox);
		setBoxAlignment(textBox);
		setBoxAlignment(layersBox);
		
		Box leftVertical = new Box(BoxLayout.Y_AXIS);
		leftVertical.setMinimumSize(new Dimension(399, 400));
		leftVertical.add(shapesBox);
		leftVertical.add(colorsBox);
		leftVertical.add(textBox);
		leftVertical.add(layersBox);
		leftVertical.add(coordsPane);
		setBoxAlignment(leftVertical);
		
		this.setLayout(new BorderLayout());
		this.add(leftVertical, BorderLayout.WEST);
		this.add(theCanvas, BorderLayout.CENTER);
		this.setSize(800, 400);
		this.setVisible(true);
		startButtonListeners();
		
	}
	
	
	private void setBoxAlignment(Box bx){
		for(Component comp : bx.getComponents())
			((JComponent)comp).setAlignmentX(Box.LEFT_ALIGNMENT);
	}
	
	protected ShapeTableModel getShapeTableModel(){
		ShapeTableModel sTmod = new ShapeTableModel();
		return sTmod;
	}
	
	protected void startButtonListeners(){
		
		final WhiteBoard thisBoard = this;
		rect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addNewModel(new DRectModel());
			}
		});
		oval.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addNewModel(new DOvalModel());
			}
		});
		line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DLineModel newModel = new DLineModel();
				newModel.setStartPt(new Point(150, 150));
				newModel.setEndPt(new Point(200, 200));
				theCanvas.addShape(newModel);
				theCanvas.sendToAllRemotes(1, newModel);
			}
		});
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final DTextModel newModel = new DTextModel();
				newModel.setText("White Board!");
				newModel.setFont(FONT_NAMES[0]);
				addNewModel(newModel);
				newModel.addDsmListener(new DShapeModel.dsmListener(){
					public void dsmChanged(DShapeModel dsm){
						updateTextInspector();
						theCanvas.sendToAllRemotes(5, theCanvas.selectedShape.getShapeModel());
					}
				});
			}
		});
		textField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DShape shape = theCanvas.selectedShape;
				if(shape != null){
					if(shape.getShapeModel() instanceof DTextModel)
						((DTextModel)shape.getShapeModel()).setText(textField.getText());
					theCanvas.sendToAllRemotes(5, shape.getShapeModel());
				}
			}
		});
		textSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DShape shape = theCanvas.selectedShape;
				if(shape != null){
					if(shape.getShapeModel() instanceof DTextModel)
						((DTextModel)shape.getShapeModel()).setFont(textSelect.getSelectedItem().toString());
					theCanvas.sendToAllRemotes(5, shape.getShapeModel());
				}
			}
		});
		
		/*Color Chooser*/
		setColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(theCanvas.selectedShape != null)
				{
					colorFrame.setLocationRelativeTo(theCanvas);
					colorFrame.setVisible(true);
					
					colorOk.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							DShape shape = theCanvas.selectedShape;
							shape.getShapeModel().setColor(colorChooser.getColor());
							theCanvas.sendToAllRemotes(5, shape.getShapeModel());
							colorFrame.setVisible(false);
							
						}
					});
					
				}
			}
		});
		/*Color Chooser*/
		
		removeShape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(theCanvas.selectedShape != null)
			{
				DShape shape = theCanvas.selectedShape;
				theCanvas.removeShape(shape);
				theCanvas.sendToAllRemotes(2, shape.getShapeModel());
			}
			}
		});
		
		moveToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(theCanvas.selectedShape != null)
				{
					DShape shape = theCanvas.selectedShape;
					theCanvas.shapeToFront(shape);
					theCanvas.sendToAllRemotes(3, shape.getShapeModel());
				}
			}
		});
		
		moveToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(theCanvas.selectedShape != null)
				{
					DShape shape = theCanvas.selectedShape;
					theCanvas.shapeToBack(shape);
					theCanvas.sendToAllRemotes(4, shape.getShapeModel());
				}
			}
		});
		
		neww.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(!theCanvas.shapeList.isEmpty())
			{
				final JFrame caution = new JFrame("Caution");
				Box y = new Box(BoxLayout.Y_AXIS);
				Box x = new Box(BoxLayout.X_AXIS);
				JLabel warning = new JLabel("Do you want to clean the whiteboard? Everything will be lost.");
				JButton yes = new JButton("Yes");
				JButton no = new JButton("No");
				x.add(yes); x.add(no);
				y.add(warning);
				y.add(x);
				for(Component comp : y.getComponents())
				{
					((JComponent)comp).setAlignmentX(Box.CENTER_ALIGNMENT);
				}
				caution.add(y);
				caution.pack();
				caution.setLocationByPlatform(true);
				caution.setVisible(true);
				no.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						caution.setVisible(false);
						caution.dispose();
					}
				});
				
				yes.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						theCanvas.shapeList.clear();
						theCanvas.repaint();
						caution.dispose();
					}
				});
				
				
			}
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save Title");
				returnVal = fileChooser.showSaveDialog(WhiteBoard.this);
				
				if(returnVal == fileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					theCanvas.save(file);
					
					String snapshotLocation = file.getAbsolutePath();
					JPanel panel = theCanvas;
					 BufferedImage bufImage = new BufferedImage(panel.getSize().width, panel.getSize().height,BufferedImage.TYPE_INT_RGB);
				       panel.paint(bufImage.createGraphics());
				       File imageFile = new File(file.getAbsolutePath()+".png");
				    try{
				        imageFile.createNewFile();
				        ImageIO.write(bufImage, "png", imageFile);
				    }catch(Exception ex){
				    }
				    
					fileChooser.disable();
				}
			}
		});
		
		savePNG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save Title");
				returnVal = fileChooser.showSaveDialog(WhiteBoard.this);
				
				if(returnVal == fileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					theCanvas.saveScreenshot(file);
					fileChooser.disable();
				}
			}
		});
		
		open.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				/*String result = JOptionPane.showInputDialog("File Name", null);
                if (result != null) {
                    File f = new File(result);
                    theCanvas.open(f);
                }*/
				fileChooser = new JFileChooser();
				returnVal = fileChooser.showOpenDialog(WhiteBoard.this);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					file = file.getAbsoluteFile();
					theCanvas.open(file);
					fileChooser.disable();
				}
				
				
			}
		});
		
		startServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
               theCanvas.doServer();
			}
		});
		
		startClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	               theCanvas.doClient();
			}
		});
	}
	
	protected void addNewModel(DShapeModel dsm){
		dsm.setRect(theCanvas.randomBoundsGenerator());
		theCanvas.addShape(dsm);
	}
	
	protected void updateTextInspector(){
		DShape shape = theCanvas.selectedShape;
		if(shape != null && shape instanceof DText){
			DTextModel dsm = ((DTextModel)shape.getShapeModel());
			textField.setText(dsm.getText());
			textSelect.setSelectedItem(dsm.getFont());
			
			textField.setEnabled(true);
			textSelect.setEnabled(true);
		}
		else{
			textField.setEnabled(false);
			textSelect.setEnabled(false);
		}
	}
	
	
	public static void main(String[] args){
		WhiteBoard[] thisBoard = new WhiteBoard[3];
		for (int i = 0; i < 3; i++){
			thisBoard[i] = new WhiteBoard();
			thisBoard[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}	
	}

}
