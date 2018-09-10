package eeyan.icelabs.bigman.volunteeapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.Newsadapter;
import models.Newsmodel;
import utils.ShakeDetector;

public class MainFeed extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Newsmodel newsmodel;
    private Newsadapter newsadapter;
    private List<Object> newsmodelList;
    private static String data_url = "https://yts.am/api/v2/list_movies.json?limit=10&page=1";
    private SensorManager sensorManager;
    private ShakeDetector detector;
    private Sensor accelerometer;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private PullRefreshLayout refreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        this.setContentView(R.layout.activity_main_feed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        initUI();

        checkCurrentUser();
        loadData(data_url);

    }

    private void checkCurrentUser()
    {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user!=null)
            return;
            else
                startActivity(new Intent(MainFeed.this,LoginActivity.class));
    }

    private void initUI()
    {
        newsmodelList = new ArrayList<>();
        refreshLayout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);
        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                   loadData(data_url);
                   refreshLayout.setRefreshing(false);
                    }
                },5000);


            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.newsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        sensorManager = (SensorManager) MainFeed.this.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        detector = new ShakeDetector();
        detector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake(int count) {
                }
        });

    }

    @TargetApi(21)
    private void setUpTransition()
    {
        Fade fade = (Fade) TransitionInflater.from(MainFeed.this).inflateTransition(R.transition.fade_it);
        getWindow().setEnterTransition(fade);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(detector,accelerometer,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(detector);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_feed,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.app_bar_switch:
                Toast.makeText(this, "Kindly shake to referesh", Toast.LENGTH_LONG).show();
                break;
            case R.id.app_bar_logout:
                auth.getInstance().signOut();
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
