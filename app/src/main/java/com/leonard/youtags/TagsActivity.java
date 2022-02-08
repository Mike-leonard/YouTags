package com.leonard.youtags;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.leonard.youtags.adapter.TagsAdapter;
import com.leonard.youtags.db.DataBaseHelper;
import com.leonard.youtags.module.Tags;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class TagsActivity extends AppCompatActivity implements TagsAdapter.ItemClickListener {

    private RecyclerView tagsListView;
    private TagsAdapter adapter;
    private List<Tags> mTags;
    private DataBaseHelper mDBHelper;
    int categorie_id;
    public static Activity context;
    public static LinearLayout unitBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int arrow;
        if(SettingsClass.supportRTL) {forceRTLIfSupported(); arrow=R.drawable.ic_arrow_back_rtl;}
        else arrow = R.drawable.ic_arrow_back;
        setContentView(R.layout.activity_tags);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        this.setTitle(getIntent().getStringExtra("title")+" "+getResources().getString(R.string.tags_activity));

        // banner ad control
        View rootView = getWindow().getDecorView().getRootView();
        AdConstantControl.bannerAdControl(TagsActivity.this, R.id.unitads, rootView);

        mDBHelper = new DataBaseHelper(this);
        tagsListView = (RecyclerView)findViewById(R.id.tags);
        tagsListView.setHasFixedSize(true);
        tagsListView.setLayoutManager(new LinearLayoutManager(this));

        File database = getApplicationContext().getDatabasePath(DataBaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error"+DataBaseHelper.DBLOCATION, Toast.LENGTH_LONG).show();
                return;
            }
        }
        categorie_id = getIntent().getIntExtra("id_cat",1);
        adapterTags();
    }

    public void adapterTags(){
        //Get product list in db when db exists
        mTags = mDBHelper.getTagsGroupeFilterByIdCat(categorie_id);
        //Init adapter
        adapter = new TagsAdapter(this, mTags);
        adapter.setClickListener(this);
        //Set adapter for listview
        tagsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DataBaseHelper.DBNAME);
            String outFileName = DataBaseHelper.DBLOCATION + DataBaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
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
        int random = ((int) (Math.random() * 4.0d)) + 1;
        if (random == 2) {
            AdConstantControl.adControl(TagsActivity.this);
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
