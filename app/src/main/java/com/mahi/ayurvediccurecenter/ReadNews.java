package com.mahi.ayurvediccurecenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosalgeek.asynctask.*;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;


/**
 * Created by HP on 11/16/2016.
 */

public class ReadNews extends AppCompatActivity implements View.OnClickListener {
    TextView title,description,date,sharenews;
    ImageView img,whatsapp;
    String html,desr;
    String ptr= "src\\s*=\\s*([\"'])?([^\"']*)";
    Pattern p = Pattern.compile(ptr);
    Matcher m;
    int position;
    private AdView mAdView;
    AdRequest adRequest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsreadscreen);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Display icon in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title= (TextView) findViewById(R.id.newstitle);
        date= (TextView) findViewById(R.id.date);
        sharenews= (TextView) findViewById(R.id.sharenews);
        sharenews.setOnClickListener(this);
        img= (ImageView) findViewById(R.id.newsimg);
        whatsapp= (ImageView) findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(this);
        position=Post_Adapter.selectedPositon;
        description= (TextView) findViewById(R.id.newsdescription);
        title.setText(MainActivity.ptitle.get(Post_Adapter.selectedPositon));
        description.setText(MainActivity.post.get(Post_Adapter.selectedPositon));
        getSupportActionBar().setTitle(title.getText());
            ImageLoader.getInstance().displayImage(MainActivity.pimg.get(Post_Adapter.selectedPositon),img);

        mAdView = (AdView) findViewById(R.id.adView);
      adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId())
{
    case android.R.id.home:
        onBackPressed();
        break;
}

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sharenews:
               shareIntent(false);
                break;
            case R.id.whatsapp:
                shareIntent(true);
        }
    }



    private void shareIntent(boolean b)

    {
        try {


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (b) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://ayurvediccureceterdhar.blogspot.in"+"\n *" + title.getText().toString() + "*\n" + description.getText().toString()+"\n*Contact Person: Dr. Rajendra Chandrawat* \n*Mobile No:+919425716310* \n" +
                        "*email:ayurvediccurecenter@gmail.com*");
            } else
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://ayurvediccureceterdhar.blogspot.in"+"\n" + title.getText().toString() + "\n" + description.getText().toString()+"\nContack Person: Dr. Rajendra Chandrawat \nMobile No:+919425716310 \n" +
                        "email:ayurvediccurecenter@gmail.com");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


}
