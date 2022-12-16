package utils;

import javafx.scene.control.Tooltip;
// Creates a new tooltip window
public class TooltipSetter {
	private String text;
	private Tooltip tooltip;
	public TooltipSetter(String text) {
		this.text = text;
		tooltip = new Tooltip();
		tooltip.setText(text);
		tooltip.setStyle(
		    "-fx-background-color: #ffffffd0;"
			+ "-fx-font-family: \"Calibri\";"
		    + "-fx-font-size: 10pt;"
		    + "-fx-text-fill: #1E3D58;"
		);
	}
	
	public Tooltip getTooltip() {
		return tooltip;
	}
	
	public String getTooltipText() {
		return text;
	}
		
	
}
