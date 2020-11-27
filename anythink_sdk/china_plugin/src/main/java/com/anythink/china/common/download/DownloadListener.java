/*
 * Copyright © 2018-2020 TopOn. All rights reserved.
 * https://www.toponad.com
 * Licensed under the TopOn SDK License Agreement
 * https://github.com/toponteam/TopOn-Android-SDK/blob/master/LICENSE
 */

package com.anythink.china.common.download;

public interface DownloadListener {

    void onSuccess(String url);

    void onProgress(int progress);

    void onFailed(String url);
}
