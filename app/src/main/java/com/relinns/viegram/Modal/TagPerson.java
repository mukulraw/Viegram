package com.relinns.viegram.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 24-07-2017.
 */
public class TagPerson {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("x_cordinates")
    @Expose
    private String xCordinates;
    @SerializedName("y_cordinates")
    @Expose
    private String yCordinates;

    public TagPerson(String id, String displayName, String xCordinates, String yCordinates) {
        this.id = id;
        this.displayName = displayName;
        this.xCordinates = xCordinates;
        this.yCordinates = yCordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getXCordinates() {
        return xCordinates;
    }

    public void setXCordinates(String xCordinates) {
        this.xCordinates = xCordinates;
    }

    public String getYCordinates() {
        return yCordinates;
    }

    public void setYCordinates(String yCordinates) {
        this.yCordinates = yCordinates;
    }
}
