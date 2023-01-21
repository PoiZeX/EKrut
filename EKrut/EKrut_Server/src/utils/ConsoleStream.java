package utils;

import java.io.IOException;
import java.io.OutputStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * The Class ConsoleStream.
 */

/**
 * Pipes the stream for default console to GUI Console
 *
 */
public class ConsoleStream extends OutputStream {
	private TextArea textArea;
	
	/**
	 * Instantiates a new console stream.
	 *
	 * @param textArea the text area
	 */
	public ConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * Append text.
	 *
	 * @param paramString the param string
	 */
	public void appendText(String paramString) {
		Platform.runLater(() -> this.textArea.appendText(paramString));
	}
	
	/**
	 * Write.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void write(int b) throws IOException {
		appendText(String.valueOf((char) b));
	}
}