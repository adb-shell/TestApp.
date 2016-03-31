package com.karthik.imager.APIModels;

/**
 * Created by karthikrk on 27/12/15.
 */

//This class used to make generic list of objects in adapter of recycler view
public class GridItem {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullImageUrl() {
        return fullImageUrl;
    }

    public void setFullImageUrl(String fullImageUrl) {
        this.fullImageUrl = fullImageUrl;
    }

    private String id;
    private String username;
    private String imageUrl;
    private String fullImageUrl;

    public GridItem(String id,String username,String imageUrl,String fullImageUrl){
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.fullImageUrl = fullImageUrl;
    }
}
