package entity;

import java.util.Objects;

import common.CommonFunctions;
import common.ScreensDescription;
import enums.ScreensNamesEnum;
import javafx.scene.Scene;
import javafx.scene.control.Label;
public class ScreenEntity {

	private ScreensNamesEnum sc;
	private Scene scene;
	private Label headline, path;
	
	/**
	 * Instantiates a new screen entity.
	 *
	 * @param sc the sc
	 * @param scene the scene
	 */
	public ScreenEntity(ScreensNamesEnum sc, Scene scene) {
		super();
		this.sc = sc;
		this.scene = scene;

	}
	
	@Override
	public String toString() {
		return CommonFunctions.splitByUpperCase(sc.toString());
	}
	
	/**
	 * Gets the sc.
	 *
	 * @return the sc
	 */
	public ScreensNamesEnum getSc() {
		return sc;
	}
	
	/**
	 * Sets the sc.
	 *
	 * @param sc the new sc
	 */
	public void setSc(ScreensNamesEnum sc) {
		this.sc = sc;
	}
	
	/**
	 * Gets the scene.
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Sets the scene.
	 *
	 * @param scene the new scene
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	/**
	 * Gets the headline.
	 *
	 * @return the headline
	 */
	public Label getHeadline() {
		return headline;
	}
	
	/**
	 * Sets the headline.
	 *
	 * @param headline the new headline
	 */
	public void setHeadline(Label headline) {
		this.headline = headline;
	}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public Label getPath() {
		return path;
	}
	
	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
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
