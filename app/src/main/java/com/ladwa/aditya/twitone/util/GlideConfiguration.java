package com.ladwa.aditya.twitone.util;

import android.app.ActivityManager;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * A Configuration class for Glide Image loading library
 * Created by Aditya on 14-Jul-16.
 */
public class GlideConfiguration implements GlideModule {

    private static final int CACHE_SIZE = 20 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
