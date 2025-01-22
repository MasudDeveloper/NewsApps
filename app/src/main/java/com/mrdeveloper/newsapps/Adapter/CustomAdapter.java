package com.mrdeveloper.newsapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mrdeveloper.newsapps.HomeFragment;
import com.mrdeveloper.newsapps.NewsDetailsActivity;
import com.mrdeveloper.newsapps.NewsModel;
import com.mrdeveloper.newsapps.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String,String>> newsArraylist;

    HashMap<String,String> hashMap;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> newsArraylist) {
        this.context = context;
        this.newsArraylist = newsArraylist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvTitle, tvDescription, tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.news_image);
            tvTitle = itemView.findViewById(R.id.news_title);
            tvDescription = itemView.findViewById(R.id.news_description);
            tvTime = itemView.findViewById(R.id.news_published_at);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String title = newsArraylist.get(position).get("title");
        String description = newsArraylist.get(position).get("description");
        String imageUrl = newsArraylist.get(position).get("imageUrl");
        String publishTime = newsArraylist.get(position).get("publishTime");

        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);
        holder.tvTime.setText(publishTime);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.load_image)// ডিফল্ট প্লেসহোল্ডার ইমেজ
                    .into(holder.imageView);
        } else {
            // যদি ইমেজ URL খালি বা null হয়, তাহলে প্লেসহোল্ডার ইমেজ সেট করুন
            holder.imageView.setImageResource(R.drawable.no_image);
        }

        if (newsArraylist.size() > 0) {

            HomeFragment.trendingNewsTv.setText(newsArraylist.get(0).get("title"));

        } else {
            HomeFragment.trendingNewsTv.setText("");
            Toast.makeText(context, "No Trending News.", Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("description",description);
                intent.putExtra("publishTime",publishTime);

                holder.imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = holder.imageView.getDrawingCache();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

                byte[] imageBytes = outputStream.toByteArray();

                intent.putExtra("image",imageBytes);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsArraylist.size();
    }



}
