// Project and Training 2: Space News Controller - Computer Science, Berner Fachhochschule

package ch.bfh.carbon;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import java.io.Reader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for the Carbon Footprint application
 */
public class Controller {

	// The URL of the API
	private static final String DATA_FILE_PATH = "src/main/java/ch/bfh/carbon/";

	// File where the data is stored
	private static final String DATA_FILE_NAME = "carbon_data.json";

	private static Locale locale;

	// MenuItems, Buttons, Label and Columns defined in the FXML file
	@FXML
	public MenuItem saveItem;
	@FXML
	public MenuItem exitItem;
	@FXML
	public MenuItem aboutItem;

	@FXML
	public MenuItem deItem;
	@FXML
	public MenuItem enItem;
	@FXML
	public TableView<CarbonData> view;
	@FXML
	public TableColumn<CarbonData, String> urlCol;
	@FXML
	public TableColumn<CarbonData, String> greenCol;
	@FXML
	public TableColumn<CarbonData, Long> bytesCol;
	@FXML
	public TableColumn<CarbonData, Double> cleanerThanCol;
	@FXML
	public TableColumn<CarbonData, String> timestampCol;
	@FXML
	public TextField inputUrl;
	@FXML
	public Button deleteRowButton;
	@FXML
	public Button deleteAllButton;
	private final DataModel dataModel = new DataModel(); // The model of the application
	private static final int COL_WIDTH = 5; // The width of the columns in the table

	private static final int DIALOG_WIDTH = 640; // The width of the dialog
	private static final int DIALOG_HEIGHT = 210; // The height of the dialog
	@FXML
	public Menu fileButton;
	@FXML
	public Menu helpButton;
	@FXML
	public Menu languageButton;

	@FXML
	public Label url;

	/**
	 * Creates the whole Window, with all its table columns, the stored data, menu items, colum names and buttons.
	 * Furthermore, sets the start language to english.
	 */
	@FXML
	public void initialize() {
		urlCol.prefWidthProperty().bind(view.widthProperty().divide(COL_WIDTH));
		greenCol.prefWidthProperty().bind(view.widthProperty().divide(COL_WIDTH));
		bytesCol.prefWidthProperty().bind(view.widthProperty().divide(COL_WIDTH));
		cleanerThanCol.prefWidthProperty().bind(view.widthProperty().divide(COL_WIDTH));
		timestampCol.prefWidthProperty().bind(view.widthProperty().divide(COL_WIDTH));

		// Initialize and load data from a file
		loadData();


		deItem.setOnAction(event -> changeLanguage("de"));
		enItem.setOnAction(event -> changeLanguage("en"));
		saveItem.setOnAction(event -> saveData());
		exitItem.setOnAction(event -> System.exit(0));
		aboutItem.setOnAction(event -> showAboutDialog());
		deleteAllButton.setOnAction(event -> deleteAllRow());
		deleteRowButton.setOnAction(event -> deleteSelectedRow());

		// Set up table columns and data
		view.getColumns().add(urlCol);
		urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
		view.getColumns().add(greenCol);
		greenCol.setCellValueFactory(cellData -> {
			String greenValue = cellData.getValue().getGreen();
			return new SimpleStringProperty(greenValue);
		});

		// Set the cell factory for the green column to display the green value in different languages
		greenCol.setCellFactory(column -> new TableCell<>() {
			@Override
			protected void updateItem(String greenValue, boolean empty) {
				super.updateItem(greenValue, empty);
				if (empty || greenValue == null) {
					setText(null);
				} else {
					setText(localizeGreenValue(greenValue));
				}
			}
		});

		view.getColumns().add(bytesCol);
		bytesCol.setCellValueFactory(new PropertyValueFactory<>("bytes"));
		view.getColumns().add(cleanerThanCol);
		cleanerThanCol.setCellValueFactory(new PropertyValueFactory<>("cleanerThan"));
		view.getColumns().add(timestampCol);
		// Set the cell factory for the timestamp column to display the timestamp for the different languages
		timestampCol.setCellValueFactory(cellData -> {
			Long timestamp = cellData.getValue().getTimestamp();
			String formattedTimestamp = formatTimestamp(timestamp);
			return new SimpleStringProperty(formattedTimestamp);
		});

		view.setItems(dataModel.getData());

		// Set up the input field for the URL
		inputUrl.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				addUrl();
			}
		});
		changeLanguage("en");

	}
}
	/**
	 * Changes the language of the application. When the User clicks on ether of the buttons (en, de, fr).
	 *
	 * @param language The language code (e.g., "en", "de", "fr")
	 */
