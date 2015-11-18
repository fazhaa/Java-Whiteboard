package whiteBoard;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.*;

public class WhiteBoard extends JFrame{
	
	/**
	 * Serial Version UID is required, used default
	 */
	private static final long serialVersionUID = 1L;
	private Box shapesBox = new Box(BoxLayout.X_AXIS);
	private Box colorsBox = new Box(BoxLayout.X_AXIS);
	private Box textBox = new Box(BoxLayout.X_AXIS);
	private Box layersBox = new Box(BoxLayout.X_AXIS);
	private Box coordsBox = new Box(BoxLayout.X_AXIS);
	private JButton rect = new JButton("Rect");
	private JButton oval = new JButton("Oval");
	private JButton line = new JButton("Line");
	private JButton text = new JButton("Text");
	private JButton setColor = new JButton("Set Color");
	private JButton moveToFront = new JButton("Move To Front");
	private JButton moveToBack = new JButton("Move To Back");
	private JButton removeShape = new JButton("Remove Shape");
	private JTextField textField = new JTextField();
	private JTextArea textArea = new JTextArea("Font", 1, 1);
	private Vector<Vector<Integer>> rowData = new Vector<Vector<Integer>>();
	private Vector<String> colNames = new Vector<String>();

	private JTable coordTable;
	private Canvas theCanvas = new Canvas();
	
	public WhiteBoard(){
		
		shapesBox.setName("Add");
		shapesBox.add(rect);
		shapesBox.add(oval);
		shapesBox.add(line);
		shapesBox.add(text);
		shapesBox.setMaximumSize(new Dimension(400, 30));
		colorsBox.add(setColor);
		colorsBox.setMaximumSize(new Dimension(400, 30));
		textBox.add(textField);
		textBox.add(textArea);
		textBox.setMaximumSize(new Dimension(400, 20));
		layersBox.add(moveToFront);
		layersBox.add(moveToBack);
		layersBox.add(removeShape);
		layersBox.setMaximumSize(new Dimension(400, 30));
		
		colNames.add("X");
		colNames.add("Y");
		colNames.add("Width");
		colNames.add("Height");
		
		coordTable = new JTable(rowData, colNames);
		coordsBox.add(coordTable);
		
		setBoxAlignment(shapesBox);
		setBoxAlignment(colorsBox);
		setBoxAlignment(textBox);
		setBoxAlignment(layersBox);
		
		Box leftVertical = new Box(BoxLayout.Y_AXIS);
		leftVertical.add(shapesBox);
		leftVertical.add(colorsBox);
		leftVertical.add(textBox);
		leftVertical.add(layersBox);
		leftVertical.add(coordsBox);
		setBoxAlignment(leftVertical);
		
		theCanvas.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.add(leftVertical, BorderLayout.WEST);
		this.add(theCanvas, BorderLayout.CENTER);
		this.setSize(800, 400);
		coordTable.setVisible(true);
		this.setVisible(true);
		
	}
	
	
	private void setBoxAlignment(Box bx){
		for(Component comp : bx.getComponents())
			((JComponent)comp).setAlignmentX(Box.LEFT_ALIGNMENT);
	}
	
	public static void main(String[] args){
		WhiteBoard thisBoard = new WhiteBoard();
	}

}
