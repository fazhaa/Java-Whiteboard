package whiteBoard;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Canvas extends JPanel{
	
	private ArrayList<DShape> shapeList;
	
	Canvas(){
		setSize(400, 400); //set size to 400x400
		setLayout(new BorderLayout());
		setOpaque(true);
		setBackground(new Color(255, 255, 255)); //set background to white
		//repaint(new Rectangle(new Dimension(400, 400)));
		setVisible(true);
	}
	
	protected void paintComponent(){
		
		for(DShape shape : shapeList){
			//shape.getShape().)
		}
	}

}
