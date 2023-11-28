/*
 * Project and Training 2: Space News Controller - Computer Science, Berner Fachhochschule
 */

package ch.bfh.carbon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
	@FXML
	public Label version;

	@FXML
	public void initialize() {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		version.setText("Hello, JavaFX " + javafxVersion	+ ", running on Java " + javaVersion + ".");
	}
}
