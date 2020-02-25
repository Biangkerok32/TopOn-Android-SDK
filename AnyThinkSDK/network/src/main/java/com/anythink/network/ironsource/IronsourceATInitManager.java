package com.anythink.network.ironsource;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.anythink.core.api.ATInitMediation;
import com.anythink.core.common.base.AnyThinkBaseAdapter;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyInterstitialListener;
import com.ironsource.mediationsdk.sdk.ISDemandOnlyRewardedVideoListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IronsourceATInitManager extends ATInitMediation {

    private static final String TAG = IronsourceATInitManager.class.getSimpleName();
    private String mAppKey;
    private static IronsourceATInitManager sInstance;
    private Handler mHandler;

    private ConcurrentHashMap<String, AnyThinkBaseAdapter> mAdapterMap;
    private ConcurrentHashMap<String, AnyThinkBaseAdapter> mLoadResultAdapterMap;

    ISDemandOnlyInterstitialListener isDemandOnlyInterstitialListener = new ISDemandOnlyInterstitialListener() {
        @Override
        public void onInterstitialAdReady(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mLoadResultAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceATInterstitialAdapter) {
                ((IronsourceATInterstitialAdapter) baseAdapter).onInterstitialAdReady();
            }
            removeLoadResultAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdLoadFailed(String instanceId, IronSourceError ironSourceError) {
            AnyThinkBaseAdapter baseAdapter = mLoadResultAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceATInterstitialAdapter) {
                ((IronsourceATInterstitialAdapter) baseAdapter).onInterstitialAdLoadFailed(ironSourceError);
            }
            removeLoadResultAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdOpened(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceATInterstitialAdapter) {
                ((IronsourceATInterstitialAdapter) baseAdapter).onInterstitialAdOpened();
            }
        }

        @Override
        public void onInterstitialAdClosed(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceATInterstitialAdapter) {
                ((IronsourceATInterstitialAdapter) baseAdapter).onInterstitialAdClosed();
            }
            removeAdapter("inter_" + instanceId);
        }


        @Override
        public void onInterstitialAdShowFailed(String instanceId, IronSourceError ironSourceError) {
            removeAdapter("inter_" + instanceId);
        }

        @Override
        public void onInterstitialAdClicked(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("inter_" + instanceId);
            if (baseAdapter instanceof IronsourceATInterstitialAdapter) {
                ((IronsourceATInterstitialAdapter) baseAdapter).onInterstitialAdClicked();
            }
        }
    };


    ISDemandOnlyRewardedVideoListener isDemandOnlyRewardedVideoListener = new ISDemandOnlyRewardedVideoListener() {
        @Override
        public void onRewardedVideoAdLoadSuccess(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mLoadResultAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdLoadSuccess();
            }
            removeLoadResultAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdLoadFailed(String instanceId, IronSourceError ironSourceError) {
            AnyThinkBaseAdapter baseAdapter = mLoadResultAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdLoadFailed(ironSourceError);
            }
            removeLoadResultAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdOpened(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdOpened();
            }
        }

        @Override
        public void onRewardedVideoAdClosed(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdClosed();
            }
        }

        @Override
        public void onRewardedVideoAdShowFailed(String instanceId, IronSourceError ironSourceError) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdShowFailed(ironSourceError);
            }
            removeAdapter("rv_" + instanceId);
        }

        @Override
        public void onRewardedVideoAdClicked(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdClicked();
            }
        }

        @Override
        public void onRewardedVideoAdRewarded(String instanceId) {
            AnyThinkBaseAdapter baseAdapter = mAdapterMap.get("rv_" + instanceId);
            if (baseAdapter instanceof IronsourceATRewardedVideoAdapter) {
                ((IronsourceATRewardedVideoAdapter) baseAdapter).onRewardedVideoAdRewarded();
            }
        }
    };


    private IronsourceATInitManager() {
        mAdapterMap = new ConcurrentHashMap<>();
        mLoadResultAdapterMap = new ConcurrentHashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static IronsourceATInitManager getInstance() {
        if (sInstance == null) {
            sInstance = new IronsourceATInitManager();
        }
        return sInstance;
    }

    @Override
    public void initSDK(Context context, Map<String, Object> serviceExtras) {
        if (!(context instanceof Activity)) {
            return;
        }
        initSDK(((Activity) context), serviceExtras, null);
    }

    public void initSDK(Activity activity, Map<String, Object> serviceExtras, final InitCallback initCallback) {

        final String appkey = (String) serviceExtras.get("app_key");
        if (TextUtils.isEmpty(appkey)) {
            return;
        }
        if (TextUtils.isEmpty(mAppKey) || !TextUtils.equals(mAppKey, appkey)) {

            if (serviceExtras.containsKey("gdpr_consent") && serviceExtras.containsKey("need_set_gdpr")) {
                //Whether to agree to collect data
                boolean gdp_consent = (boolean) serviceExtras.get("gdpr_consent");
                //Whether to set the GDPR of the network
                boolean need_set_gdpr = (boolean) serviceExtras.get("need_set_gdpr");

                if (need_set_gdpr) {
                    IronSource.setConsent(gdp_consent);
                }
            }

            logGDPRSetting(IronsourceATConst.NETWORK_FIRM_ID);

            IronSource.setISDemandOnlyInterstitialListener(isDemandOnlyInterstitialListener);
            IronSource.setISDemandOnlyRewardedVideoListener(isDemandOnlyRewardedVideoListener);

            IronSource.initISDemandOnly(activity, appkey, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO);
            mAppKey = appkey;

            postDelay(new Runnable() {
                @Override
                public void run() {
                    mAppKey = appkey;
                    if (initCallback != null) {
                        initCallback.onFinish();
                    }
                }
            }, 5000L); //The first initialization takes about 5 seconds
        } else {
            if (initCallback != null) {
                initCallback.onFinish();
            }
        }

    }

    public void loadInterstitial(final String instanceId, IronsourceATInterstitialAdapter interstitialAdapter) {
        putLoadResultAdapter("inter_" + instanceId, interstitialAdapter);
        IronSource.loadISDemandOnlyInterstitial(instanceId);

    }

    public void loadRewardedVideo(final String instanceId, IronsourceATRewardedVideoAdapter rewardedVideoAdapter) {
        putLoadResultAdapter("rv_" + instanceId, rewardedVideoAdapter);
        IronSource.loadISDemandOnlyRewardedVideo(instanceId);
    }

    protected synchronized void putLoadResultAdapter(String instanceId, AnyThinkBaseAdapter baseAdapter) {
        mLoadResultAdapterMap.put(instanceId, baseAdapter);
    }

    private synchronized void removeLoadResultAdapter(String instanceId) {
        mLoadResultAdapterMap.remove(instanceId);
    }

    protected synchronized void putAdapter(String instanceId, AnyThinkBaseAdapter baseAdapter) {
        mAdapterMap.put(instanceId, baseAdapter);
    }

    private synchronized void removeAdapter(String instanceId) {
        mAdapterMap.remove(instanceId);
    }


    protected void postDelay(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    interface InitCallback {
        void onFinish();
    }

    @Override
    public String getNetworkName() {
        return "Ironsource";
    }

    @Override
    public String getNetworkSDKClass() {
        return "com.ironsource.mediationsdk.IronSource";
    }


    @Override
    public List getActivityStatus() {
        ArrayList<String> list = new ArrayList<>();
        list.add("com.ironsource.sdk.controller.ControllerActivity");
        list.add("com.ironsource.sdk.controller.InterstitialActivity");
        list.add("com.ironsource.sdk.controller.OpenUrlActivity");
        return list;
    }
}
