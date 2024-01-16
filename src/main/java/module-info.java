/**
 * Project and Training 2: Carbon - Computer Science, Berner Fachhochschule
 *
 * Specification of required Java modules. Add further entries if necessary.
 */
module carbonfx {
	exports ch.bfh.carbon;

	// JavaFX
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;

	// FasterXML JSON library
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires org.json;
	requires java.sql;
}
