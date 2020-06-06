package com.toutiaoad;

import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import javax.annotation.Nonnull;

public class TTAdFullScreenVideoModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "TTAdFullScreenVideo";
    public static final String EVENT_AD_SHOWED = "fullScreenVideoAdShowed";
    public static final String EVENT_AD_VIDEO_BAR_CLICK = "fullScreenVideoAdClick";
    public static final String EVENT_AD_VIDEO_CLOSE = "fullScreenVideoAdClose";
    public static final String EVENT_AD_VIDEO_COMPLETE = "fullScreenVideoComplete";
    public static final String EVENT_AD_SKIPPED_VIDEO = "fullScreenVideoSkipped";
    public static final String EVENT_AD_FAILED_TO_LOAD = "fullScreenVideoAdFailedToLoad";

    private TTFullScreenVideoAd mttFullVideoAd;

    public TTAdFullScreenVideoModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void showFullScreenVideoAd(String codeId) {
        try {
            initFullScreenVideoAd(codeId);
        } catch (Exception e) {
            Log.e("...ScreenVideoModule", e.getMessage());
        }


    }

    private void showAd() {
        getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mttFullVideoAd != null) {
                    mttFullVideoAd.showFullScreenVideoAd(getCurrentActivity(), TTAdConstant.RitScenes.GAME_GIFT_BONUS, null);
                }
            }
        });
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    //    @ReactMethod
    private void initFullScreenVideoAd(String codeId) {
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary((MainActivity) getCurrentActivity());
        //step3:创建TTAdNative对象,用于调用广告请求接口
        TTAdNative mTTAdNative = ttAdManager.createAdNative((MainActivity) getCurrentActivity());
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(getCurrentActivity(), message + "message");
                WritableMap error = Arguments.createMap();
                error.putInt("code", code);
                error.putString("message", message);
                sendEvent(EVENT_AD_FAILED_TO_LOAD, error);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {

                    @Override
                    public void onAdShow() {
//                        TToast.show(getCurrentActivity(), "FullVideoAd show1");
                        sendEvent(EVENT_AD_SHOWED, null);
                    }

                    @Override
                    public void onAdVideoBarClick() {
//                        TToast.show(getCurrentActivity(), "FullVideoAd bar click");
                        sendEvent(EVENT_AD_VIDEO_BAR_CLICK, null);

                    }

                    @Override
                    public void onAdClose() {
//                        TToast.show(getCurrentActivity(), "FullVideoAd close");
                        sendEvent(EVENT_AD_VIDEO_CLOSE, null);

                    }

                    @Override
                    public void onVideoComplete() {
//                        TToast.show(getCurrentActivity(), "FullVideoAd complete");
                        sendEvent(EVENT_AD_VIDEO_COMPLETE, null);

                    }

                    @Override
                    public void onSkippedVideo() {
//                        TToast.show(getCurrentActivity(), "FullVideoAd skipped");
                        sendEvent(EVENT_AD_SKIPPED_VIDEO, null);
                    }

                });
                showAd();

            }

            @Override
            public void onFullScreenVideoCached() {
//                TToast.show(getCurrentActivity(), "FullVideoAd video cached");
            }
        });


    }

}
