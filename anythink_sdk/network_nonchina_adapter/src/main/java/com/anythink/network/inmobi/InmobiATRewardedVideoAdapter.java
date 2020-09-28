package com.anythink.network.inmobi;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.anythink.core.api.ATMediationSetting;
import com.anythink.core.api.ErrorCode;
import com.anythink.rewardvideo.unitgroup.api.CustomRewardVideoAdapter;
import com.inmobi.ads.AdMetaInfo;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.ads.listeners.InterstitialAdEventListener;

import java.util.Map;

/**
 * Created by zhou on 2018/6/27.
 */

public class InmobiATRewardedVideoAdapter extends CustomRewardVideoAdapter {
    private static final String TAG = InmobiATRewardedVideoAdapter.class.getSimpleName();

    InMobiInterstitial interstitialAd;
    Long placeId;

    int mClickCallbackType;


    /***
     * init and load
     */
    private void initAndLoad(final Context context, final Map<String, Object> serverExtras) {
        InmobiATInitManager.getInstance().initSDK(context.getApplicationContext(), serverExtras, new InmobiATInitManager.OnInitCallback() {
            @Override
            public void onSuccess() {
                try {
                    startLoadAd(context);
                } catch (Throwable e) {
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("", e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError("", "Inmobi " + errorMsg);
                }
            }
        });
    }

    private void startLoadAd(Context context) {
        interstitialAd = new InMobiInterstitial(context.getApplicationContext(), placeId, new InterstitialAdEventListener() {
            @Override
            public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
                InmobiATInitManager.getInstance().removeInmobiAd(interstitialAd);
                if (mLoadListener != null) {
                    mLoadListener.onAdLoadError("" + inMobiAdRequestStatus.getStatusCode(), inMobiAdRequestStatus.getMessage());
                }
            }

            @Override
            public void onAdFetchSuccessful(InMobiInterstitial inMobiInterstitial, AdMetaInfo adMetaInfo) {
                if (mLoadListener != null) {
                    mLoadListener.onAdDataLoaded();
                }
            }

            @Override
            public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial, AdMetaInfo adMetaInfo) {
                if (mLoadListener != null) {
                    mLoadListener.onAdCacheLoaded();
                }
            }

            @Override
            public void onRewardsUnlocked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayEnd();
                }

                if (mImpressionListener != null) {
                    mImpressionListener.onReward();
                }
            }

            @Override
            public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {
                InmobiATInitManager.getInstance().removeInmobiAd(interstitialAd);
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayFailed("", "AdDisplayFailed");
                }
            }

            @Override
            public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {
            }

            @Override
            public void onAdDisplayed(InMobiInterstitial inMobiInterstitial, AdMetaInfo adMetaInfo) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayStart();
                }
            }

            @Override
            public void onAdClicked(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdPlayClicked();
                }
            }


            @Override
            public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {
                InmobiATInitManager.getInstance().removeInmobiAd(interstitialAd);
                if (mImpressionListener != null) {
                    mImpressionListener.onRewardedVideoAdClosed();
                }
            }
        });
        InmobiATInitManager.getInstance().addInmobiAd(interstitialAd);
        interstitialAd.load();
    }


    @Override
    public void loadCustomNetworkAd(Context context, Map<String, Object> serverExtras, Map<String, Object> localExtras) {

        String accountId = (String) serverExtras.get("app_id");
        String unitId = (String) serverExtras.get("unit_id");

        if (TextUtils.isEmpty(accountId) || TextUtils.isEmpty(unitId)) {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "inmobi account_id or unit_id is empty!");
            }
            return;
        }
        placeId = Long.parseLong(unitId);
        mClickCallbackType = 0;
        initAndLoad(context, serverExtras);
    }

    @Override
    public boolean isAdReady() {
        if (interstitialAd != null) {
            return interstitialAd.isReady();
        }
        return false;
    }

    @Override
    public boolean setUserDataConsent(Context context, boolean isConsent, boolean isEUTraffic) {
        return InmobiATInitManager.getInstance().setUserDataConsent(context, isConsent, isEUTraffic);
    }

    @Override
    public void show(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show();
        }
    }

    @Override
    public void destory() {
        if (interstitialAd != null) {
            try {
                interstitialAd.setListener(null);
            } catch (Throwable e) {
            }
            interstitialAd = null;
        }
    }


    @Override
    public String getNetworkSDKVersion() {
        return InmobiATConst.getNetworkVersion();
    }

    @Override
    public String getNetworkName() {
        return InmobiATInitManager.getInstance().getNetworkName();
    }

    @Override
    public String getNetworkPlacementId() {
        try {
            return String.valueOf(placeId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}