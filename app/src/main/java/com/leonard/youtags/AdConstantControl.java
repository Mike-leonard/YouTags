package com.leonard.youtags;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import static com.leonard.youtags.Splash.adHourEditor;
import static com.leonard.youtags.Splash.appInstallEditor;
import static com.leonard.youtags.Splash.appInstallPref;
import static com.leonard.youtags.Splash.dailyAdClicked;
import static com.leonard.youtags.Splash.dailyAdClickedEditor;
import static com.leonard.youtags.Splash.dailyBanEditor;
import static com.leonard.youtags.Splash.dailyIntEditor;
import static com.leonard.youtags.Splash.dailyMaxBannerAds;
import static com.leonard.youtags.Splash.dailyMaxIntersialAds;
import static com.leonard.youtags.Splash.hourBanEditor;
import static com.leonard.youtags.Splash.hourIntEditor;
import static com.leonard.youtags.Splash.perHoursBannerAds;
import static com.leonard.youtags.Splash.perHoursIntersialAds;
import static com.leonard.youtags.Splash.prefAdHourControl;
import static com.leonard.youtags.Splash.prefDailyAdClicked;
import static com.leonard.youtags.Splash.prefDailyBanAds;
import static com.leonard.youtags.Splash.prefDailyIntAds;
import static com.leonard.youtags.Splash.prefHourBanAds;
import static com.leonard.youtags.Splash.prefHourIntAds;
import static com.leonard.youtags.Splash.isGoogleAdEnabled;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

public class AdConstantControl {
    private static String intersialUnityPlacement = "Interstitial_Android";
    private static String bannerUnityPlacement = "Banner_Android";
    private static boolean testMode = true;



    public static void adNetworkIntializeRequest (Activity act) {

        MobileAds.initialize(act, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        UnityAds.initialize(act, SettingsClass.unityAppID, unityAdsListener, testMode);
        // for banner ads
        UnityAds.initialize(act, SettingsClass.unityAppID,
                null, testMode , true);
    }

    // two adnetworks banner ad control
    public static void bannerAdControl (Activity act, int layout_id, View v) {

        if (isGoogleAdEnabled) {
            Long tsLong = System.currentTimeMillis()/1000;
            prefAdHourControl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            appInstallPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Long nextHour = prefAdHourControl.getLong("next-hour", 0);
            Long appInstallTime = appInstallPref.getLong("ins-time", 0);

            // next day finder
            if (appInstallTime > tsLong) {
                if (dailyMaxBannerAds < 200) {
                    if (nextHour > tsLong) {
                        if (perHoursBannerAds < 25 ) {
                            if(dailyAdClicked < 2) {
                                googleBannerView(act, layout_id, v);
                            } else {
                                unityBannerAdShower(act, layout_id, v);
                            }
                        } else { //
                            unityBannerAdShower(act, layout_id, v);
                        }
                    } else { // jokon current time boro hoi jaibo

                        Long nextOneHour = tsLong + 3600;
                        adHourEditor = prefAdHourControl.edit();
                        adHourEditor.putLong("next-hour", nextOneHour);
                        adHourEditor.commit();

                        // per hour ad reset
                        perHoursBannerAds = 0;
                        hourBanEditor = prefHourBanAds.edit();
                        hourBanEditor.putInt("hourly-ban-ads", perHoursBannerAds);
                        hourBanEditor.commit();
                        // update next hour timestamp
                        // reset perHourIntersials
                        Log.d("nextHour", "Else-Next-hour");
                    }
                } else { //
                    unityBannerAdShower(act, layout_id, v);
                }
            } else { // // next day update

                Long nextOneDay = tsLong + 86400;
                appInstallEditor = appInstallPref.edit();
                appInstallEditor.putLong("ins-time", nextOneDay);
                appInstallEditor.commit();

                // daily ad reset
                dailyMaxBannerAds = 0;
                dailyBanEditor = prefDailyBanAds.edit();
                dailyBanEditor.putInt("daily-ban-ads", dailyMaxBannerAds);
                dailyBanEditor.commit();

                // Daily ad Click reset
                dailyAdClicked = 0;
                dailyAdClickedEditor = prefDailyAdClicked.edit();
                dailyAdClickedEditor.putInt("daily-ad-click", dailyAdClicked);
                dailyAdClickedEditor.commit();
            }
        } else { // show unity ads

            unityBannerAdShower(act, layout_id, v);
        }
    }

    // two adnetworks Intersial ad control
    public static void adControl (Activity act) {
        if (isGoogleAdEnabled) {

            Long tsLong = System.currentTimeMillis()/1000;
            prefAdHourControl = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            appInstallPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Long nextHour = prefAdHourControl.getLong("next-hour", 0);
            Long appInstallTime = appInstallPref.getLong("ins-time", 0);

            // next day finder
            if (appInstallTime > tsLong) {
                if (dailyMaxIntersialAds < 50) {
                    // nextHour finder
                    if (nextHour > tsLong) {
                        if (perHoursIntersialAds < 10 ) {
                            // daily ad clicked
                            if(dailyAdClicked < 2) {
                                googleIntersialAds(act);
                            } else { // ad Clicked
                                unityAdDisplay(act);
                            }
                        } else { // 10 ta taki beshi oile
                            unityAdDisplay(act);
                        }
                    } else { // jokon current time boro hoi jaibo
                        // per hour ad reset
                        perHoursIntersialAds = 0;
                        hourIntEditor = prefHourIntAds.edit();
                        hourIntEditor.putInt("hourly-int-ads", perHoursIntersialAds);
                        hourIntEditor.commit();
                        // update next hour timestamp
                        // reset perHourIntersials
                    }

                } else { // if 50+ start showing unity
                    unityAdDisplay(act);
                }
            } else { // next day update
                // daily ad reset
                dailyMaxIntersialAds = 0;
                dailyIntEditor = prefDailyIntAds.edit();
                dailyIntEditor.putInt("daily-int-ads", dailyMaxIntersialAds);
                dailyIntEditor.commit();
            }

        } else {   // google ad  disabled statement
            unityAdDisplay(act);
        }
    }

    // Google normal banner view
    private static void googleBannerView (final Activity act, final int layout_id, final View v) {

        LinearLayout adContainer = v.findViewById(layout_id);

        com.google.android.gms.ads.AdView adView = new
                com.google.android.gms.ads.AdView(getApplicationContext());
        adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId(getApplicationContext().getString(R.string.google_banner_id));
        adView.setAdUnitId(SettingsClass.admBanner);

        // Initiate a generic request to load it with an ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Place the ad view.
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        adContainer.addView(adView, params);

        adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                unityBannerAdShower(act, layout_id, v);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                perHoursBannerAds++;
                dailyMaxBannerAds++;

                hourBanEditor = prefHourBanAds.edit();
                dailyBanEditor = prefDailyBanAds.edit();
                hourBanEditor.putInt("hourly-ban-ads", perHoursBannerAds);
                dailyBanEditor.putInt("daily-ban-ads", dailyMaxBannerAds);
                hourBanEditor.commit();
                dailyBanEditor.commit();

            }
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                dailyAdClicked++;
                dailyAdClickedEditor = prefDailyAdClicked.edit();
                dailyAdClickedEditor.putInt("daily-ad-click", dailyAdClicked);
                dailyAdClickedEditor.commit();
            }
            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }


    // Google Intersial ads
    private static void googleIntersialAds (final Activity act) {
        AdRequest adRequest = new AdRequest.Builder().build();

        final InterstitialAd[] mInterGoogle = {null};
        //mInterGoogle[0].load(act, getApplicationContext().getString(R.string.gog_Inter_id),
        mInterGoogle[0].load(act, SettingsClass.adInterstitial,
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        if (interstitialAd != null) {
                            mInterGoogle[0] = interstitialAd;
                            mInterGoogle[0].show(act);
                        }
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                perHoursIntersialAds++;
                                dailyMaxIntersialAds++;
                                hourIntEditor = prefHourIntAds.edit();
                                dailyIntEditor = prefDailyIntAds.edit();
                                hourIntEditor.putInt("hourly-int-ads", perHoursIntersialAds);
                                dailyIntEditor.putInt("daily-int-ads", dailyMaxIntersialAds);
                                hourIntEditor.commit();
                                dailyIntEditor.commit();
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                dailyAdClicked++;
                                dailyAdClickedEditor = prefDailyAdClicked.edit();
                                dailyAdClickedEditor.putInt("daily-ad-click", dailyAdClicked);
                                dailyAdClickedEditor.commit();
                            }
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                mInterGoogle[0] = null;
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        mInterGoogle[0] = null;
                        unityAdDisplay(act);
                    }

                });
    }


    // Unity Banner Ads
    private static void unityBannerAdShower (Activity act, int layout_id, View v) {

        LinearLayout topBannerView = v.findViewById(layout_id);
        com.unity3d.services.banners.BannerView topBanner =
                new com.unity3d.services.banners.BannerView(act, bannerUnityPlacement,
                        new UnityBannerSize(320, 50));
        topBanner.load();
        // Place the ad view.
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        topBannerView.addView(topBanner, params);

        topBanner.setListener(new com.unity3d.services.banners.BannerView.IListener() {
            @Override
            public void onBannerLoaded(com.unity3d.services.banners.BannerView bannerView) {
            }

            @Override
            public void onBannerClick(com.unity3d.services.banners.BannerView bannerView) {
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                Log.e("lad", bannerErrorInfo.errorMessage);
            }

            @Override
            public void onBannerLeftApplication(com.unity3d.services.banners.BannerView bannerView) {

            }
        });
    }

    // Unity Intersial ads
    private static void unityIntersialAds () {
        UnityAds.load(intersialUnityPlacement);
    }
    private static void unityAdDisplay (Activity act) {
        if (UnityAds.isReady(intersialUnityPlacement)){
            Log.e("adsUNDius", "rdy");
            UnityAds.show(act, intersialUnityPlacement);
        }
    }
    // unity intersila listener
    private static IUnityAdsListener unityAdsListener = new IUnityAdsListener() {
        @Override
        public void onUnityAdsReady(String s) {
            unityIntersialAds();
        }

        @Override
        public void onUnityAdsStart(String s) {
        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            Log.e("adsUni", unityAdsError.toString());
        }
    };
}

/*  <LinearLayout
        android:id="@+id/normal_ad_include"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_below="@+id/tv_ans_bmr"
        android:layout_gravity="center" />
*/