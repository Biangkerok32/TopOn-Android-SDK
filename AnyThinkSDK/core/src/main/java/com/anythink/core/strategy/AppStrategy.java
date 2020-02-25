package com.anythink.core.strategy;

import android.text.TextUtils;

import com.anythink.core.api.ATSDK;
import com.anythink.core.common.base.Const;
import com.anythink.core.common.base.SDKContext;
import com.anythink.core.common.base.UploadDataLevelManager;
import com.anythink.core.common.entity.NetworkInfoBean;
import com.anythink.core.common.utils.CommonLogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @author zhou
 * 2017/12/29.
 */
public class AppStrategy {
    public static final String TAG = AppStrategy.class.getSimpleName();

    protected boolean isLocalStrategy;
    /***
     * Avail time
     */
    private long strategyOutTime;

    /***
     * req_ver
     */
    private String req_ver;
    /***
     * Update time
     */
    private long updateTime;

    /**
     * GDPR Level，0:PERSONALIZED ，1:NONPERSONALIZED
     */
    private int gdpr_sdcs;
    /**
     * GDPR switch
     */
    private int gdpr_so;
    /**
     * GDPR url
     */
    private String gdpr_nu;
    /**
     * Country code of EU
     */
    private String gdpr_a;
    /**
     * EU-Traffic, 1:true，0:false
     */
    private int gdpr_ia;

    /**
     * Splash Timeout
     *
     * @return
     */
    private long placementTimeOut;

    private String upId;

    /**
     * Logger Info
     */
    private String logger;

    /**
     * Tracking url
     **/
    private String tkAddress;
    /**
     * Tracking num
     **/
    private int tkMaxAmount;
    /**
     * Tracking interval
     **/
    private long tkInterval;
    /**
     * No Tracking Keys
     **/
    private int[] tkNoTrackingType;
    /**
     * Agent url
     **/
    private String daAddress;
    /**
     * Agent num
     **/
    private int daMaxAmount;
    /**
     * Agent interval
     **/
    private long daInterval;
    /**
     * No Agent Keys
     **/
    private String daNotKeys;
    /**
     * Instant Agent Keys
     **/
    private String daRealTimeKeys;

    /**
     * Psid avail time
     *
     * @return
     */
    private long psidTimeOut;

    /**
     * Myoffer cache size
     *
     * @return
     */
    private long offerCacheSize;

    /**
     * Use Default Mediation GDPR switch
     */
    private int useNetworkDefaultGDPR;

    /**
     * Notice Map
     */
    private Map noticeMap;

    /**
     * Pre-init Mediation's info
     */
    private String preinitStr;

    private String t_c;

    private ConcurrentHashMap<String, NetworkInfoBean> tkInfoMap;

    public boolean isLocalStrategy() {
        return isLocalStrategy;
    }

    public String getTC() {
        return t_c;
    }

    public void setTC(String tc) {
        this.t_c = tc;
    }

    public ConcurrentHashMap<String, NetworkInfoBean> getTkInfoMap() {
        return tkInfoMap;
    }

    public void setTkInfoMap(ConcurrentHashMap<String, NetworkInfoBean> tkInfoMap) {
        this.tkInfoMap = tkInfoMap;
    }


    public int getUseNetworkDefaultGDPR() {
        return useNetworkDefaultGDPR;
    }

    public void setUseNetworkDefaultGDPR(int useNetworkDefaultGDPR) {
        this.useNetworkDefaultGDPR = useNetworkDefaultGDPR;
    }

    public long getOfferCacheSize() {
        return offerCacheSize;
    }

    public void setOfferCacheSize(long offerCacheSize) {
        this.offerCacheSize = offerCacheSize;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public Map<String, String> getNoticeMap() {
        return noticeMap;
    }

    public void setNoticeMap(Map noticeMap) {
        this.noticeMap = noticeMap;
    }

    public String getPreinitStr() {
        return preinitStr;
    }

    public void setPreinitStr(String preinitStr) {
        this.preinitStr = preinitStr;
    }

    static class ResponseKey {
        private static String strategyOutTime_key = "scet";
        private static String req_ver_key = "req_ver";

        private static String gdpr_sdcs = "gdpr_sdcs";
        private static String gdpr_so = "gdpr_so";
        private static String gdpr_nu = "gdpr_nu";
        private static String gdpr_a = "gdpr_a";
        private static String gdpr_ia = "gdpr_ia";
        private static String pl_n = "pl_n";

        private static String upid = "upid";

        private static String logger = "logger";
        private static String tk_address = "tk_address";
        private static String tk_max_amount = "tk_max_amount";
        private static String tk_interval = "tk_interval";
        private static String tk_no_t = "tk_no_t";
        private static String da_address = "da_address";
        private static String da_max_amount = "da_max_amount";
        private static String da_interval = "da_interval";
        private static String da_not_keys = "da_not_keys";
        private static String da_rt_keys = "da_rt_keys";

        private static String new_psid_time = "n_psid_tm";

        private static String myoffer_cache_size = "c_a";

        private static String tk_firm = "tk_firm";

        private static String n_l = "n_l";

        private static String preinit = "preinit";

        private static String nw_eu_def = "nw_eu_def";

        private static String t_c = "t_c";


    }

    public long getPsidTimeOut() {
        return psidTimeOut;
    }

    public void setPsidTimeOut(long psidTimeOut) {
        this.psidTimeOut = psidTimeOut;
    }

//    public String getUpId() {
//        return upId;
//    }

//    public void setUpId(String upId) {
//        this.upId = upId;
//    }

    public long getStrategyOutTime() {
        return strategyOutTime;
    }

    public void setStrategyOutTime(long strategyOutTime) {
        this.strategyOutTime = strategyOutTime;
    }


    public String getReq_ver() {
        return req_ver;
    }

    public void setReq_ver(String req_ver) {
        this.req_ver = req_ver;
    }


    public int getGdprSdcs() {
        return gdpr_sdcs;
    }

    public void setGdprSdcs(int gdpr_sdcs) {
        this.gdpr_sdcs = gdpr_sdcs;
    }

    public int getGdprSo() {
        return gdpr_so;
    }

    public void setGdprSo(int gdpr_so) {
        this.gdpr_so = gdpr_so;
    }

    public String getGdprNu() {
        return gdpr_nu;
    }

    public void setGdprNu(String gdpr_nu) {
        this.gdpr_nu = gdpr_nu;
    }

    public String getGdprA() {
        return gdpr_a;
    }

    public void setGdprA(String gdpr_a) {
        this.gdpr_a = gdpr_a;
    }

    public int getGdprIa() {
        return gdpr_ia;
    }

    public void setGdprIa(int gdpr_ia) {
        this.gdpr_ia = gdpr_ia;
    }


    public long getPlacementTimeOut() {
        return placementTimeOut;
    }

    public void setPlacementTimeOut(long splashTimeOut) {
        this.placementTimeOut = splashTimeOut;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLogger() {
        return this.logger;
    }

    public String getTkAddress() {
        return tkAddress;
    }

    public void setTkAddress(String tkAddress) {
        this.tkAddress = tkAddress;
    }

    public int getTkMaxAmount() {
        return tkMaxAmount;
    }

    public void setTkMaxAmount(int tkMaxAmount) {
        this.tkMaxAmount = tkMaxAmount;
    }

    public long getTkInterval() {
        return tkInterval;
    }

    public void setTkInterval(long tkInterval) {
        this.tkInterval = tkInterval;
    }

    public int[] getTkNoTrackingType() {
        return tkNoTrackingType;
    }

    public void setTkNoTrackingType(int[] tkNoTrackingType) {
        this.tkNoTrackingType = tkNoTrackingType;
    }

    public String getDaAddress() {
        return daAddress;
    }

    public void setDaAddress(String daAddress) {
        this.daAddress = daAddress;
    }

    public int getDaMaxAmount() {
        return daMaxAmount;
    }

    public void setDaMaxAmount(int daMaxAmount) {
        this.daMaxAmount = daMaxAmount;
    }

    public long getDaInterval() {
        return daInterval;
    }

    public void setDaInterval(long daInterval) {
        this.daInterval = daInterval;
    }

    public String getDaNotKeys() {
        return daNotKeys;
    }

    public void setDaNotKeys(String daNotKeys) {
        this.daNotKeys = daNotKeys;
    }

    public String getDaRealTimeKeys() {
        return daRealTimeKeys;
    }

    public void setDaRealTimeKeys(String daRealTimeKeys) {
        this.daRealTimeKeys = daRealTimeKeys;
    }

    /***
     * Parse AppSetting' Json String
     * @param jsonStr
     * @return
     */
    public static AppStrategy parseStrategy(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            return null;
        }
        CommonLogUtil.i(TAG, jsonStr);
        AppStrategy strategy = new AppStrategy();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.isNull(ResponseKey.req_ver_key)) {
                strategy.setReq_ver("unkown");
            } else {
                strategy.setReq_ver(jsonObject.optString(ResponseKey.req_ver_key));
            }

            if (jsonObject.isNull(ResponseKey.strategyOutTime_key)) {
                strategy.setStrategyOutTime(Const.DEFAULT_SDK_KEY.APPSTRATEGY_DEFAULT_OUTTIME);
            } else {
                strategy.setStrategyOutTime(jsonObject.optLong(ResponseKey.strategyOutTime_key));
            }

            if (jsonObject.isNull(ResponseKey.gdpr_sdcs)) {
                strategy.setGdprSdcs(0);
            } else {
                strategy.setGdprSdcs(jsonObject.optInt(ResponseKey.gdpr_sdcs));

            }

            if (jsonObject.isNull(ResponseKey.gdpr_so)) {
                strategy.setGdprSo(0);
            } else {
                strategy.setGdprSo(jsonObject.optInt(ResponseKey.gdpr_so));
            }

            if (jsonObject.isNull(ResponseKey.gdpr_nu)) {
                strategy.setGdprNu("");
            } else {
                strategy.setGdprNu(jsonObject.optString(ResponseKey.gdpr_nu));

            }

            if (jsonObject.isNull(ResponseKey.gdpr_a)) {
                strategy.setGdprA("[\"AT\",\"BE\",\"BG\",\"HR\",\"CY\",\"CZ\",\"DK\",\"EE\",\"FI\",\"FR\",\"DE\",\"GR\",\"HU\",\"IS\",\"IE\",\"IT\",\"LV\",\"LI\",\"LT\",\"LU\",\"MT\",\"NL\",\"NO\",\"PL\",\"PT\",\"RO\",\"SK\",\"SI\",\"ES\",\"SE\",\"GB\",\"UK\"]");
            } else {
                strategy.setGdprA(jsonObject.optString(ResponseKey.gdpr_a));
            }

            if (jsonObject.isNull(ResponseKey.gdpr_ia)) {
                strategy.setGdprIa(0);
            } else {
                strategy.setGdprIa(jsonObject.optInt(ResponseKey.gdpr_ia));

            }


            if (jsonObject.isNull(ResponseKey.pl_n)) {
                strategy.setPlacementTimeOut(5000L);
            } else {
                strategy.setPlacementTimeOut(jsonObject.optLong(ResponseKey.pl_n));
            }


            if (!jsonObject.isNull(ResponseKey.logger)) {
                JSONObject loggerObject = jsonObject.optJSONObject(ResponseKey.logger);
                strategy.setLogger(loggerObject.toString());

                strategy.setTkAddress(loggerObject.optString(ResponseKey.tk_address));
                strategy.setTkMaxAmount(loggerObject.optInt(ResponseKey.tk_max_amount));
                strategy.setTkInterval(loggerObject.optLong(ResponseKey.tk_interval));

                //解析 不上报类型
                String tk_no_t_json = loggerObject.optString(ResponseKey.tk_no_t);
                int[] noTrackingTypeArrays = null;
                if (!TextUtils.isEmpty(tk_no_t_json)) {
                    JSONArray tk_no_t_array = new JSONArray(tk_no_t_json);

                    if (tk_no_t_array != null) {
                        int length = tk_no_t_array.length();
                        //有 不上报的类型 存在
                        if (length > 0) {
                            noTrackingTypeArrays = new int[length];
                            for (int i = 0; i < length; i++) {
                                noTrackingTypeArrays[i] = tk_no_t_array.getInt(i);
                            }
                        }
                    }
                }
                strategy.setTkNoTrackingType(noTrackingTypeArrays);

                strategy.setDaAddress(loggerObject.optString(ResponseKey.da_address));
                strategy.setDaMaxAmount(loggerObject.optInt(ResponseKey.da_max_amount));
                strategy.setDaInterval(loggerObject.optLong(ResponseKey.da_interval));
                strategy.setDaNotKeys(loggerObject.optString(ResponseKey.da_not_keys));
                strategy.setDaRealTimeKeys(loggerObject.optString(ResponseKey.da_rt_keys));

                ConcurrentHashMap tempMap = new ConcurrentHashMap<>();
                try {
                    if (!loggerObject.isNull(ResponseKey.tk_firm)) {
                        JSONObject tkFirmObject = new JSONObject(loggerObject.optString(ResponseKey.tk_firm));
                        Iterator<String> keyIterator = tkFirmObject.keys();
                        while (keyIterator.hasNext()) {
                            String key = keyIterator.next();
                            NetworkInfoBean networkInfoBean = new NetworkInfoBean();
                            JSONObject itemObject = tkFirmObject.optJSONObject(key);
                            networkInfoBean.tkLoadSwitch = itemObject.optInt("tk_fi_re_sw");
                            networkInfoBean.tkImpressionSwitch = itemObject.optInt("tk_im_sw");
                            networkInfoBean.tkShowSwtich = itemObject.optInt("tk_sh_sw");
                            networkInfoBean.tkClickSwitch = itemObject.optInt("tk_ck_sw");
                            networkInfoBean.detailInfo = itemObject.optString("pg_m_li");
                            tempMap.put(key, networkInfoBean);
                        }
                    }
                } catch (Exception e) {

                }
                strategy.setTkInfoMap(tempMap);

            }

            if (!jsonObject.isNull(ResponseKey.new_psid_time)) {
                strategy.setPsidTimeOut(jsonObject.optLong(ResponseKey.new_psid_time));
            }

            if (!jsonObject.isNull(ResponseKey.myoffer_cache_size)) {
                strategy.setOfferCacheSize(jsonObject.optLong(ResponseKey.myoffer_cache_size));
            }

            /**
             * v5.4.0
             */
            if (!jsonObject.isNull(ResponseKey.n_l)) {
                String n_l_json = jsonObject.optString(ResponseKey.n_l);
                JSONObject n_l_json_object = new JSONObject(n_l_json);
                Iterator<String> keyIterator = n_l_json_object.keys();
                String key;
                Map<String, String> noticeMap = new HashMap<>();
                while (keyIterator.hasNext()) {
                    key = keyIterator.next();
                    noticeMap.put(key, n_l_json_object.optString(key));
                }
                strategy.setNoticeMap(noticeMap);
            }

            if (!jsonObject.isNull(ResponseKey.t_c)) {
                String tc_json = jsonObject.optString(ResponseKey.t_c);
                strategy.setTC(tc_json);
            }


            /** v5.4.1 */
            if (!jsonObject.isNull(ResponseKey.preinit)) {
                strategy.setPreinitStr(jsonObject.optString(ResponseKey.preinit));
            }

            if (!jsonObject.isNull(ResponseKey.nw_eu_def)) {
                strategy.setUseNetworkDefaultGDPR(jsonObject.optInt(ResponseKey.nw_eu_def));
            }
        } catch (Exception e) {
            if (Const.DEBUG) {
                e.printStackTrace();
            }

        }

        return strategy;

    }


    /**
     * GDPR Setting
     *
     * @param serviceExtras
     */
    public static void fillGdprData(Map<String, Object> serviceExtras) {
        AppStrategy appStrategy = AppStrategyManager.getInstance(SDKContext.getInstance().getContext()).getAppStrategyByAppId(SDKContext.getInstance().getAppId());
        boolean isEU = appStrategy != null && appStrategy.getGdprIa() == 1;
        boolean isUseNetworkDefaultGDPR = appStrategy != null && appStrategy.getUseNetworkDefaultGDPR() == 1;
        UploadDataLevelManager uploadDataLevelManager = UploadDataLevelManager.getInstance(SDKContext.getInstance().getContext());
        serviceExtras.put("gdpr_consent", uploadDataLevelManager.isNetworkGDPRConsent());
        serviceExtras.put("is_eu", isEU);

        boolean needSetNetworkGDPR = false;

        if (appStrategy.isLocalStrategy()) {
            /**level=UNKNOWN (Use Mediation's default GDPR setting）**/
            needSetNetworkGDPR = uploadDataLevelManager.getUploadDataLevel() != ATSDK.UNKNOWN;
        } else {
            if (uploadDataLevelManager.getUploadDataLevel() == ATSDK.UNKNOWN) { /**UNKNOWNN**/
                if (appStrategy.getGdprIa() == 0) { /**Not EU-Traffic**/
                    needSetNetworkGDPR = false;
                } else { /**EU-Traffic**/
                    if (isUseNetworkDefaultGDPR) {
                        /**Set by Service Setting**/
                        needSetNetworkGDPR = false;
                    } else {
                        needSetNetworkGDPR = true;
                    }
                }
            } else { /**Not UNKNOW**/
                needSetNetworkGDPR = true;
            }
        }

        //Status of Setting Mediation GDPR config
        serviceExtras.put("need_set_gdpr", needSetNetworkGDPR);
    }

}
