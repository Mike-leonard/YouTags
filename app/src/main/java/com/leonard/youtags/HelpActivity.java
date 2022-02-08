package com.leonard.youtags;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HelpActivity extends AppCompatActivity {

    public static Activity context;
    public static LinearLayout unitBanner;
    private Button mainPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        context = this;
        int arrow;
        if(SettingsClass.supportRTL) {forceRTLIfSupported(); arrow=R.drawable.ic_arrow_back_rtl;}
        else arrow = R.drawable.ic_arrow_back;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        this.setTitle(getResources().getString(R.string.title_activity_help));

        // banner ad control
        View rootView = getWindow().getDecorView().getRootView();
        AdConstantControl.bannerAdControl(HelpActivity.this, R.id.unitads, rootView);

        int i = getIntent().getIntExtra("from", 0);
        if (i == 1) {
            mainPager = findViewById(R.id.main_pager);
            mainPager.setVisibility(View.VISIBLE);
            mainPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HelpActivity.this, MainActivity.class));
                    //showFullAd(false);
                    HelpActivity.this.finish();
                }
            });
        } else {

        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //showFullAd(true);
        int random = ((int) (Math.random() * 3.0d)) + 1;
        if (random == 2) {
            AdConstantControl.adControl(HelpActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
