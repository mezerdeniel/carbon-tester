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
     * Constructs a new WebsiteCarbonData object with the specified parameters.
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
     * @param cleanerThan The cleanliness rating of the website
     */
    public void setCleanerThan(double cleanerThan) {
        this.cleanerThan = cleanerThan;
    }

    /**
     * Returns the formatted timestamp of the data.
     *
     * @return The formatted timestamp of the data
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
     * Converts the WebsiteCarbonData object to a JSON object.
     *
     * @return The JSON representation of the object
     * @throws JSONException if there is an error during JSON conversion
     */
    public JSONObject toJSON() throws JSONException {
        //Converts the WebsiteCarbonData object to a JSON object
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
