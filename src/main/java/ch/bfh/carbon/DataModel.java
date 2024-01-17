package ch.bfh.carbon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the model for the website data in the application.
 */
public class DataModel {

    //Represent the model for the website data in the application
    private ObservableList<CarbonData> data;

    /**
     * Constructs a new WebsiteDataModel object.
     */
    public DataModel() {
        data = FXCollections.observableArrayList();
    }

    /**
     * Adds new website carbon data to the model.
     *
     * @param obj The WebsiteCarbonData object to add
     */
    public void addNewWebsiteData(CarbonData obj) {
        boolean didNotFind = true;
        List<CarbonData> remove = new ArrayList<>();
        //Check if the data is already in the list
        for (CarbonData webData : data) {
            if (webData.getUrl().equals(obj.getUrl())) {
                remove.add(webData); // Duplicate Data for removal
                didNotFind = false; //set to false
            }
        }
        data.removeAll(remove); // Remove elements outside the loop
        if (didNotFind) {
            data.add(obj);
        } else {
            data.add(obj); // Add the new object after removing duplicates
        }
    }

    /**
     * Returns the observable list of website carbon data.
     *
     * @return The observable list of WebsiteCarbonData objects
     */
    public ObservableList<CarbonData> getData() {
        return data;
    } //	return data

    /**
     * set the observable list of website carbon data.
     *
     * @param data set observable list of WebsiteCarbonData objects
     */
    public void setData(ObservableList<CarbonData> data) {
        this.data = data; //set data
    }
}