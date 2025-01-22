package com.mrdeveloper.newsapps;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.mrdeveloper.newsapps.Adapter.CustomAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    SearchView searchView;
    //ImageSlider imageSlider;
    public static TextView trendingNewsTv;
    RecyclerView recyclerView;
    ImageView noDataImage;
    ProgressBar progressBar;

    NewsApiClient newsApiClient;

    CustomAdapter adapter;

    ArrayList<HashMap<String,String>> newsArraylist;
    HashMap<String,String> hashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = myView.findViewById(R.id.searchView);
        //imageSlider = myView.findViewById(R.id.imageSlider);
        trendingNewsTv = myView.findViewById(R.id.trendingNewsTv);
        recyclerView = myView.findViewById(R.id.recyclerView);
        noDataImage = myView.findViewById(R.id.noDataImage);
        progressBar = myView.findViewById(R.id.progressBar);

        trendingNewsTv.setSelected(true);


        loadData();
        

        adapter = new CustomAdapter(getActivity(),newsArraylist);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return myView;
    }

    public void loadData() {

        newsArraylist = new ArrayList<>();

        newsApiClient = new NewsApiClient("9482bc58b4884463b1eee878a9004948");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                newsArraylist.clear();

                progressBar.setVisibility(View.VISIBLE);

                newsApiClient.getEverything(
                        new EverythingRequest.Builder()
                                .q(query)
                                .language(MainActivity.LANGUAGE)
                                .sortBy("popularity")
                                .build(),
                        new NewsApiClient.ArticlesResponseCallback() {
                            @Override
                            public void onSuccess(ArticleResponse response) {
                                //System.out.println(response.getArticles().get(0).getTitle());

                                for (int i = 0; i < response.getArticles().size(); i++) {

                                    String title = response.getArticles().get(i).getTitle();
                                    String description = response.getArticles().get(i).getDescription();
                                    String imageUrl = response.getArticles().get(i).getUrlToImage();
                                    String publishTime = response.getArticles().get(i).getPublishedAt();

                                    hashMap = new HashMap<>();

                                    hashMap.put("title",title);
                                    hashMap.put("description",description);
                                    hashMap.put("imageUrl",imageUrl);
                                    hashMap.put("publishTime",publishTime);

                                    newsArraylist.add(hashMap);
                                }

                                progressBar.setVisibility(View.GONE);

                                if (newsArraylist.size() > 0) {
                                    Toast.makeText(getContext(), "Data Load Successful " + newsArraylist.size(), Toast.LENGTH_SHORT).show();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noDataImage.setVisibility(View.GONE);
                                } else {
                                    Glide.with(getContext())
                                            .asGif()
                                            .load(R.drawable.no_data)
                                            .into(noDataImage);
                                    recyclerView.setVisibility(View.GONE);
                                    noDataImage.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "No Data Here", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println(throwable.getMessage());

                                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                adapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
        // /v2/everything

        progressBar.setVisibility(View.VISIBLE);

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("bangladesh")
                        .language(MainActivity.LANGUAGE)
                        .sortBy("popularity")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        //System.out.println(response.getArticles().get(0).getTitle());

                        for (int i = 0; i < response.getArticles().size(); i++) {

                            String title = response.getArticles().get(i).getTitle();
                            String description = response.getArticles().get(i).getDescription();
                            String imageUrl = response.getArticles().get(i).getUrlToImage();
                            String publishTime = response.getArticles().get(i).getPublishedAt();

                            hashMap = new HashMap<>();

                            hashMap.put("title",title);
                            hashMap.put("description",description);
                            hashMap.put("imageUrl",imageUrl);
                            hashMap.put("publishTime",publishTime);

                            newsArraylist.add(hashMap);
                        }


                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Data Load Successful " + newsArraylist.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Toast.makeText(getActivity(), "Data Not Load", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}