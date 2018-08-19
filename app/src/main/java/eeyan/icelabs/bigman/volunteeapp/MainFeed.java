package eeyan.icelabs.bigman.volunteeapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.Newsadapter;
import models.Newsmodel;

public class MainFeed extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Newsmodel newsmodel;
    private Newsadapter newsadapter;
    private List<Object> newsmodelList;
    private static String data_url = "https://yts.am/api/v2/list_movies.json?limit=10&page=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preventing screenshots in the application
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        this.setContentView(R.layout.activity_main_feed);
        initUI();
        loadData(data_url);

    }

    private void initUI()
    {
        newsmodelList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.newsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    private void loadData(String web_url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(web_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray array = jsonObject.getJSONArray("movies");
                    for (int i = 0; i < array.length() ; i++)
                    {
                        newsmodel = new Newsmodel();
                        JSONObject object = array.getJSONObject(i);
                        newsmodel.setHolderId(object.getString("id"));
                        newsmodel.setHolderName(object.getString("title_english"));
                        newsmodel.setImageUrl(object.getString("large_cover_image"));
                        newsmodel.setMain_news(object.getString("summary"));
                        newsmodel.setTimestamp(object.getString("year"));
                        newsmodelList.add(newsmodel);
                    }

                    newsadapter = new Newsadapter(MainFeed.this,newsmodelList);
                    recyclerView.setAdapter(newsadapter);

                }catch(JSONException exception)
                {
                    Log.e("JSON_ERROR :-> ",exception.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainFeed.this, ""+error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        Volley.newRequestQueue(MainFeed.this).add(jsonObjectRequest);
    }



}
