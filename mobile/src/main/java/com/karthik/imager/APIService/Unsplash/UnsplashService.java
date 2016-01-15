package com.karthik.imager.APIService.Unsplash;

import com.karthik.imager.APIService.Unsplash.Model.Photos;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by karthikrk on 20/12/15.
 */
public interface UnsplashService {

    @GET("/photos/")
    Call<List<Photos>> getPhotos(@Query("client_id") String clientId);

}
