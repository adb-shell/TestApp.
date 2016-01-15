package com.karthik.imager.Fragments;

import com.google.gson.Gson;
import com.karthik.imager.APIService.FiveHunPx.Model.FiveHunPhotos;
import com.karthik.imager.APIService.FiveHunPx.Model.FiveHunResponse;
import com.karthik.imager.APIService.GridItem;
import com.karthik.imager.APIService.Unsplash.Model.Photos;
import com.karthik.imager.APIService.Unsplash.UnsplashService;
import com.karthik.imager.Adapter;
import com.karthik.imager.DetailsTransition;
import com.karthik.imager.MainActivity;
import com.karthik.imager.R;
import com.karthik.imager.Recycler.GridItemDividerDecoration;
import com.karthik.imager.Recycler.PhotoClickListner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashboardFragment extends Fragment implements PhotoClickListner{

    //register your application at unsplash.com and get the client id
    private final String unsplash_client_Id = "";
    private final String UNSPLASHBASEURL = "https://api.unsplash.com";


    private Context mContext;
    private List<GridItem> gridItems;
    private Adapter gridAdapter;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.progress)
    ProgressBar progressBar;

    //workaround: keep reference so that shared animation works else each new view/adapter loaded.
    //TODO find a better soloution for this piece of code.
    View rootView;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null){
            rootView =  inflater.inflate(R.layout.fragment_main, container, false);

            mContext = getActivity();
            ButterKnife.bind(this, rootView);
            if(gridItems==null){
                getUnspalashService();
            }else{
                loadData();
            }
        }

        return rootView;
    }


    private void getUnspalashService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UNSPLASHBASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UnsplashService service = retrofit.create(UnsplashService.class);

        retrofit.Call<List<Photos>> photos = service.getPhotos(unsplash_client_Id);

        //in case more api calls simply call.clone.enqueue()
        //To call execute that is synchronus and should be outside the main thread.
        photos.enqueue(new Callback<List<Photos>>() {
            @Override
            public void onResponse(Response<List<Photos>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    convertUnsplashGridItems(response.body());
                } else {
                    showSnackBar("Please check your network connection", null);
                }
                getFivehunService();
            }

            @Override
            public void onFailure(Throwable t) {
                getFivehunService();
                showSnackBar("Please check your network connection", null);
            }
        });
    }



    private void getFivehunService(){

        FiveHunResponse fiveHunResponse = new Gson().fromJson(loadJSONFromAsset(), FiveHunResponse.class);
        convertFiveHunGridItems(fiveHunResponse.photoList);

    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("500px.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    private void loadData(){

        //for the first time when grid adapter is not set
        gridAdapter = new Adapter(gridItems,mContext,this);
        recyclerView.addItemDecoration(new GridItemDividerDecoration(gridAdapter.getDividedViewHolderClasses(),
                mContext, R.dimen.divider_height, R.color.divider));

        recyclerView.setAdapter(gridAdapter);

        //StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,2);

        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);

        //this is to dynamically set the coloumn span size here you have to specify the span it is going to take
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 5 == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });


        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.GONE);
    }


    private void convertUnsplashGridItems(List<Photos> photosList){

        if(gridItems==null){
            gridItems = new ArrayList<>();
        }

        //for each will take memory for each iterator
        if(photosList!=null && photosList.size()>0) {

            for (Photos photo : photosList) {
                GridItem gridItem = new GridItem(photo.id,photo.user.name,getBestPossibleImage(photo),photo.urls.full);
                gridItems.add(gridItem);
            }
        }else{
            //TODO Handle in case of API error or no internet connection

        }
    }


    private void convertFiveHunGridItems(List<FiveHunPhotos> photosList){

        if(gridItems==null){
            gridItems = new ArrayList<>();
        }

        //for each will take memory for each iterator
        if(photosList!=null && photosList.size()>0) {

            for (FiveHunPhotos photo : photosList) {
                GridItem gridItem = new GridItem(photo.id,photo.user.username,photo.image_url,photo.image_url);
                gridItems.add(gridItem);
            }
            loadData();

        }else{
            //TODO Handle in case of API error or no internet connection

        }

    }

    //to get the best available photos
    private String getBestPossibleImage(Photos photo){
        if(photo.urls.small==null){
            if(photo.urls.regular!=null)
                return photo.urls.regular;
            else
                return photo.urls.full;
        }else{
            return photo.urls.small;
        }
    }


    public void showSnackBar(String message,String colorCode) {
        Snackbar snackbar = Snackbar.make(progressBar, message, Snackbar.LENGTH_LONG);
        if(colorCode==null){
            snackbar.setAction("Action", null).show();
        }else{
            View snackbarView = snackbar.getView();
            TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
            snackbarView.setBackgroundColor(ContextCompat.getColor(mContext, Color.parseColor(colorCode)));
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.primary_dark));
            snackbar.show();
        }
    }

    @Override
    public void onPhotoClick(RecyclerView.ViewHolder holder, int position) {
        //bundle args containing thumb,username and full image url
        Bundle bundle_args = new Bundle();
        bundle_args.putString(DetailsFragment.DETAIL_ARG1,gridItems.get(position).getImageUrl());
        bundle_args.putString(DetailsFragment.DETAIL_ARG2,gridItems.get(position).getUsername());
        bundle_args.putString(DetailsFragment.DETAIL_ARG3,gridItems.get(position).getFullImageUrl());

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle_args);
        detailsFragment.setSharedElementEnterTransition(new DetailsTransition());
        detailsFragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        detailsFragment.setSharedElementReturnTransition(new DetailsTransition());

        if(holder instanceof Adapter.ViewHolder){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(((Adapter.ViewHolder) holder).imageView, getString(R.string.transistion_anim))
                    .replace(R.id.container, detailsFragment)
                    .addToBackStack("")
                    .commit();
        }else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(((Adapter.FullViewHolder)holder).imageView,getString(R.string.transistion_anim))
                    .replace(R.id.container, detailsFragment)
                    .addToBackStack("")
                    .commit();
        }
    }
}
