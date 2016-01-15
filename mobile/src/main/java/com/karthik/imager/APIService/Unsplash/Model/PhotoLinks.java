package com.karthik.imager.APIService.Unsplash.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikrk on 20/12/15.
 */
public class PhotoLinks {
    @SerializedName("self")
    public String self;
    @SerializedName("html")
    public String html;
    @SerializedName("download")
    public String download;
}
