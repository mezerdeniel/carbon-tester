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
    public MenuItem menuItemSave;
    @FXML
    public MenuItem menuItemExit;
    @FXML
    public MenuItem menuItemAbout;
    @FXML
    public MenuItem menuItemFR;
    @FXML
    public MenuItem menuItemDE;
    @FXML
    public MenuItem menuItemEN;
    @FXML
    public TableView<CarbonData> tableView;
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
    public Button deleteARowButton;
    @FXML
    public Button deleteAllButton;
    private final DataModel model = new DataModel(); // The model of the application
    private static final int COL_WIDTH = 5; // The width of the columns in the table

    private static final int DIALOG_WIDTH = 640; // The width of the dialog
    private static final int DIALOG_HEIGHT = 210; // The height of the dialog
    @FXML
    public Menu file_button;
    @FXML
    public Menu help_button;
    @FXML
    public Menu language_button;

    @FXML
    public Label url;

    /**
     *
     *Creates the whole Window, with all its table columns, the stored data, menu items, colum names and buttons.
     *Furthermore, sets the start language to english.
     */
    @FXML
    public void initialize() {
        urlCol.prefWidthProperty().bind(tableView.widthProperty().divide(COL_WIDTH));
        greenCol.prefWidthProperty().bind(tableView.widthProperty().divide(COL_WIDTH));
        bytesCol.prefWidthProperty().bind(tableView.widthProperty().divide(COL_WIDTH));
        cleanerThanCol.prefWidthProperty().bind(tableView.widthProperty().divide(COL_WIDTH));
        timestampCol.prefWidthProperty().bind(tableView.widthProperty().divide(COL_WIDTH));

        // Initialize and load data from a file
        loadData();

        menuItemFR.setOnAction(event -> changeLanguage("fr"));
        menuItemDE.setOnAction(event -> changeLanguage("de"));
        menuItemEN.setOnAction(event -> changeLanguage("en"));
        menuItemSave.setOnAction(event -> saveData());
        menuItemExit.setOnAction(event -> System.exit(0));
        menuItemAbout.setOnAction(event -> showAboutDialog());
        deleteAllButton.setOnAction(event -> deleteAllRows());
        deleteARowButton.setOnAction(event -> deleteSelectedRow());

        // Set up table columns and data
        tableView.getColumns().add(urlCol);
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        tableView.getColumns().add(greenCol);
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

        tableView.getColumns().add(bytesCol);
        bytesCol.setCellValueFactory(new PropertyValueFactory<>("bytes"));
        tableView.getColumns().add(cleanerThanCol);
        cleanerThanCol.setCellValueFactory(new PropertyValueFactory<>("cleanerThan"));
        tableView.getColumns().add(timestampCol);
        // Set the cell factory for the timestamp column to display the timestamp for the different languages
        timestampCol.setCellValueFactory(cellData -> {
            Long timestamp = cellData.getValue().getTimestamp();
            String formattedTimestamp = formatTimestamp(timestamp);
            return new SimpleStringProperty(formattedTimestamp);
        });

        tableView.setItems(model.getData());

        // Set up the input field for the URL
        inputUrl.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addUrl();
            }
        });
        changeLanguage("en");

    }

    /**
     * Changes the language of the application. When the User clicks on ether of the buttons (en, de, fr).
     *
     * @param language The language code (e.g., "en", "de", "fr")
     */
    private void changeLanguage(String language) {
        // Set the new language and load the corresponding resources
        locale = new Locale(language);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        CarbonData.setLocale();

        // Update the text for the languages selected
        language_button.setText(resourceBundle.getString("menu.language.text"));
        menuItemSave.setText(resourceBundle.getString("menu.file.save.text"));
        menuItemExit.setText(resourceBundle.getString("menu.file.exit.text"));
        menuItemAbout.setText(resourceBundle.getString("menu.help.about.text"));
        deleteARowButton.setText(resourceBundle.getString("button.deleteRow.text"));
        deleteAllButton.setText(resourceBundle.getString("button.deleteAll.text"));
        file_button.setText(resourceBundle.getString("menu.file.text"));
        help_button.setText(resourceBundle.getString("menu.help.text"));
        url.setText(resourceBundle.getString("label.enterUrl.text"));
        timestampCol.setText(resourceBundle.getString("table.timestamp.text"));
        greenCol.setText(resourceBundle.getString("table.greenColumn.text"));
        cleanerThanCol.setText(resourceBundle.getString("table.cleanerThan.text"));
        tableView.setPlaceholder(new Label(resourceBundle.getString("table.placeholder.text")));
        //Update the values in the table
        for (CarbonData data : model.getData()) {
            tableView.refresh();
        }
    }

    /**
     * Displays the About dialog. When the User clicks on the help button. A window is being created, the text for the
     * title and inside the window is being added, the max width and height is set and then finally displayed.
     */
    private void showAboutDialog() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        String title = resourceBundle.getString("aboutDialog.title");

        TextArea contentTextArea = new TextArea(resourceBundle.getString("aboutDialog.content"));
        contentTextArea.setEditable(false);
        contentTextArea.setWrapText(true);
        contentTextArea.setMaxWidth(Double.MAX_VALUE);
        contentTextArea.setMaxHeight(Double.MAX_VALUE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(contentTextArea);

        alert.getDialogPane().setPrefWidth(DIALOG_WIDTH); // Width of the window
        alert.getDialogPane().setPrefHeight(DIALOG_HEIGHT); // High of the window
        alert.showAndWait();
    }

    /**
     * Displays an error message. By creating a window with no title and with the according errorMassage given when an
     * exception is thrown. Then displays the window to the user.
     *
     * @param errorMessage The error message to be displayed.
     */
    private void showError(String errorMessage) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(resourceBundle.getString("error.title"));
        //Translated error message
        alert.setContentText(resourceBundle.getString("error." + errorMessage));
        alert.showAndWait();
    }

    /**
     * Sets the timestamp for the added or loaded URLs. The format is changed automatically according to the language
     * selected by the User.
     *
     * @param timestamp the time the URL has been added to the list.
     * @return the time in the corresponding DateTimePattern.
     */
    private String formatTimestamp(Long timestamp) {
        String language = locale.getLanguage();
        ZoneId zoneId;
        DateTimeFormatter formatter;
        switch (language) {
            case "en" -> {
                zoneId = ZoneId.of("Europe/London");
                formatter = DateTimeFormatter.ofPattern("M/d/yy, h:mm a", locale);
            }
            case "de" -> {
                zoneId = ZoneId.of("Europe/Berlin");
                formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm", locale);
            }
            case "fr" -> {
                zoneId = ZoneId.of("Europe/Paris");
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm", locale);
            }
            default -> {
                // Default to GMT if language is not supported
                zoneId = ZoneId.of("GMT");
                formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm a", locale);
            }
        }

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        return dateTime.format(formatter);
    }

    /**
     * Sets the String if a website is green. By checking if the given String is "true", "false" or something else.
     * And setting the column accordingly.
     *
     * @param greenValue a String which shows if the URL is green.
     * @return the according String in the Column.
     */
    private String localizeGreenValue(String greenValue) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);
        return switch (greenValue.toLowerCase()) {
            case "true" -> resourceBundle.getString("table.greenColumn.true");
            case "false" -> resourceBundle.getString("table.greenColumn.false");
            default -> resourceBundle.getString("table.greenColumn.unknown");
        };
    }

    /**
     * Loads data from a file and populates the table.
     *
     * @throws JSONException if no JSON object is found.
     * IOException   if the file cannot be read.
     *
     */
    private void loadData() {
        try {
            model.getData().clear();
            List<CarbonData> dataList = CarbonData.loadFromJSONFile(DATA_FILE_PATH + DATA_FILE_NAME);
            model.getData().addAll(dataList);
        } catch (IOException e) {
            // Handling IOException
            e.printStackTrace();
            showError("IOException");
        } catch (JSONException e) {
            // Handling JSONException
            e.printStackTrace();
            showError("JSONException");
        }
    }

    /**
     * Saves the data to a file.
     * @throws JSONException if no JSON object is found.
     * IOException if the file does not allow to be accessed.
     */
    private void saveData() {
        try {
            CarbonData.saveToJSONFile(model.getData(), DATA_FILE_PATH + DATA_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            showError("IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            showError("JSONException");
        }
    }

    /**
     * Reads all characters from a reader and returns them as a string.
     *
     * @param rd The reader to read from
     * @return The string containing all the characters read from the reader
     * @throws IOException If an I/O error occurs while reading from the reader
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cValue;
        while ((cValue = rd.read()) != -1) {
            sb.append((char) cValue);
        }
        return sb.toString();
    }

    /**
     * Reads JSON data from a URL and returns it as a JSONObject.
     *
     * @param url The URL to read the JSON data from
     * @return The JSONObject containing the JSON data
     * @throws IOException   If the BufferReader can not access the URL
     * @throws JSONException If the JSON data is invalid or cannot be parsed
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        //Reads the JSON data from the URL
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    /**
     * Adds a URL to the table. First the URL is added to the Link of the website carbon link. The resulting JSON object
     * is being analysed, the bytes and the cleanerThan rating is received from the object. Then the
     * currentTime is taken, and then it is checked if the website is green.
     * Lastly the information is added to the table.
     *
     * @throws JSONException if the data is invalid or cannot be parsed.
     */
    private void addUrl() {
        try {
            String urlEntry = "https://api.websitecarbon.com/site?url=" + inputUrl.getText();
            JSONObject jsObj = readJsonFromUrl(urlEntry);

            // Get values from JSON
            String urlValue = jsObj.getString("url");
            long bytes = jsObj.optLong("bytes", 0);
            double cleanerThan = jsObj.optDouble("cleanerThan", 0.0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String green;

            // Checking the type of "green"
            Object greenObject = jsObj.get("green");
            if (greenObject instanceof String) {
                green = (String) greenObject;
            } else if (greenObject instanceof Boolean) {
                green = String.valueOf(greenObject);
            } else {
                String errorMessage = "'green' is neither a String nor a Boolean";
                throw new JSONException(errorMessage);
            }

            // Add new data
            CarbonData newObj = new CarbonData(urlValue, green, bytes, cleanerThan, timestamp.getTime());
            model.addNewWebsiteData(newObj);
            System.out.println("Added new data without exception.");
        } catch (JSONException e) {
            e.printStackTrace();
            showError("jsonException");
        } catch (IOException e) {
            e.printStackTrace();
            showError("IOException");
        }
    }

    /**
     * Deletes all rows from the table.
     */
    private void deleteAllRows() {
        model.getData().clear();
    }

    /**
     * Deletes the selected row from the table.
     */
    private void deleteSelectedRow() {
        CarbonData selectedRow = tableView.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            model.getData().remove(selectedRow);
        }
    }
}
