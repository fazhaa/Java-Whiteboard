package whiteBoard;

import javax.swing.table.AbstractTableModel;

public class ShapeTableModel extends AbstractTableModel{
	
	/**
	 * Auto generated serial version UID
	 */
	private static final long serialVersionUID = -819517879997985110L;
	public static final int X = 0;
	public static final int Y = 1;
	public static final int WIDTH = 2;
	public static final int HEIGHT = 3;
	
	public ShapeTableModel() {
		super();
		//finish later
		
		
	}
	
	public int getColumnCount(){
		return HEIGHT + 1;
	}
	
	public int getRowCount(){
		return 1; //fix later
		//return 
	}
	
	public Object getValueAt(int row, int col){
		return new Object(); //fix later
	}

}
