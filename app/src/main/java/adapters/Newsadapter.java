package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eeyan.icelabs.bigman.volunteeapp.ProfileActivity;
import eeyan.icelabs.bigman.volunteeapp.R;
import models.Newsmodel;

public class Newsadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Newsmodel newsmodel;
    private List<Object> list;
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    public Newsadapter(Context mContext, List<Object> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = list.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd)
        {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType)
        {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_unified,parent,false);
                return new UnifiedAdAdapter(unifiedLayout);
            case MENU_ITEM_VIEW_TYPE:
                default:
                    View row_view = LayoutInflater.from(mContext).inflate(R.layout.newsfeed_row,parent,false);
                    Newsholder newsholder = new Newsholder(row_view);
                    return newsholder;

        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holdera, final int position) {

        int viewType = getItemViewType(position);
        switch(viewType)
        {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) list.get(position);
                populateNativeAdView(nativeAd,((UnifiedAdAdapter) holdera).getAdView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                //
            default:
                Newsholder holder = (Newsholder) holdera;
                newsmodel = (Newsmodel) list.get(position);
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

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }

    }
