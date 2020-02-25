package com.anythink.network.startapp;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.anythink.core.api.ATMediationSetting;
import com.anythink.core.api.AdError;
import com.anythink.core.api.ErrorCode;
import com.anythink.interstitial.unitgroup.api.CustomInterstitialAdapter;
import com.anythink.interstitial.unitgroup.api.CustomInterstitialListener;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.Map;

public class StartAppATInterstitialAdapter extends CustomInterstitialAdapter {

    StartAppAd startAppAd;
    String adTag = "";

    @Override
    public void loadInterstitialAd(Context context, Map<String, Object> serverExtras, ATMediationSetting mediationSetting, CustomInterstitialListener customInterstitialListener) {

        String appId = "";

        if (serverExtras.containsKey("app_id")) {
            appId = serverExtras.get("app_id").toString();
        }
        if (serverExtras.containsKey("ad_tag")) {
            adTag = serverExtras.get("ad_tag").toString();
        }

        if (adTag == null) {
            adTag = "";
        }

        mLoadResultListener = customInterstitialListener;

        if (TextUtils.isEmpty(appId)) {
            if (mLoadResultListener != null) {
                AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", "app_id could not be null.");
                mLoadResultListener.onInterstitialAdLoadFail(this, adError);
            }
            return;
        }

        if (!(context instanceof Activity)) {
            if (mLoadResultListener != null) {
                AdError adError = ErrorCode.getErrorCode(ErrorCode.noADError, "", "context need be activity.");
                mLoadResultListener.onInterstitialAdLoadFail(this, adError);
            }
            return;
        }

        StartAppATInitManager.getInstance().initSDK(context, serverExtras);
        startAppAd = new StartAppAd(context);
        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC, new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                if (mLoadResultListener != null) {
                    mLoadResultListener.onInterstitialAdLoaded(StartAppATInterstitialAdapter.this);
                }
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                if (mLoadResultListener != null) {
                    if (ad != null) {
                        mLoadResultListener.onInterstitialAdLoadFail(StartAppATInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", ad.getErrorMessage()));
                    } else {
                        mLoadResultListener.onInterstitialAdLoadFail(StartAppATInterstitialAdapter.this, ErrorCode.getErrorCode(ErrorCode.noADError, "", "StartApp has not error msg."));
                    }
                }
            }
        });

    }

    @Override
    public void show(Context context) {
        if (startAppAd.isReady()) {
            startAppAd.showAd(adTag, new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    if (mImpressListener != null) {
                        mImpressListener.onInterstitialAdClose(StartAppATInterstitialAdapter.this);
                    }
                }

                @Override
                public void adDisplayed(Ad ad) {
                    if (mImpressListener != null) {
                        mImpressListener.onInterstitialAdShow(StartAppATInterstitialAdapter.this);
                    }
                }

                @Override
                public void adClicked(Ad ad) {
                    if (mImpressListener != null) {
                        mImpressListener.onInterstitialAdClicked(StartAppATInterstitialAdapter.this);
                    }
                }

                @Override
                public void adNotDisplayed(Ad ad) {
                }
            });
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public boolean isAdReady() {
        return startAppAd != null && startAppAd.isReady();
    }

    @Override
    public String getSDKVersion() {
        return StartAppATConst.getSDKVersion();
    }

    @Override
    public void clean() {

    }

    @Override
    public String getNetworkName() {
        return StartAppATInitManager.getInstance().getNetworkName();
    }
}
