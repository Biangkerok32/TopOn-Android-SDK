/*
 * Copyright © 2018-2020 TopOn. All rights reserved.
 * https://www.toponad.com
 * Licensed under the TopOn SDK License Agreement
 * https://github.com/toponteam/TopOn-Android-SDK/blob/master/LICENSE
 */

package com.anythink.network.gdt;

import com.qq.e.comm.managers.status.SDKStatus;

/**
 * Created by zhou on 2018/4/3.
 */

public class GDTATConst {
    @Deprecated
    /***
     * ad type for gdt
     * 1,Self Rendering Image + text ads only
     * 2,Self Rendering Picture | video + text,
     * 3,Native Express,Picture | video + text
     */
    public static final String ADTYPE = "gdtadtype";
    @Deprecated
    public static final String AD_WIDTH = "gdtad_width";
    public static final String AD_HEIGHT = "gdtad_height";

    public static final int NETWORK_FIRM_ID = 8;

    public static String getNetworkVersion() {
        try {
            return SDKStatus.getSDKVersion();
        } catch (Throwable e) {

        }
        return "";
    }
}
