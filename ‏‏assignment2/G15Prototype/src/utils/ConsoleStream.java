package utils;

import java.io.IOException;
import java.io.OutputStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ConsoleStream extends OutputStream {
	private TextArea textArea;

	public ConsoleStream(TextArea textArea) {
		this.textArea = textArea;
	}

	public void appendText(String paramString) {
		Platform.runLater(() -> this.textArea.appendText(paramString));
	}

	public void write(int b) throws IOException {
		appendText(String.valueOf((char) b));
	}
}