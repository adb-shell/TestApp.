package com.karthik.imager.APIModels.Unsplash.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 20/12/15.
 */
public class User {
    @SerializedName("id")
    public String id;
    @SerializedName("username")
    public String username;
    @SerializedName("name")
    public String name;
    @SerializedName("links")
    public Links links;
}
