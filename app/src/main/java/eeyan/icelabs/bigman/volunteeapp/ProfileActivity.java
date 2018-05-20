package eeyan.icelabs.bigman.volunteeapp;

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
                        "Notifications",
                        "Logout"
                };
        for (int x = 0; x < images.length; x++)
        {
            Profileitems profileitems = new Profileitems();
            profileitems.setSmallIcon(images[x]);
            profileitems.setId(titles[x]);
            profileitemsList.add(profileitems);
        }

        profileadapter = new Profileadapter(profileitemsList,ProfileActivity.this);
        recyclerView.setAdapter(profileadapter);

        String photo = "https://images.unsplash.com/photo-1506158981101-17d5fadfa720?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=e7d95e455554ef8e79ab93a24e450b9c&auto=format&fit=crop&w=634&q=80";
        String photo2 = "https://images.unsplash.com/photo-1526371962155-8f27ed893ab7?ixlib=rb-0.3.5&s=9b12ed29be8300e4dbe42b1a7f58e15a&auto=format&fit=crop&w=1350&q=80";
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
        return true;
    }

    private void loadPhoto(View view, String image_url)
    {
        Picasso.get().load(image_url).into((ImageView) view);
    }

}
