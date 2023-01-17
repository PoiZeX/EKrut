package utils;

import java.io.IOException;
import java.io.OutputStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
/***
 * The {@code ConsoleStream} class captures the output from standard output and error streams,
 * and redirect it to a {@code TextArea} in a JavaFX application.
 * @author author
 * @version 1.0
 * @since YYYY-MM-DD
*/

/**
 * Pipes the stream for default console to GUI Console
 *
 */
public class ConsoleStream extends OutputStream {
	private TextArea textArea;
	/**
	 * Constructs a new {@code ConsoleStream} object, with a given {@code TextArea} where the captured output will be redirected.
	 * @param textArea A {@code TextArea} where the captured output will be displayed.
	 */
	public ConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}
	
	/**
	 * Append a given text to the {@code TextArea}.
	 * @param paramString A {@code String} that represents the text to be appended to the {@code TextArea}.
	 */
	public void appendText(String paramString) {
		Platform.runLater(() -> this.textArea.appendText(paramString));
	}
	/**
	 * Write a given int to the {@code TextArea} in form of character.
	 * @param b An {@code int} that will be converted to character and written in the {@code TextArea}.
	 * @throws IOException if an I/O error occurs.
	 */
	public void write(int b) throws IOException {
		appendText(String.valueOf((char) b));
	}
}