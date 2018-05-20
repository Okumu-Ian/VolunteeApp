package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import eeyan.icelabs.bigman.volunteeapp.R;
import models.Profileitems;

/**
 * Created by The Architect on 5/20/2018.
 */

public class Profileadapter extends RecyclerView.Adapter<Profileadapter.ProfilefabHolder>{

    public class ProfilefabHolder extends RecyclerView.ViewHolder
    {
        private FloatingActionButton floatingActionButton;
        private TextView textView;
        public ProfilefabHolder(View itemView) {
            super(itemView);
            floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.item_fab);
            textView = (TextView) itemView.findViewById(R.id.txt_full_profile_description);
        }
    }

    private List<Profileitems> list;
    private Context mContext;
    private Activity mActivity;

    public Profileadapter(List<Profileitems> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ProfilefabHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.profile_item,parent,false);
            ProfilefabHolder holder = new ProfilefabHolder(view);

        return holder;


    }

    @Override
    public void onBindViewHolder(ProfilefabHolder holder, final int position) {

        Profileitems profileitems = list.get(position);
        //holder.floatingActionButton.setImageResource(profileitems.getSmallIcon());
        holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener(position);
            }
        });
        Picasso.get().load(profileitems.getSmallIcon()).into(holder.floatingActionButton);
        holder.textView.setText(profileitems.getId());
    }

    private void clickListener(int position)
    {
        Toast.makeText(mContext, position+" Coming soon.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
