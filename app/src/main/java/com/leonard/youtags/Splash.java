package com.leonard.youtags;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class Splash extends AppCompatActivity {

    public static boolean isGoogleAdEnabled = false;
    public static Activity context;
    Button start;
    ConsentSDK consentSDK;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;

    public static SharedPreferences prefAdHourControl, appInstallPref;
    public static SharedPreferences.Editor adHourEditor, appInstallEditor;

    public static SharedPreferences prefHourBanAds, prefHourIntAds,
            prefDailyBanAds, prefDailyIntAds,
            prefDailyAdClicked;
    public static SharedPreferences.Editor hourBanEditor, hourIntEditor,
            dailyBanEditor, dailyIntEditor,
            dailyAdClickedEditor;
    public static int perHoursBannerAds, dailyMaxBannerAds;
    public static int perHoursIntersialAds, dailyMaxIntersialAds, dailyAdClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_splash);

        //Chceking it's first time or not
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        // Ad status to 1 hour
        prefAdHourControl = getSharedPreferences("next-hour", MODE_PRIVATE);
        // Hourly Banner ads showing
        prefHourBanAds = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        perHoursBannerAds = prefHourBanAds.getInt("hourly-ban-ads", 0);
        // Hourly Intersial ads showing
        prefHourIntAds = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        perHoursIntersialAds = prefHourIntAds.getInt("hourly-int-ads", 0);

        // Daily banner ads
        prefDailyBanAds = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        dailyMaxBannerAds = prefDailyBanAds.getInt("daily-ban-ads", 0);
        // Daily intersial ads
        prefDailyIntAds = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        dailyMaxIntersialAds = prefDailyIntAds.getInt("daily-int-ads", 0);
        // Daily Ad Clicked
        //prefDailyAdClicked = getSharedPreferences("daily-ad-click", MODE_PRIVATE);
        prefDailyAdClicked = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        dailyAdClicked = prefDailyAdClicked.getInt("daily-ad-click", 0);

        // default app install time
        //appInstallPref = getSharedPreferences("ins-time", MODE_PRIVATE);
        appInstallPref = getSharedPreferences("ins-time", 0);
        
        int arrow;
        if(SettingsClass.supportRTL) {forceRTLIfSupported(); }

        context = this;
        consentSDK = new ConsentSDK.Builder(this)
                .addPrivacyPolicy(SettingsClass.privacy_policy_url) // Add your privacy policy url
                .addPublisherId(SettingsClass.publisherID) // Add your admob publisher id
                .build();

        consentSDK.checkConsent(new ConsentSDK.ConsentCallback() {
            @Override
            public void onResult(boolean isRequestLocationInEeaOrUnknown) {
                // Your code
            }
        });
        AdConstantControl.adNetworkIntializeRequest(Splash.this);
        Calendar rightNow = Calendar.getInstance();
        if ((rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) ||
                (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ||
                (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) ||
                (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)||
                (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
        ) {
            isGoogleAdEnabled = true;
        } else {   // google ad disabled else statement
            isGoogleAdEnabled = false;
        }
   /*     mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(SettingsClass.Interstitial);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();*/

        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isItFirestTime()) {
                    Intent i = new Intent(Splash.this, HelpActivity.class);
                    i.putExtra("from", 1);
                    startActivity(i);
                    Splash.this.finish();
                } else {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    //showFullAd(false);
                    Splash.this.finish();
                }
            }
        });
        
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
  /*  private void requestNewInterstitial() {
        mInterstitialAd.loadAd(ConsentSDK.getAdRequest(context));
    }

    private void showFullAd(boolean count){
        if(count){
            SettingsClass.mCount++;
            if(SettingsClass.mCount == SettingsClass.nbShowInterstitial) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else SettingsClass.mCount--;
            }
        } else if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }*/


   
    public boolean isItFirestTime() {
        if (sharedPreferences.getBoolean("firstTime", true)) {
            sharedEditor.putBoolean("firstTime", false);
            sharedEditor.commit();
            sharedEditor.apply();
            return true;
        } else {
            return false;
        }

    }
}
