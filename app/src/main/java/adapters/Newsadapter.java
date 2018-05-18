package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eeyan.icelabs.bigman.volunteeapp.ProfileActivity;
import eeyan.icelabs.bigman.volunteeapp.R;
import models.Newsmodel;

public class Newsadapter extends RecyclerView.Adapter<Newsadapter.Newsholder> {

    private Context mContext;
    private Newsmodel newsmodel;
    private List<Newsmodel> list;


    public Newsadapter(Context mContext, List<Newsmodel> list) {
        this.mContext = mContext;
        this.list = list;

    }

    public class Newsholder extends RecyclerView.ViewHolder
    {
        private ImageView img_news_holder;
        private TextView txt_name_holder,txt_name_timestamp,txt_name_news;
        private CircleImageView img_logo_holder;

        public Newsholder(View view)
        {
            super(view);

            img_logo_holder = (CircleImageView) view.findViewById(R.id.img_newsfeed_poster);
            img_news_holder = (ImageView) view.findViewById(R.id.img_newsfeed_post);

            txt_name_holder = (TextView) view.findViewById(R.id.txt_name_holder);
            txt_name_timestamp = (TextView) view.findViewById(R.id.txt_timestamp_holder);
            txt_name_news = (TextView) view.findViewById(R.id.txt_newsfeed_main_text);

        }
    }

    @Override
    public Newsholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View row_view = LayoutInflater.from(mContext).inflate(R.layout.newsfeed_row,parent,false);
        Newsholder newsholder = new Newsholder(row_view);
        return newsholder;

    }

    @Override
    public void onBindViewHolder(Newsholder holder, final int position) {

        newsmodel = list.get(position);
        Picasso.get().load(newsmodel.getImageUrl()).into(holder.img_news_holder);
        Picasso.get().load(newsmodel.getImageUrl()).into(holder.img_logo_holder);
        holder.txt_name_news.setText(newsmodel.getMain_news());
        holder.txt_name_holder.setText(newsmodel.getHolderName());
        holder.txt_name_timestamp.setText(newsmodel.getTimestamp());
        holder.img_news_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToast(position);
            }
        });
    }

    private void clickToast(int position)
    {
        mContext.startActivity(new Intent(mContext, ProfileActivity.class));
        //Toast.makeText(mContext, "You have clicked: "+list.get(position).getHolderName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
