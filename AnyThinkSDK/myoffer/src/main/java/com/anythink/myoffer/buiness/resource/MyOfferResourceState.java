package com.anythink.myoffer.buiness.resource;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.anythink.core.common.base.Const;
import com.anythink.core.common.base.SDKContext;
import com.anythink.core.common.utils.CommonMD5;
import com.anythink.myoffer.entity.MyOfferAd;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Url resource state
 */
public class MyOfferResourceState {

    private static final String MYOFFER_DIR = Const.RESOURCE_HEAD + "_myoffer_resouce";


    private static Map<String, Integer> sStateMap = new HashMap<>();
    public static final int NORMAL = 0;
    public static final int LOADING = 1;

    public static boolean isLoading(String url) {

        Integer state = sStateMap.get(url);
        return MyOfferResourceState.LOADING == (state != null? state: MyOfferResourceState.NORMAL);
    }

    public static void setState(String url, int state) {
        sStateMap.put(url, state);
    }


    /**
     * Check if the resource exists
     */
    public static boolean isExist(String url) {
        String resourcePath = getResourcePath(url);
        return !TextUtils.isEmpty(url) && !TextUtils.isEmpty(resourcePath) && new File(resourcePath + ".0").exists();
    }
    /**
     * Check if the resource exists
     */
    public static boolean isExist(MyOfferAd myOfferAd) {
        if(myOfferAd == null) {
            return false;
        }
        List<String> urlList = myOfferAd.getUrlList();
        int size = urlList.size();
        if(size > 0) {
            for (int i = 0; i < size; i++) {
                if(!isExist(urlList.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /**
     * Resource save path
     */
    public static String getSaveDirectory() {
        if(getAppContext() == null) {
            return null;
        }

        File baseFile = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                File fileDir = getAppContext().getExternalFilesDir(null);

                if (fileDir != null) {
                    baseFile = getRandomFileDir(fileDir);

                    if(baseFile != null) {
                        baseFile = new File(baseFile, MYOFFER_DIR);
                    }
                }
            } catch (Throwable t) {
            }
        }

        if(baseFile == null && hasPermission()) {
            String rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator
                    + MYOFFER_DIR + File.separator
                    + getAppContext().getPackageName();
            baseFile = getRandomFileDir(new File(rootPath));
            if (!hasEnoughSpace()) {
                baseFile = null;
            }
        }
        if (baseFile == null) {
            String saveDirectory = getAppContext().getFilesDir().getAbsoluteFile() + File.separator + MYOFFER_DIR;
            baseFile = new File(saveDirectory);
        }

        return baseFile.getAbsolutePath();
    }

    private static File getRandomFileDir(File fileDir) {
        File baseFile = null;
        File temp = new File(fileDir,  UUID.randomUUID() + "");
        if (temp.exists()) {
            temp.delete();
        }
        if (temp.mkdirs()) {
            temp.delete();
            baseFile = fileDir.getAbsoluteFile();
        }
        return baseFile;
    }

    private static boolean hasEnoughSpace() {
        return getAvailableExternalMemorySize() > 30 * 1024 * 1024;
    }

    /**
     * @return byte
     */
    @SuppressWarnings("deprecation")
    private static long getAvailableExternalMemorySize() {
        if (hasSDCard()) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            } catch (Error e) {
            }
        }
        return 0;
    }

    /**
     * Get resource name by url
     */
    public static String getResourceName(String url) {
        return CommonMD5.getMD5(url);
    }
    /**
     * Get resource
     */
    public static String getResourcePath(String url) {
        String saveDirectory = getSaveDirectory();
        if(saveDirectory != null) {
            return saveDirectory + File.separator + getResourceName(url);
        } else {
            return null;
        }
    }

    private static Context getAppContext() {
        try {
            return SDKContext.getInstance().getContext().getApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static boolean hasPermission() {
        boolean mHasPermission = false;
        Context appContext = getAppContext();
        if(appContext == null) {
            return mHasPermission;
        }
        try {
            PackageManager pm = appContext.getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    appContext.getPackageName());
            mHasPermission = hasPerm == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHasPermission;
    }

}
