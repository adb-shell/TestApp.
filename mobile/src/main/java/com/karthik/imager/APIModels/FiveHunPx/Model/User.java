package com.karthik.imager.APIModels.FiveHunPx.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 27/12/15.
 */
public class User {
    @SerializedName("username")
    public String username;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("fullname")
    public String fullname;
}
