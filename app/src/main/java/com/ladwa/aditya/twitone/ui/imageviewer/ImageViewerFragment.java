package com.ladwa.aditya.twitone.ui.imageviewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ladwa.aditya.twitone.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageViewerFragment extends Fragment {
    private static final int STORAGE_REQUEST_CODE = 120;
    private SubsamplingScaleImageView imageView;
    private String imageUrl;

    public ImageViewerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);

        imageUrl = getActivity().getIntent().getStringExtra(getActivity().getString(R.string.extra_url));
        imageView = (SubsamplingScaleImageView) view.findViewById(R.id.imageView);
        final MaterialProgressBar materialProgressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar_imageViewer);
        setHasOptionsMenu(true);

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_image_viewer, menu);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    Toast.makeText(getActivity(), R.string.storage_permission, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_REQUEST_CODE);

//                    Timber.d("No permission");
                    break;
                }


                saveImage();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        Glide.with(getActivity().getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        new DownloadImage().execute(resource);
                    }
                });
    }

    private class DownloadImage extends AsyncTask<Bitmap, Boolean, Void> {
        boolean success = false;
        String imagepath;

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {


            Bitmap bm = bitmaps[0];

            String path = Environment.getExternalStorageDirectory().toString();
            File dir = new File(path, "/Twitone");
            try {
                dir.mkdirs();


                File img = new File(dir, UUID.randomUUID().toString() +  ".jpeg");
                FileOutputStream outStream = new FileOutputStream(img);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

//                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), img.getAbsolutePath(), img.getName(), img.getName());
                MediaScannerConnection.scanFile(getActivity().getApplicationContext(), new String[]{img.getPath()}, new String[]{"image/jpeg"}, null);
                outStream.flush();
                outStream.close();
                success = true;
                imagepath = img.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (success)
                Toast.makeText(getActivity().getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
