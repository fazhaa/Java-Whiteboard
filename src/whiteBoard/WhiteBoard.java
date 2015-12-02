package whiteBoard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
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
	private JButton moveToFront;
	private JButton moveToBack;
	private JButton removeShape;
	private JTextField textField;
	private JComboBox textSelect;
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
				}
			}
		});
		textSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DShape shape = theCanvas.selectedShape;
				if(shape != null){
					if(shape.getShapeModel() instanceof DTextModel)
						((DTextModel)shape.getShapeModel()).setFont(textSelect.getSelectedItem().toString());
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
						
						@Override
						public void actionPerformed(ActionEvent e) {
							theCanvas.selectedShape.getShapeModel().setColor(colorChooser.getColor());
							colorFrame.setVisible(false);
							
						}
					});
					
				}
			}
		});
		/*Color Chooser*/
		
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
		WhiteBoard thisBoard = new WhiteBoard();
		thisBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
