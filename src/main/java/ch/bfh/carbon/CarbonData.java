package ch.bfh.carbon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents website carbon data in the application.
 */
public class CarbonData {
    private String url;
    private String green;
    private final long bytes;
    private double cleanerThan;
    private long timestamp;

    /**
     * Constructs a new CarbonData object with the specified parameters.
     *
     * @param url         The URL of the website
     * @param green       The green status of the website
     * @param bytes       The size of the website in bytes
     * @param cleanerThan The cleanliness rating of the website
     * @param timestamp   The timestamp of the data
     */
    public CarbonData(String url, String green, long bytes, double cleanerThan, long timestamp) {
        this.url = url;
        this.green = green;
        this.bytes = bytes;
        this.cleanerThan = cleanerThan;
        this.timestamp = timestamp;
    }

    /**
     * Returns the URL of the website.
     *
     * @return The URL of the website
     */
    public String getUrl() {
        return url;
    }


    /**
     * Sets the URL of the website.
     *
     * @param url The URL of the website
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the size of the website in bytes.
     *
     * @return The size of the website in bytes
     */
    public long getBytes() {
        return bytes; //necessary for returning bytes
    }

    /**
     * Returns the green status of the website.
     *
     * @return The green status of the website
     */
    public String getGreen() {
        return green;
    }

    /**
     * Sets the green status of the website.
     *
     * @param green The green status of the website
     */
    public void setGreen(String green) {
        this.green = green;
    }

    /**
     * Sets the locale of the website.
     *
     */
    public static void setLocale() {
    }

    /**
     * Returns the cleanliness rating of the website.
     *
     * @return The cleanliness rating of the website
     */
    public double getCleanerThan() {
        return cleanerThan;
    }

    /**
     * Sets the cleanliness rating of the website.
     *
     * @param cleanerThan The cleanliness rating.
     */
    public void setCleanerThan(double cleanerThan) {
        this.cleanerThan = cleanerThan;
    }

    /**
     * Returns the formatted timestamp.
     *
     * @return The formatted timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp of the data.
     *
     * @param timestamp The timestamp of the data
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Converts the CarbonData object to a JSON object.
     *
     * @return The JSON representation of the object
     * @throws JSONException if there is an error during JSON conversion
     */
    public JSONObject toJSON() throws JSONException {
        //Converts the CarbonData object to a JSON object
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", url);
            jsonObject.put("green", green);
            jsonObject.put("bytes", bytes);
            jsonObject.put("cleanerThan", cleanerThan);
            jsonObject.put("timestamp", timestamp);
            return jsonObject;
        } catch (JSONException jsonException) {
            throw new JSONException("");
        }
    }

    /**
     * Converts a JSON object to a CarbonData object.
     *
     * @param jsonObject The JSON object to convert
     * @return The converted WebsiteCarbonData object
     * @throws JSONException if there is an error during JSON conversion
     */
    public static CarbonData fromJSON(JSONObject jsonObject) throws JSONException {
        try {
            //Extracts the data from the JSON object and creates a new CarbonData object
            String url = jsonObject.getString("url");
            String green = "unknown";
            if (jsonObject.has("green")) {
                Object greenValue = jsonObject.get("green");
                green = greenValue.toString();
            }
            long bytes = jsonObject.getLong("bytes");
            double cleanerThan = jsonObject.getDouble("cleanerThan");
            long timestamp = jsonObject.getLong("timestamp");
            return new CarbonData(url, green, bytes, cleanerThan, timestamp);
        } catch (JSONException e) {
            String errorMessage = "An error occurred while parsing the JSON object: " + e.getMessage();
            throw new JSONException(errorMessage);
        }
    }

    /**
     * Loads a list of CarbonData objects from a JSON file.
     *
     * @param filePath The path of the JSON file to load
     * @return The list of WebsiteCarbonData objects loaded from the file
     * @throws IOException   if there is an error reading the file
     * @throws JSONException if there is an error during JSON conversion
     */
    public static List<CarbonData> loadFromJSONFile(String filePath) throws IOException, JSONException {
        List<CarbonData> dataList = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                StringBuilder jsonData = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    jsonData.append(line);
                }
                //Iterates through the JSON file and adds the data to the dataList
                JSONArray jsonArray = new JSONArray(jsonData.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject dataObj = jsonArray.getJSONObject(i);
                        CarbonData websiteData = CarbonData.fromJSON(dataObj);
                        dataList.add(websiteData);
                    } catch (JSONException e) {
                        String errorMessage = "An error occurred while parsing JSON data at index " + i
                                + ": " + e.getMessage();
                        throw new JSONException(errorMessage);
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * Saves a list of CarbonData objects to a JSON file.
     *
     * @param dataList The list of WebsiteCarbonData objects to save
     * @param filePath The path of the JSON file to save
     * @throws IOException   if there is an error writing to the file
     * @throws JSONException if there is an error during JSON conversion
     */
    public static void saveToJSONFile(List<CarbonData> dataList, String filePath)
            throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();
        //Converts the list of CarbonData objects to a JSON array
        for (CarbonData websiteData : dataList) {
            try {
                JSONObject dataObj = websiteData.toJSON();
                jsonArray.put(dataObj);
            } catch (JSONException e) {
                String errorMessage = "An error occurred while creating JSON data for CarbonData: "
                        + e.getMessage();
                throw new JSONException(errorMessage);
            }
        }
        //Writes to the file
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonArray.toString());
        }
    }

}
