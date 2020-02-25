package com.anythink.network.appnext;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.appnext.core.AppnextAdCreativeType;
import com.appnext.core.AppnextError;
import com.appnext.nativeads.MediaView;
import com.appnext.nativeads.NativeAd;
import com.appnext.nativeads.NativeAdListener;
import com.appnext.nativeads.NativeAdRequest;
import com.appnext.nativeads.NativeAdView;
import com.appnext.nativeads.PrivacyIcon;
import com.anythink.core.api.AdError;
import com.anythink.core.api.ErrorCode;
import com.anythink.nativead.unitgroup.api.CustomNativeAd;

import java.util.List;

public class AppnextATNativeAd extends CustomNativeAd {
    private final String TAG = AppnextATNativeAd.class.getSimpleName();

    Context mContext;
    String mPlacementId;

    NativeAd mNativeAd;
    NativeAdView mNativeAdView;
    MediaView mMediaView;

    LoadCallbackListener mCustomNativeListener;

    public AppnextATNativeAd(Context context, String placementId, LoadCallbackListener customNativeListener) {
        mContext = context.getApplicationContext();
        mPlacementId = placementId;
        mCustomNativeListener = customNativeListener;

    }

    public void loadAd() {
        log(TAG, "loadad");
        NativeAd nativeAd = new NativeAd(mContext, mPlacementId);
        nativeAd.setPrivacyPolicyColor(PrivacyIcon.PP_ICON_COLOR_LIGHT);
        nativeAd.setAdListener(new NativeAdListener() {
            public void onAdLoaded(NativeAd nativeAd, AppnextAdCreativeType creativeType) {
                log(TAG, "onAdLoaded");
                mNativeAd = nativeAd;
                setData();
                if (mCustomNativeListener != null) {
                    mCustomNativeListener.onSuccess( AppnextATNativeAd.this);
                }
            }

            public void onAdClicked(NativeAd nativeAd) {
                log(TAG, "onAdClicked");
                notifyAdClicked();
            }

            public void onError(NativeAd nativeAd, AppnextError error) {
                log(TAG, "onError:" + error.getErrorMessage());
                if (mCustomNativeListener != null) {
                    mCustomNativeListener.onFail(ErrorCode.getErrorCode(ErrorCode.noADError, "", error.getErrorMessage()));
                }
            }

            public void adImpression(NativeAd nativeAd) {
            }
        });
        nativeAd.loadAd(new NativeAdRequest()
                // optional - config your ad request:
                .setCachingPolicy(NativeAdRequest.CachingPolicy.ALL)
                .setCreativeType(NativeAdRequest.CreativeType.ALL)
                .setVideoLength(NativeAdRequest.VideoLength.SHORT)
                .setVideoQuality(NativeAdRequest.VideoQuality.LOW));

    }

    private void setData() {
        setIconImageUrl(mNativeAd.getIconURL());
        setMainImageUrl(mNativeAd.getWideImageURL());
        setTitle(mNativeAd.getAdTitle());
        setDescriptionText(mNativeAd.getAdDescription());
        setCallToActionText(mNativeAd.getCTAText());
        String rate = mNativeAd.getStoreRating();
        try {
            setStarRating(Double.parseDouble(rate));
        } catch (Exception e) {

        }
        NativeAdView nativeAdView = new NativeAdView(mContext);
        MediaView mediaView = new MediaView(mContext);

        if (TextUtils.isEmpty(mNativeAd.getVideoUrl())) {
            mAdSourceType = IMAGE_TYPE;
        } else {
            mAdSourceType = VIDEO_TYPE;
        }
        mNativeAd.setNativeAdView(nativeAdView);
        mNativeAd.setMediaView(mediaView);

    }

    @Override
    public ViewGroup getCustomAdContainer() {
        mNativeAdView = new NativeAdView(mContext);
        mMediaView = new MediaView(mContext);
        if (mNativeAd != null) {
            mNativeAd.setNativeAdView(mNativeAdView);
            mNativeAd.setMediaView(mMediaView);
        }
        return mNativeAdView;
    }

    // Lifecycle Handlers
    @Override
    public void prepare(final View view, FrameLayout.LayoutParams layoutParams) {
        if (view == null) {
            return;
        }
        if (mNativeAd != null) {
            mNativeAd.registerClickableViews(view);
        }
        log(TAG, "prepare");
    }

    @Override
    public void prepare(View view, List<View> clickViewList, FrameLayout.LayoutParams layoutParams) {
        if (view == null) {
            return;
        }

        if (mNativeAd != null) {
            mNativeAd.registerClickableViews(clickViewList);
        }
    }

    @Override
    public void clear(final View view) {
        log(TAG, "clear");
    }


    @Override
    public View getAdMediaView(Object... object) {
        return mMediaView;
    }

    @Override
    public void destroy() {
        log(TAG, "destory");
        try {
            if (mNativeAd != null) {
                mNativeAd.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface LoadCallbackListener{
        public void onSuccess(CustomNativeAd customNativeAd);
        public void onFail(AdError adError);
    }
}
