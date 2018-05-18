package eeyan.icelabs.bigman.volunteeapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.InfiniteScroller;
import adapters.Newsadapter;
import models.Newsmodel;

public class NewsFeed extends AppCompatActivity {

    private RecyclerView news_list;
    private List<Newsmodel> newsmodelList;
    private Newsadapter newsadapter;
    private Newsmodel newsmodel;
    private LinearLayout linearLayout;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initUI();
    }

    private void initUI()
    {
        news_list = (RecyclerView) findViewById(R.id.newsFeedList);
        linearLayout = (LinearLayout) findViewById(R.id.pBarList);
        newsmodelList =  new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(NewsFeed.this,LinearLayoutManager.VERTICAL,false);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        news_list.setLayoutManager(manager);
        news_list.setItemAnimator(animator);

        mContext = getApplicationContext();

        news_list.addOnScrollListener(new InfiniteScroller() {
           @Override
           public void onLoadMore(int current_page) {
               loadConsecutivePages(current_page);
           }
       });

        loadFirstPage();

    }

    private void loadFirstPage()
    {


        final String load_more = "https://yts.am/api/v2/list_movies.json?limit=10&page=1";
        JsonObjectRequest request = new JsonObjectRequest(load_more, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject other = response.getJSONObject("data");
                    JSONArray array = other.getJSONArray("movies");

                    for (int i = 0; i < array.length(); i++)
                    {
                        newsmodel = new Newsmodel();
                        JSONObject myOnject = array.getJSONObject(i);
                        newsmodel.setImageUrl(myOnject.getString("large_cover_image"));
                        newsmodel.setHolderName(myOnject.getString("title_english"));
                        newsmodel.setTimestamp(myOnject.getString("year"));
                        newsmodel.setHolderId(myOnject.getString("id"));
                        newsmodel.setMain_news(myOnject.getString("summary"));
                        newsmodelList.add(newsmodel);
                    }

                    newsadapter = new Newsadapter(NewsFeed.this,newsmodelList);
                    news_list.setAdapter(newsadapter);

                }
                catch (JSONException exception)
                {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NewsFeed.this, ""+error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        Volley.newRequestQueue(mContext).add(request);

    }

    private void loadConsecutivePages(int page)
    {

        linearLayout.setVisibility(View.VISIBLE);
        String web_url = "https://yts.am/api/v2/list_movies.json?limit=10&page="+page;
        JsonObjectRequest request = new JsonObjectRequest(web_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject other = response.getJSONObject("data");
                    JSONArray array = other.getJSONArray("movies");

                    for (int i = 0; i < array.length(); i++)
                    {
                        newsmodel = new Newsmodel();
                        JSONObject myOnject = array.getJSONObject(i);
                        newsmodel.setImageUrl(myOnject.getString("large_cover_image"));
                        newsmodel.setHolderName(myOnject.getString("title_english"));
                        newsmodel.setTimestamp(myOnject.getString("year"));
                        newsmodel.setHolderId(myOnject.getString("id"));
                        newsmodel.setMain_news(myOnject.getString("summary"));
                        newsmodelList.add(newsmodel);
                    }

                    newsadapter.notifyDataSetChanged();
                    linearLayout.setVisibility(View.GONE);

                }
                catch (JSONException exception)
                {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(mContext).add(request);

    }

}
