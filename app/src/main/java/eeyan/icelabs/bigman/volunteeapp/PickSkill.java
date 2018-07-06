package eeyan.icelabs.bigman.volunteeapp;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import adapters.SkillAdapter;
import models.SkillModel;

public class PickSkill extends AppCompatActivity {

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
    }

    private void initUI()
    {
        modelList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.skillList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        loadDetails();
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
            modelList.add(skillModel);
        }

        adapter = new SkillAdapter(modelList,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.app_bar_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skill_set,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();
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
