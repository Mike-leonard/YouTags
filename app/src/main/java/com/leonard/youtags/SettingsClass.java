package com.leonard.youtags;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by asus on 06/04/2018.
 */

public class SettingsClass {

    public static Boolean supportRTL = false;
    public static String contactMail = "mortuza.7@gmail.com";

   /* public static String publisherID ="ca-app-pub-8329902046519331~5609674196";
    public static String admBanner   = "ca-app-pub-8329902046519331/6731184175";
    public static String Interstitial = "ca-app-pub-8329902046519331/2408795782";*/

    public static String unityAppID = "4602307";
    public static String publisherID ="ca-app-pub-3940256099942544~3347511713";
    public static String admBanner   = "ca-app-pub-3940256099942544/6300978111";
    public static String adInterstitial = "ca-app-pub-3940256099942544/1033173712";
    public static String adNative = "ca-app-pub-3940256099942544/2247696110";
    public static String privacy_policy_url = "https://www.app-privacy-policy.com/live.php?token=BUoefVplu4jQ4JI5R7W5oiGDy6PrxPQQ";
    public static int nbShowInterstitial = 4;
    public static int mCount = 0;



































    public static void admobBannerCall(Activity activity , final LinearLayout linerlayout){

        AdView adView = new AdView(activity);
        adView.setAdUnitId(SettingsClass.admBanner);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(ConsentSDK.getAdRequest(activity));
        linerlayout.setVisibility(View.GONE);
        linerlayout.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                linerlayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
