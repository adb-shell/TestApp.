package com.karthik.imager.APIModels.FiveHunPx.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 27/12/15.
 */
public class FiveHunPhotos {
    @SerializedName("id")
    public String id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("camera")
    public String camera;

    @SerializedName("width")
    public String width;

    @SerializedName("height")
    public String height;

    @SerializedName("image_url")
    public String image_url;

    @SerializedName("user")
    public User user;
}
