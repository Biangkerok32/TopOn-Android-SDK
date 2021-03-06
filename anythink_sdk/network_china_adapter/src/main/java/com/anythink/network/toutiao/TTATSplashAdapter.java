/*
 * Copyright © 2018-2020 TopOn. All rights reserved.
 * https://www.toponad.com
 * Licensed under the TopOn SDK License Agreement
 * https://github.com/toponteam/TopOn-Android-SDK/blob/master/LICENSE
 */

package com.anythink.network.toutiao;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.anythink.splashad.unitgroup.api.CustomSplashAdapter;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;

import java.util.Map;

public class TTATSplashAdapter extends CustomSplashAdapter implements TTSplashAd.AdInteractionListener {
    private final String TAG = getClass().getSimpleName();

    String appId = "";
    String slotId = "";
    String personalizedTemplate = "";

    @Override
    public void loadCustomNetworkAd(final Context context, Map<String, Object> serverExtra, Map<String, Object> localExtra) {
        if (serverExtra.containsKey("app_id") && serverExtra.containsKey("slot_id")) {
            appId = (String) serverExtra.get("app_id");
            slotId = (String) serverExtra.get("slot_id");

        } else {
            if (mLoadListener != null) {
                mLoadListener.onAdLoadError("", "app_id or slot_id is empty!");
            }
            return;
        }

        personalizedTemplate = "0";
        if (serverExtra.containsKey("personalized_template")) {
            personalizedTemplate = (String) serverExtra.get("personalized_template");
        }

        TTATInitManager.getInstance().initSDK(context, serverExtra, true, new TTATInitManager.InitCallback() {
            @Override
            public void onFinish() {
                try {
                    startLoad(context);
                } catch (Throwable e) {
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("", e.getMessage());
                    }
                }
            }
        });
    }

    private void startLoad(Context context) {
        TTAdManager ttAdManager = TTAdSdk.getAdManager();

        final TTAdNative mTTAdNative = ttAdManager.createAdNative(context);//baseContext is recommended for activity
        final AdSlot.Builder adSlotBuilder = new AdSlot.Builder().setCodeId(slotId);

        int width = 0;
        int height = 0;
        ViewGroup.LayoutParams layoutParams = mContainer.getLayoutParams();
        if (layoutParams != null) {
            width = layoutParams.width;
            height = layoutParams.height;
        }
        if (width <= 0) {
            width = context.getResources().getDisplayMetrics().widthPixels;
        }
        if (height <= 0) {
            height = context.getResources().getDisplayMetrics().heightPixels;
        }

        adSlotBuilder.setImageAcceptedSize(width, height); //Must be set

        if (TextUtils.equals("1", personalizedTemplate)) {// Native Express
            adSlotBuilder.setExpressViewAcceptedSize(width, height);
        }

        postOnMainThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdSlot adSlot = adSlotBuilder.build();
                    mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                        @Override
                        public void onError(int i, String s) {
                            if (mLoadListener != null) {
                                mLoadListener.onAdLoadError(i + "", s);
                            }
                        }

                        @Override
                        public void onTimeout() {
                            if (mLoadListener != null) {
                                mLoadListener.onAdLoadError("", "onTimeout");
                            }
                        }

                        @Override
                        public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                            if (ttSplashAd != null) {
//                    ttSplashAd.setNotAllowSdkCountdown();
                                ttSplashAd.setSplashInteractionListener(TTATSplashAdapter.this);
                                View splashView = ttSplashAd.getSplashView();
                                if (splashView != null) {
                                    if (mLoadListener != null) {
                                        mLoadListener.onAdCacheLoaded();
                                    }
                                    mContainer.removeAllViews();
                                    mContainer.addView(splashView);
                                } else {
                                    if (mLoadListener != null) {
                                        mLoadListener.onAdLoadError("", "");
                                    }
                                }

                            } else {
                                if (mLoadListener != null) {
                                    mLoadListener.onAdLoadError("", "");
                                }
                            }
                        }
                    }, 5000);
                } catch (Exception e) {
                    if (mLoadListener != null) {
                        mLoadListener.onAdLoadError("", e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public String getNetworkName() {
        return TTATInitManager.getInstance().getNetworkName();
    }

    @Override
    public void destory() {

    }

    @Override
    public String getNetworkPlacementId() {
        return slotId;
    }

    @Override
    public String getNetworkSDKVersion() {
        return TTATConst.getNetworkVersion();
    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mImpressionListener != null) {
            mImpressionListener.onSplashAdClicked();
        }

    }

    @Override
    public void onAdShow(View view, int i) {
        if (mImpressionListener != null) {
            mImpressionListener.onSplashAdShow();
        }

    }

    @Override
    public void onAdSkip() {
        if (mImpressionListener != null) {
            mImpressionListener.onSplashAdDismiss();
        }
    }

    @Override
    public void onAdTimeOver() {
        if (mImpressionListener != null) {
            mImpressionListener.onSplashAdDismiss();
        }
    }

}
