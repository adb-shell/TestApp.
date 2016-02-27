package com.karthik.imager.Fragments;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karthik.imager.APIService.FiveHunPx.Model.FiveHunPhotos;
import com.karthik.imager.APIService.FiveHunPx.Model.FiveHunResponse;
import com.karthik.imager.APIService.GridItem;
import com.karthik.imager.APIService.Unsplash.Model.Photos;
import com.karthik.imager.Adapter;
import com.karthik.imager.DetailsTransition;
import com.karthik.imager.ImagerApp;
import com.karthik.imager.MainActivity;
import com.karthik.imager.R;
import com.karthik.imager.Recycler.GridItemDividerDecoration;
import com.karthik.imager.Recycler.PhotoClickListner;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashboardFragment extends Fragment implements PhotoClickListner{

    //register your application at unsplash.com and get the client id
    private final String unsplash_client_Id = "";
    private final String UNSPLASHBASEURL = "https://api.unsplash.com/photos/";


    private Context mContext;
    private List<GridItem> gridItems;
    private Adapter gridAdapter;
    private Handler mainUIThread;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView progressView;

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

            //handler that communicates with the UI Thread
            mainUIThread = new Handler(mContext.getMainLooper());

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

        OkHttpClient client = ((ImagerApp)((MainActivity)mContext).getApplication()).getOkHttpInstance();

        //adding query parameters.
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UNSPLASHBASEURL).newBuilder();
        urlBuilder.addQueryParameter("client_id",unsplash_client_Id);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Please check your network connection", Toast.LENGTH_SHORT).show();
                        getFivehunService();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                final Gson gson = new Gson();
                final TypeToken<List<Photos>> PhotoList = new TypeToken<List<Photos>>() {};

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    Reader charStream = body.charStream();
                    final List<Photos> photoList = gson.fromJson(charStream, PhotoList.getType());
                    //run the handler in the main thread.
                    mainUIThread.post(new Runnable() {
                        @Override
                        public void run() {
                            convertUnsplashGridItems(photoList);
                            getFivehunService();
                        }
                    });
                    body.close();
                }else{
                    mainUIThread.post(new Runnable() {
                        @Override
                        public void run() {
                            getFivehunService();
                        }
                    });
                }
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
        progressView.setVisibility(View.GONE);
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
