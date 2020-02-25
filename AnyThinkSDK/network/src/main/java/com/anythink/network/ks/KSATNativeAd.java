package com.anythink.network.ks;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.nativead.unitgroup.api.CustomNativeAd;
import com.kwad.sdk.export.i.KsNativeAd;
import com.kwad.sdk.nativead.KsAppDownloadListener;
import com.kwad.sdk.nativead.KsImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Native Self-rendering
 */
public class KSATNativeAd extends CustomNativeAd {

    Context context;
    KsNativeAd ksNativeAd;
    boolean isVideoSoundEnable;


    public KSATNativeAd(Context context, KsNativeAd ksNativeAd, boolean isVideoSoundEnable) {
        this.context = context;
        this.ksNativeAd = ksNativeAd;
        this.isVideoSoundEnable = isVideoSoundEnable;

        setAdData();
    }

    private void setAdData() {
        setTitle(ksNativeAd.getAppName());
        setIconImageUrl(ksNativeAd.getAppIconUrl());
        setAdFrom(ksNativeAd.getAdSource());
        setStarRating((double) ksNativeAd.getAppScore());
        setDescriptionText(ksNativeAd.getAdDescription());
        List<KsImage> imageList = ksNativeAd.getImageList();
        ArrayList<String> imageStringList = new ArrayList<>();

        if (imageList != null && imageList.size() > 0) {
            for (KsImage ksImage : imageList) {
                imageStringList.add(ksImage.getImageUrl());
            }
            setMainImageUrl(imageStringList.get(0));
        }
        setImageUrlList(imageStringList);
        setCallToActionText(ksNativeAd.getActionDescription());
        setVideoUrl(ksNativeAd.getVideoUrl());

    }

    @Override
    public Bitmap getAdLogo() {
        return ksNativeAd != null? ksNativeAd.getSdkLogo(): null;
    }

    private void getChildView(List<View> childViews, View view) {
        if (view instanceof ViewGroup && view != ksNativeAd.getVideoView(context, isVideoSoundEnable)) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                getChildView(childViews, child);
            }
        } else {
            if (view != ksNativeAd.getVideoView(context, isVideoSoundEnable)) {
                childViews.add(view);
            }
        }
    }

    @Override
    public void prepare(final View view, FrameLayout.LayoutParams layoutParams) {

        List<View> childViews = new ArrayList<>();
        getChildView(childViews, view);
        ksNativeAd.registerViewForInteraction((ViewGroup) view, childViews, new KsNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, KsNativeAd ksNativeAd) {
                notifyAdClicked();
            }

            @Override
            public void onAdShow(KsNativeAd ksNativeAd) {

            }
        });

        ksNativeAd.setDownloadListener(new KsAppDownloadListener() {
            @Override
            public void onIdle() {

            }

            @Override
            public void onProgressUpdate(int i) {

            }

            @Override
            public void onDownloadFinished() {

            }

            @Override
            public void onInstalled() {

            }
        });
    }

    @Override
    public void prepare(View view, List<View> clickViewList, FrameLayout.LayoutParams layoutParams) {

        List<View> childViews;
        if(clickViewList != null && clickViewList.size() > 0) {
            childViews = clickViewList;
        } else {
            childViews = new ArrayList<>();
            getChildView(childViews, view);
        }
        ksNativeAd.registerViewForInteraction((ViewGroup) view, childViews, new KsNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, KsNativeAd ksNativeAd) {
                notifyAdClicked();
            }

            @Override
            public void onAdShow(KsNativeAd ksNativeAd) {

            }
        });

        ksNativeAd.setDownloadListener(new KsAppDownloadListener() {
            @Override
            public void onIdle() {

            }

            @Override
            public void onProgressUpdate(int i) {

            }

            @Override
            public void onDownloadFinished() {

            }

            @Override
            public void onInstalled() {

            }
        });
    }

    @Override
    public View getAdMediaView(Object... object) {
        try {
            return ksNativeAd.getVideoView(context, isVideoSoundEnable);
        } catch (Throwable e) {

        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
