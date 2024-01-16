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
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	/**
	 * Start method called by the JavaFX framework upon calling launch().
	 *
	 * @param stage a (default) stage provided by the framework
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = new FXMLLoader(getClass().getResource("sample.fxml")).load();
		stage.setTitle("Website Carbon Tester");
		stage.setScene(new Scene(root, WIDTH, HEIGHT));
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
