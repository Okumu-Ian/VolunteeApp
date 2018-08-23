package eeyan.icelabs.bigman.volunteeapp;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.RecyclerItemListener;
import adapters.SkillAdapter;
import models.SkillModel;

import static adapters.SkillAdapter.*;

public class PickSkill extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<SkillModel> modelList;
    private SkillModel skillModel;
    private SkillAdapter adapter;
    private android.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_skill);
        initUI();
        //setupTransitions();
    }

    private void initUI()
    {
        modelList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.skillList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        loadDetails();
    }

    private void loadataFromNet(String URL)
    {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("myArray");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("");
                        String image = object.getString("");
                        String items = object.getString("");
                        String identity = object.getString("");
                    }
                }catch (JSONException exc)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    @TargetApi(21)
    private void setupTransitions()
    {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.slide_it);
        getWindow().setExitTransition(slide);

    }

    private void loadDetails()
    {
        int [] myInt = {R.drawable.one,
        R.drawable.two,
        R.drawable.three,
        R.drawable.four,
        R.drawable.five,
        R.drawable.six,
        R.drawable.seven,
        R.drawable.eight,
        R.drawable.nine};
        String [] myStrings =
                {
                  "Computers and Tech",
                        "Education and Learning",
                        "Medicine and Microbiology",
                        "Chemistry",
                        "Physics",
                        "Law and Legal",
                        "Capentry and WoodWork",
                        "BlackSmithery and MetalWork",
                        "Building and Construction"
                };

        for (int i = 0; i <myInt.length; i++)
        {
            skillModel = new SkillModel();
            skillModel.setSkill_image_local(myInt[i]);
            skillModel.setSkill_name(myStrings[i]);
            skillModel.setSkill_id(""+i);
            modelList.add(skillModel);
        }
        adapter = new SkillAdapter(modelList,getApplicationContext(),PickSkill.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_share:
                shareIntent();
                break;
            case R.id.app_bar_search:
                break;
            default:
                super.onOptionsItemSelected(item);

        }
        return true;
    }

    private void shareIntent()
    {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setAction("text/plain");
        String shareBodyText = "https://play.google.com/youtube";
        String shareTitle = "Hey guys check out VolunteeApp. Its really cool.";
        String chooseTitle = "Choose sharing method";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareTitle);
        intent.putExtra(Intent.EXTRA_TEXT,shareBodyText);
        startActivity(Intent.createChooser(intent,chooseTitle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skill_set,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }


}
