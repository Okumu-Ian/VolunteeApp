package eeyan.icelabs.bigman.volunteeapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
    private int REQUEST_CODE = 0101;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initUI();
    }

    private boolean isReadStorageAllowed() {
        boolean staus = false;
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            staus = true;
        //If permission is not granted returning false
        return staus;
    }

    public void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

        }
        ActivityCompat.requestPermissions(NewsFeed.this,new String[]{Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(mContext, "Permission granted!", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(mContext, "Permission grant failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_feed,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(mContext, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @SuppressLint("ResourceAsColor")
    private void initUI()
    {
        news_list = (RecyclerView) findViewById(R.id.newsFeedList);
        linearLayout = (LinearLayout) findViewById(R.id.pBarList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        //swipeRefreshLayout.setColorSchemeColors(R.color.blue,R.color.colorAccent,R.color.green,R.color.yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });
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

        if (!isReadStorageAllowed())
        {
            checkPermission();
        }
        getSupportActionBar().setLogo(R.drawable.other_test_icon);
        getSupportActionBar().setIcon(R.drawable.other_test_icon);
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
        //swipeRefreshLayout.setRefreshing(false);

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
