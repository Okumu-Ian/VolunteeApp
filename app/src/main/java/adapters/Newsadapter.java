package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eeyan.icelabs.bigman.volunteeapp.R;
import models.Newsmodel;

public class Newsadapter extends RecyclerView.Adapter<Newsadapter.Newsholder> {

    private Context mContext;
    private Newsmodel newsmodel;
    private List<Newsmodel> list;
    private List<Newsmodel> filteredList;

    public Newsadapter(Context mContext, List<Newsmodel> list) {
        this.mContext = mContext;
        this.list = list;
        this.filteredList = list;
    }

    public class Newsholder extends RecyclerView.ViewHolder
    {
        private ImageView img_news_holder;
        private TextView txt_name_holder,txt_name_timestamp,txt_name_news;
        private ImageView img_logo_holder;

        public Newsholder(View view)
        {
            super(view);

            img_logo_holder = (ImageView) view.findViewById(R.id.img_newsfeed_poster);
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
    public void onBindViewHolder(Newsholder holder, int position) {

        newsmodel = list.get(position);
        holder.txt_name_news.setText(newsmodel.getNewsText());
        holder.txt_name_holder.setText(newsmodel.getHolderName());
        holder.txt_name_timestamp.setText(newsmodel.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
