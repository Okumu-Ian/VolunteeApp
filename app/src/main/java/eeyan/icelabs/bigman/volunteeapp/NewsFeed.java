package eeyan.icelabs.bigman.volunteeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.List;

import adapters.Newsadapter;
import models.Newsmodel;

public class NewsFeed extends AppCompatActivity {

    private RecyclerView news_list;
    private List<Newsmodel> newsmodelList;
    private Newsadapter newsadapter;
    private Newsmodel newsmodel;
    private LinearLayout linearLayout;
    private String api_url = "https://yts.am/api/v2/list_movies.json?limit=10&page=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
    }

    private void initUI()
    {
        news_list = (RecyclerView) findViewById(R.id.newsFeedList);
        linearLayout = (LinearLayout) findViewById(R.id.pBarList);

        LinearLayoutManager manager = new LinearLayoutManager(NewsFeed.this,LinearLayoutManager.VERTICAL,false);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();




    }

    private void loadFirstPage()
    {

    }

    private void loadConsecutivePages()
    {

    }

}
