package com.karthik.imager.APIService.Unsplash.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 20/02/16.
 */
public class Categories {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("photo_count")
    public String photo_count;

    @SerializedName("links")
    public Links links;
}
