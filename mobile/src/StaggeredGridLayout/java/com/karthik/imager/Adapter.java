package com.karthik.imager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.karthik.imager.APIModels.GridItem;
import com.karthik.imager.Recycler.LikeButtonView;
import com.karthik.imager.Recycler.PhotoClickListner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GridItem> photosList;
    private Context mContext;
    private PhotoClickListner photoClickListner;
    private OkHttpClient okHttpClient;
    private Picasso picasso;

    //hold temporary liked items
    private boolean[] isLiked;

    public static final int ViewHolder = 0;
    public static final int FullViewHolder = 1;

    private String SHIMMER_BACKGROUND = "#D7D7D7";
    private String SHIMMER_BACKGROUND_TRANSPARENT = "#00000000";

    public Adapter(List<GridItem> photosList,Context context,PhotoClickListner  listner,OkHttpClient client){
        super();
        this.photosList = photosList;
        mContext = context;
        photoClickListner = listner;
        okHttpClient = client;
        picasso = new Picasso.Builder(mContext)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
        isLiked = new boolean[photosList.size()];
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case ViewHolder:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.grid_item, parent, false);
                ViewHolder viewHolder = new ViewHolder(v);
                return viewHolder;
            case FullViewHolder:
                v = LayoutInflater.from(mContext)
                        .inflate(R.layout.grid_item_full, parent, false);
                FullViewHolder fullViewHolder = new FullViewHolder(v);
                return fullViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ViewHolder:
                bindViewholder((ViewHolder) holder,position);
                break;
            case FullViewHolder:
                bindFullViewholder((FullViewHolder) holder,position);
                break;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

        shimmerConfig(holder);

        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).txtView.setText("");
        }else{
            ((FullViewHolder)holder).txtView.setText("");
        }
    }

    private void bindViewholder(final ViewHolder holder, final int position){

        //shimer layout config's
        shimmerConfig(holder);

        holder.shimmerFrameLayout.startShimmerAnimation();


        picasso.load(photosList.get(position).getImageUrl()).fit().centerCrop().into(holder.imageView, new Callback() {

            @Override
            public void onSuccess() {
                holder.shimmerFrameLayout.setBackgroundColor(Color.parseColor(SHIMMER_BACKGROUND_TRANSPARENT));
                holder.shimmerFrameLayout.stopShimmerAnimation();
                MaterialImageLoading.animate(holder.imageView).setDuration(2500).start();
                attachViewHolderData(holder, position);
            }

            @Override
            public void onError() {
                //show error message
            }
        });

        holder.heartLike.setIsLikedImageresource(isLiked[position] ? true : false);
    }

    private void bindFullViewholder(final FullViewHolder holder, final int position){

        //shimmer layout config's
        shimmerConfig(holder);

        holder.shimmerFrameLayout.startShimmerAnimation();
        picasso.load(photosList.get(position).getImageUrl()).fit().centerCrop().into(holder.imageView, new Callback() {

            @Override
            public void onSuccess() {
                holder.shimmerFrameLayout.setBackgroundColor(Color.parseColor(SHIMMER_BACKGROUND_TRANSPARENT));
                holder.shimmerFrameLayout.stopShimmerAnimation();
                MaterialImageLoading.animate(holder.imageView).setDuration(2000).start();
                attachFullViewHolderData(holder, position);
            }

            @Override
            public void onError() {
                //show error probably using snackbar
            }
        });

        holder.heartLike_full.setIsLikedImageresource(isLiked[position] ? true : false);
    }

    private void attachViewHolderData(final ViewHolder holder,final int position){
        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + "_image");

        //only after that material anim
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoClickListner.onPhotoClick(holder, position);
            }
        });

        holder.txtView.setText(photosList.get(position).getUsername());


        holder.heartLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked[position] = !isLiked[position];
                holder.heartLike.AnimClick(isLiked[position]);
            }
        });
    }


    private void attachFullViewHolderData(final FullViewHolder holder,final int position){
        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + "_image");

        //only after that material anim
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoClickListner.onPhotoClick(holder, position);
            }
        });

        holder.txtView.setText(photosList.get(position).getUsername());

        holder.heartLike_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked[position] = !isLiked[position];
                holder.heartLike_full.AnimClick(isLiked[position]);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (photosList!=null)
            return photosList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.card_image_view)
        public ImageView imageView;

        @Bind(R.id.user_name)
        TextView txtView;

        @Bind(R.id.heart_like)
        LikeButtonView heartLike;

        @Bind(R.id.shimmer_view_container)
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FullViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.full_card_image_view)
        public ImageView imageView;

        @Bind(R.id.user_name)
        TextView txtView;

        @Bind(R.id.heart_like_full)
        LikeButtonView heartLike_full;

        @Bind(R.id.shimmer_view_container)
        ShimmerFrameLayout shimmerFrameLayout;

        public FullViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //two view types defined here.
    @Override
    public int getItemViewType(int position) {
        if(position%2==0){
            return FullViewHolder;
        }else{
            return ViewHolder;
        }
    }


    //which image to show from the small regular and full
    public Class[] getDividedViewHolderClasses() {
        return new Class[] { ViewHolder.class,FullViewHolder.class };
    }

    //settings shimmer layout config's
    private void shimmerConfig(RecyclerView.ViewHolder holder){
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.shimmerFrameLayout.setBackgroundColor(Color.parseColor(SHIMMER_BACKGROUND));
            viewHolder.shimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
            viewHolder.shimmerFrameLayout.setTilt(0);
        }else{
            FullViewHolder fullViewHolder = (FullViewHolder)holder;
            fullViewHolder.shimmerFrameLayout.setBackgroundColor(Color.parseColor(SHIMMER_BACKGROUND));
            fullViewHolder.shimmerFrameLayout.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
            fullViewHolder.shimmerFrameLayout.setTilt(0);
        }
    }
}

