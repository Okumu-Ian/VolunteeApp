package eeyan.icelabs.bigman.volunteeapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.Profileadapter;
import de.hdodenhof.circleimageview.CircleImageView;
import models.Profileitems;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Profileadapter profileadapter;
    private List<Profileitems> profileitemsList;
    private CircleImageView circleImageView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();

    }

    private void initUI()
    {
        profileitemsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.profileItemList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        circleImageView = (CircleImageView) findViewById(R.id.img_profile_photo);
        imageView = (ImageView) findViewById(R.id.img_profile_cover);

        prepareData();
    }

    private void prepareData()
    {

        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        doStuff();
                    }
                });
                return null;
            }
        }.execute();

    }



    void doStuff()
    {
        int [] images =
                {
                        R.drawable.home_ic,
                        R.drawable.edit_ic,
                        R.drawable.resume_ic,
                        R.drawable.message_ic,
                        R.drawable.notification_ic,
                        R.drawable.logout_ic
                };
        String [] titles =
                {
                        "Home",
                        "Edit",
                        "Resume",
                        "Messages",
                        "Alerts",
                        "Logout"
                };
        for (int x = 0; x < images.length; x++)
        {
            Profileitems profileitems = new Profileitems();
            profileitems.setSmallIcon(images[x]);
            profileitems.setId(titles[x]);
            profileitemsList.add(profileitems);
        }

        profileadapter = new Profileadapter(profileitemsList,ProfileActivity.this,ProfileActivity.this);
        recyclerView.setAdapter(profileadapter);

        String photo = "https://images.unsplash.com/photo-1507146426996-ef05306b995a?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1cf9c13e09f5f2ec5139b6475751b310&auto=format&fit=crop&w=1050&q=80";
        String photo2 = "https://images.unsplash.com/photo-1503385824845-4f3407ce5e03?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=9217a85a5ea86011aa582344960c31d6&auto=format&fit=crop&w=1050&q=80";
        loadPhoto(circleImageView,photo);
        loadPhoto(imageView,photo2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(ProfileActivity.this,Settings.class));
        return true;
    }

    private void loadPhoto(View view, String image_url)
    {
        Picasso.get().load(image_url).into((ImageView) view);
    }

}
