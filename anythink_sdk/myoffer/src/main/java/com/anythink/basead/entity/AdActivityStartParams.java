/*
 * Copyright © 2018-2020 TopOn. All rights reserved.
 * https://www.toponad.com
 * Licensed under the TopOn SDK License Agreement
 * https://github.com/toponteam/TopOn-Android-SDK/blob/master/LICENSE
 */

package com.anythink.basead.entity;

import com.anythink.core.common.entity.BaseAdContent;
import com.anythink.core.common.entity.BaseAdSetting;

/**
 * Start AdActivity Params
 */
public class AdActivityStartParams {
    public String requestId;
    public int format;
    public String scenario;
    public BaseAdContent baseAdContent;
    public String placementId;
    public BaseAdSetting baseAdSetting;
    public String eventId;
    public int orientation;
}
