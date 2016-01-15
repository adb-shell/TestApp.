package com.karthik.imager.APIService.Unsplash.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 20/12/15.
 */
public class Photos {
    @SerializedName("id")
    public String id;

    @SerializedName("height")
    public String height;

    @SerializedName("width")
    public String width;

    @SerializedName("color")
    public String color;

    @SerializedName("user")
    public User user;

    @SerializedName("urls")
    public Urls urls;

    @SerializedName("links")
    public PhotoLinks links;
}
