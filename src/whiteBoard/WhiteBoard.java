package whiteBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.*;

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
	private JButton setColor;
	private JButton moveToFront;
	private JButton moveToBack;
	private JButton removeShape;
	private JTextField textField;
	private JComboBox textSelect;
	//private Vector<Vector<Integer>> rowData = new Vector<Vector<Integer>>();
	//private Vector<String> colNames = new Vector<String>();

	private JTable coordTable;
	private Canvas theCanvas;
	
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
		//shapesBox.setMinimumSize(new Dimension(400, 50));
		
		colorsBox.add(setColor = new JButton("Set Color"));
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
		coordTable.setModel(getShapeTableModel());
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
		//setResizable(false);
		
		this.setLayout(new BorderLayout());
		this.add(leftVertical, BorderLayout.WEST);
		this.add(theCanvas, BorderLayout.CENTER);
		//theCanvas.setVisible(true);
		this.setSize(800, 400);
		theCanvas.setVisible(true);
		coordsPane.setVisible(true);
		this.setVisible(true);
		
	}
	
	
	private void setBoxAlignment(Box bx){
		for(Component comp : bx.getComponents())
			((JComponent)comp).setAlignmentX(Box.LEFT_ALIGNMENT);
	}
	
	protected ShapeTableModel getShapeTableModel(){
		ShapeTableModel sTmod = new ShapeTableModel();
		return sTmod;
	}
	
	public static void main(String[] args){
		WhiteBoard thisBoard = new WhiteBoard();
	}

}
