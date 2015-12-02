package whiteBoard;

public class DTextModel extends DShapeModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -642446392100997510L;
	
	String text;
	String font;
	
	protected String getText() { return text; }
	
	protected String getFont() { return font; }
	
	protected void setText(String newText){
		if(newText.equals(text) == false){
			text = newText;
			notifyListeners();
		}
	}
	
	protected void setFont(String newFont){
		if(newFont.equals(font) == false){
			font = newFont;
			notifyListeners();
		}
	}
	
	protected void setModel(DShapeModel newDsm){
		super.setModel(newDsm);
		text = ((DTextModel)newDsm).text;
		font = ((DTextModel)newDsm).text;
	}

}
