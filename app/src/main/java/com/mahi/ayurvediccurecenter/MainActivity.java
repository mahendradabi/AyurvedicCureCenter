package com.mahi.ayurvediccurecenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int SETTINGS_RESULT =12 ;
    String desr;
Post_Adapter post_adapter;
    String nextpagetoken;
    ProgressDialog pd;
    String TEMP_FILE_NAME = "blogpost";
    File tempFile;

String hiturl="https://www.googleapis.com/blogger/v3/blogs/3145083365830210982/posts?fetchImages=true&maxResults=20&key=AIzaSyBJyNgQilZaQSOfOaR11hGaVqWv7aD-ETk";
    //List for title ,post description, images;
  public static   ArrayList<String> ptitle=new ArrayList<>();
  public static   ArrayList<String> post=new ArrayList<>();
  public static   ArrayList<String> pimg=new ArrayList<>();

    //reclyerview for display list of post
    RecyclerView recyclerView;
    private AdView mAdView;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(getApplicationContext(), UserSettingActivity.class);
      //  startActivityForResult(i, SETTINGS_RESULT);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        pd=new ProgressDialog(this);
        pd.setTitle("Wait");
        pd.setMessage("Refreshing Please Wait....");

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView= (RecyclerView) findViewById(R.id.postlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        post_adapter=new Post_Adapter(this,ptitle,post,pimg);
        recyclerView.setAdapter(post_adapter);

        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        invalidateOptionsMenu();
        new getJsonString().execute(hiturl);
//getPost(hiturl);

       /* try {
            String json=run("https://news.google.co.in/news?cf=all&hl=hi&ned=hi_in&output=rss");
            Log.d("test",json);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }
    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }





/*

void getPost(String url)
{

}*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new getJsonString().execute("https://www.googleapis.com/blogger/v3/blogs/3145083365830210982/posts?pageToken="+nextpagetoken+"&fetchImages=true&maxResults=20&key=AIzaSyBJyNgQilZaQSOfOaR11hGaVqWv7aD-ETk");
            //    getPost("https://www.googleapis.com/blogger/v3/blogs/3145083365830210982/posts?pageToken="+nextpagetoken+"&fetchImages=true&maxResults=20&key=AIzaSyBJyNgQilZaQSOfOaR11hGaVqWv7aD-ETk");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SETTINGS_RESULT)
        {
            displayUserSettings();
        }

    }

    private void displayUserSettings()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String  settings = "";

        settings=settings+"Password: " + sharedPrefs.getString("prefUserPassword", "NOPASSWORD");

        settings=settings+"\nRemind For Update:"+ sharedPrefs.getBoolean("prefLockScreen", false);

        settings=settings+"\nUpdate Frequency: "
                + sharedPrefs.getString("prefUpdateFrequency", "NOUPDATE");


       Log.d("MAHI",settings);
    }


    class getJsonString extends AsyncTask<String,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {

                String json=  run(strings[0]);
                Log.d("finaltest",json);
                JSONObject jsonObjec=new JSONObject(json);
                JSONArray items=jsonObjec.getJSONArray("items");
                if (jsonObjec.has("nextPageToken"))
                {
                    nextpagetoken=jsonObjec.optString("nextPageToken");
                }
                else {
                    Toast.makeText(getApplicationContext(),"No More Post",Toast.LENGTH_LONG).show();

                }
                if(items!=null)
                {
                    for(int i=0;i<items.length();i++)
                    {
                        String title=items.getJSONObject(i).optString("title");
                        ptitle.add(title);
                        String content=items.getJSONObject(i).optString("content");
                        desr= Html.fromHtml(content).toString();
                        desr= desr.substring(desr.indexOf('\n')+2);
                        post.add(desr);
                        String  url1;
                        if(items.getJSONObject(i).has("images"))
                        {
                            Log.d("mahitest","hai url");
                            url1  = items.getJSONObject(i).getJSONArray("images").getJSONObject(0).optString("url");
                        }
                        else {
                            url1 = "htt://winnipegayurvediccentre.ca/wp-content/uploads/2011/01/Ayurvedic-Supplements.jpg";
                            Log.d("mahitest","nahi tha  url aad ho gya hai");

                        }
                        Log.d("imgurl",i+"="+url1);
                        pimg.add(url1);
                    }



                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                pd.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            post_adapter.notifyDataSetChanged();
        }
    }

    public String getTempFile(String filename) {
        /** Getting Cache Directory */
        StringBuilder returnjson=new StringBuilder();
        File cDir = getBaseContext().getCacheDir();
        /** Getting a reference to temporary file, if created earlier */
        tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME+filename);

        String strLine="";
        /** Reading contents of the temporary file, if already exists */
        try {
            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);

            /** Reading the contents of the file , line by line */
            while( (strLine=bReader.readLine()) != null  ){
                returnjson.append(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return returnjson.toString();
    }

    private void writeCache(String json)
    {
        FileWriter writer=null;
        try {
            writer = new FileWriter(tempFile);
            /** Saving the contents to the file*/
            writer.write(json);
            /** Closing the writer object */
            writer.close();
            //  Toast.makeText(getBaseContext(), "Temporarily saved contents in " + tempFile.getPath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
