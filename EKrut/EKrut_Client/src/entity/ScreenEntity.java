package entity;

import java.util.Objects;

import common.CommonFunctions;
import common.ScreensDescription;
import common.ScreensNamesEnum;
import javafx.scene.Scene;
import javafx.scene.control.Label;
public class ScreenEntity {

	private ScreensNamesEnum sc;
	private Scene scene;
	private Label headline, path;
	
	public ScreenEntity(ScreensNamesEnum sc, Scene scene) {
		super();
		this.sc = sc;
		this.scene = scene;

	}
	
	@Override
	public String toString() {
		return CommonFunctions.splitByUpperCase(sc.toString());
	}
	
	public ScreensNamesEnum getSc() {
		return sc;
	}
	public void setSc(ScreensNamesEnum sc) {
		this.sc = sc;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public Label getHeadline() {
		return headline;
	}
	public void setHeadline(Label headline) {
		this.headline = headline;
	}
	public Label getPath() {
		return path;
	}
	public void setPath(Label path) {
		this.path = path;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(sc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScreenEntity other = (ScreenEntity) obj;
		return sc == other.sc;
	}
	
}
