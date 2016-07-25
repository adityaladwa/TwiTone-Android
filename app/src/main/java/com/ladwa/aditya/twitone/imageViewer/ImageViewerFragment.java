package com.ladwa.aditya.twitone.imageviewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ladwa.aditya.twitone.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageViewerFragment extends Fragment {

    public ImageViewerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        final String imageUrl = getActivity().getIntent().getStringExtra(getActivity().getString(R.string.extra_url));
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById(R.id.imageView);
        final MaterialProgressBar materialProgressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar_imageViewer);

        Glide.with(getActivity().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImage(ImageSource.bitmap(resource));
                        materialProgressBar.setVisibility(View.GONE);
                    }
                });

        return view;
    }
}
