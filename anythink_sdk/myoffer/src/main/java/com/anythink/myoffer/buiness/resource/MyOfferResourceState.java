package com.anythink.myoffer.buiness.resource;


import com.anythink.core.common.base.SDKContext;
import com.anythink.core.common.entity.MyOfferAd;
import com.anythink.core.common.entity.MyOfferSetting;
import com.anythink.core.common.res.ResourceDiskCacheManager;
import com.anythink.core.common.res.ResourceEntry;
import com.anythink.core.common.utils.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Url resource state
 */
public class MyOfferResourceState {


    private static Map<String, Integer> sStateMap = new HashMap<>();
    public static final int NORMAL = 0;
    public static final int LOADING = 1;

    public static boolean isLoading(String url) {

        Integer state = sStateMap.get(url);
        return MyOfferResourceState.LOADING == (state != null ? state : MyOfferResourceState.NORMAL);
    }

    public static void setState(String url, int state) {
        sStateMap.put(url, state);
    }


    /**
     * Check if the resource exists
     */
    public static boolean isExist(String url) {
        String resourceName = FileUtil.hashKeyForDisk(url);
        return ResourceDiskCacheManager.getInstance(SDKContext.getInstance().getContext()).isExistFile(ResourceEntry.INTERNAL_CACHE_TYPE, resourceName);
    }

    /**
     * Check if the resource exists
     */
    public static boolean isExist(MyOfferAd myOfferAd, MyOfferSetting myOfferSetting) {
        if (myOfferAd == null) {
            return false;
        }
        List<String> urlList = myOfferAd.getUrlList(myOfferSetting);
        int size = urlList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (!isExist(urlList.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

}
