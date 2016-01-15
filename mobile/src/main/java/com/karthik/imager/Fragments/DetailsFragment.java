package com.karthik.imager.Fragments;

import com.karthik.imager.R;
import com.karthik.imager.Animutils.AnimatorPath;
import com.karthik.imager.Animutils.PathEvaluator;
import com.karthik.imager.Animutils.PathPoint;
import com.karthik.imager.Animutils.ReverseInterpolator;
import com.karthik.imager.WallpaperTask;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by karthikrk on 08/01/16.
 */
public class DetailsFragment  extends Fragment {
    //arguements
    public static String DETAIL_ARG1 = "detail_arg1";
    public static String DETAIL_ARG2 = "detail_arg2";
    public static String DETAIL_ARG3 = "detail_arg3";

    private Context mContext;
    private boolean mRevealFlag,isDownloading;
    private float mFabSize;
    private ProgressDialog dialog;
    private WallpaperTask task;


    //view variables
    @Bind(R.id.detail_image)
    ImageView imageView;

    @Bind(R.id.detail_fab)
    FloatingActionButton fab;

    @Bind(R.id.reveal_layout)
    LinearLayout revealLayout;

    @Bind(R.id.non_reveal_layout)
    RelativeLayout non_reveal_layout;

    @Bind(R.id.image_cancel)
    ImageView cancel_image;

    @Bind(R.id.image_reveal1)
    ImageView wallpaper;

    @Bind(R.id.text_reveal2)
    TextView credits;

    @Bind(R.id.row3)
    RelativeLayout row3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,rootView);

        mContext = getActivity();
        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);

        Picasso.with(mContext)
                .load(getArguments().getString(DETAIL_ARG1))
                .fit()
                .noPlaceholder()
                .centerCrop()
                .into(imageView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed(v);
            }
        });

        return rootView;
    }


    private void onFabPressed(final View view) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(-200, -50, -230, -450, -255, -500);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(300);
        anim.start();

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //make a circular reveal animations.
                showRevealLayout(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void reverseFabPressed(){
        AnimatorPath path = new AnimatorPath();
        path.moveTo(0, 0);
        path.curveTo(-200, -50, -230, -450, -255, -500);

        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "fabLoc",
                new PathEvaluator(), path.getPoints().toArray());

        //path animator to reverse the animation
        anim.setInterpolator(new ReverseInterpolator());
        anim.setDuration(300);
        anim.start();

    }

    //revealing layout.
    private void showRevealLayout(final View buttonView){

        revealLayout.setVisibility(View.VISIBLE);
        non_reveal_layout.setVisibility(View.GONE);
        credits.setText("Photo Credits: "+getArguments().getString(DETAIL_ARG2));

        mRevealFlag = true;
        int cx,cy;

        float offset_X = getinPixels(mContext, 32);
        float offset_Y = getinPixels(mContext, 50);

        cx = (int) (buttonView.getX()+offset_X);
        cy = (int) (buttonView.getY()+offset_Y);
        int finalRadius = Math.max(revealLayout.getWidth(), revealLayout.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, 0, finalRadius);
        anim.start();

        setListners(true);

    }

    private void setListners(boolean isSet){
        if(isSet){

            cancel_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideRevealLayout();
                }
            });

            wallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(task==null){
                        dialog = new ProgressDialog(mContext);
                        dialog.setTitle("Setting up wallpaper");
                        dialog.setMessage("Please wait");
                        dialog.setCancelable(false);
                        dialog.show();

                        //async task to set wallpaper
                        task = new WallpaperTask(getArguments().getString(DETAIL_ARG1),mContext);
                        task.execute();
                    }
                }
            });


            row3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Avoiding multiple downloads
                    if(!isDownloading) {
                        isDownloading = true;
                        Toast.makeText(mContext, "Downloading", Toast.LENGTH_LONG).show();
                        Uri downloadUri = Uri.parse(getArguments().getString(DETAIL_ARG3));
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle("Downloading")
                                .setDescription("Image File Download");

                        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                        request.setDescription("Downloading a file");
                        final long id = downloadManager.enqueue(request);
                    }else{
                        Toast.makeText(mContext, "File is been downloaded", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            cancel_image.setOnClickListener(null);
            wallpaper.setOnClickListener(null);
            row3.setOnClickListener(null);
        }
    }

    private void hideRevealLayout(){
        setListners(false);

        mRevealFlag = false;

        int cx =0,cy=0;
        float offset_X = getinPixels(mContext,32);
        float offset_Y = getinPixels(mContext,50);

        cx = (int) (fab.getX()+offset_X);
        cy = (int) (fab.getY()+offset_Y);

        int initialRadius = revealLayout.getWidth();

        non_reveal_layout.setVisibility(View.VISIBLE);

        Animator anim = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //hide the layout
                revealLayout.setVisibility(View.GONE);

                reverseFabPressed();
            }
        });

        anim.start();
    }

    public void setFabLoc(PathPoint newLoc) {
        fab.setTranslationX(newLoc.mX);

        if (mRevealFlag)
            fab.setTranslationY(newLoc.mY - (mFabSize / 2));
        else
            fab.setTranslationY(newLoc.mY);
    }

    //this method is called by the main activity when user presses back
    //this is used for the reversing animation if present.
    public void onBackPressed(){
        if(mRevealFlag){
            hideRevealLayout();
        }else{
            getFragmentManager().popBackStack();
        }
    }

    private float getinPixels(Context context,int dp){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (dp *displayMetrics.density + 0.5f);
    }

    //this method is used as a callback from the wallpaper asynctask
    public void wallpaperSetCallback(boolean isSuccess){
        dialog.dismiss();
        task = null;
        if(isSuccess){
            Toast.makeText(mContext,"Wallpaper Changed",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mContext, "Please check your network connection", Toast.LENGTH_LONG).show();
        }
    }

}
