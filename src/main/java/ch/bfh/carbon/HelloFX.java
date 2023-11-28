/*
	* Project and Training 2: Carbon - Computer Science, Berner Fachhochschule
	*/
package ch.bfh.carbon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Dummy application class demonstrating a JavaFX application.
 */
public class HelloFX extends Application {

	/**
	 * Start method called by the JavaFX framework upon calling launch().
	 *
	 * @param stage a (default) stage provided by the framework
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		stage.setTitle("Website Carbon Tester");
		stage.setScene(new Scene(root, 640, 480));
		stage.show();
	}

	/**
	 * Main entry point of the application.
	 *
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch();
	}

}
