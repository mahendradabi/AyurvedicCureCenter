package com.mahi.ayurvediccurecenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by HP on 11/16/2016.
 */

public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.MyViewHolder>{
    public static int selectedPositon;
  static  public DisplayImageOptions options;
    String ptr= "src\\s*=\\s*([\"'])?([^\"']*)";
    String desr;
    Pattern p = Pattern.compile(ptr);
    Context ctx;
    Matcher m;
    ArrayList<String> title,description,imgurl;
public Post_Adapter(Context ctx, ArrayList<String> title,ArrayList<String> description,ArrayList<String> imgurl )
{
    this.ctx=ctx;
    this.title=title;
    this.description=description;
    this.imgurl=imgurl;

    ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(ctx);
    config.threadPriority(Thread.NORM_PRIORITY - 2);
    config.denyCacheImageMultipleSizesInMemory();
    config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
    config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
    config.tasksProcessingOrder(QueueProcessingType.LIFO);
    config.writeDebugLogs(); // Remove for release app


    ImageLoader.getInstance().init(config.build());

    options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.logo)
            .showImageForEmptyUri(R.drawable.logo)
            .showImageOnFail(R.drawable.logo)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();


}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            holder.tvtitle.setText(title.get(position));
            holder.tvdescription.setText(description.get(position));

            ImageLoader.getInstance()
                    .displayImage(imgurl.get(position), holder.imageView, options);
            holder.newscard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPositon=position;
                   ctx.startActivity(new Intent(ctx, ReadNews.class));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle,tvdescription;
        ImageView imageView;
        CardView newscard;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            this.tvtitle= (TextView) itemView.findViewById(R.id.newstitle);
            this.tvdescription= (TextView) itemView.findViewById(R.id.newsdescription);
            this.imageView= (ImageView) itemView.findViewById(R.id.imageView2);
            this.newscard= (CardView) itemView.findViewById(R.id.newscard);
        }
    }
}
