package adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import eeyan.icelabs.bigman.volunteeapp.MainFeed;
import eeyan.icelabs.bigman.volunteeapp.NewsFeed;
import eeyan.icelabs.bigman.volunteeapp.R;
import models.SkillModel;

/**
 * Created by The Architect on 6/16/2018.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MainHolder> implements Filterable{

    private List<SkillModel> modelList;
    private Context context;
    private Activity myActivity;
    private View view;
    private List<SkillModel> sortedList;

    /*TODO
    1.Add Window transitions.
    2.Database design
   */

    public SkillAdapter(List<SkillModel> modelList, Context context, Activity myActivity) {
        this.modelList = modelList;
        this.context = context;
        this.sortedList = modelList;
        this.myActivity = myActivity;

    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return myHolder(parent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MainHolder holder, final int position) {

        final SkillModel model = sortedList.get(position);
        holder.mainImage.setClickable(true);
        Picasso.get().load(model.getSkill_image_local()).into(holder.mainImage);
        holder.mainText.setText(model.getSkill_name());
        holder.compatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });
        holder.mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog(myActivity,model.getSkill_name());

            }
        });

    }

    private MainHolder myHolder(ViewGroup parent)
    {
        view = LayoutInflater.from(context).inflate(R.layout.skillset,parent,false);
        return new MainHolder(view);
    }

    private void showDialog(final Activity mContext, final String TITLE)
    {
        String title = "Choose category?";
        String message = "Are you sure you want to choose "+TITLE+"?";
        String positiveBtn = "NO";
        String negativeBtn = "Yes";
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                putValueToPref(TITLE);
                setupTransitions();
                mContext.startActivity(new Intent(mContext, MainFeed.class));
                mContext.finish();
            }
        });
        builder.create();
        builder.show();
    }

    @RequiresApi(21)
    private void setupTransitions()
    {
        Slide slide = (Slide) TransitionInflater.from(myActivity).inflateTransition(R.transition.slide_it);
        myActivity.getWindow().setExitTransition(slide);

    }


    private void putValueToPref(String data)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_PREF_SKILL",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("my_skill",data);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString  = charSequence.toString();
                if (charString.isEmpty())
                {
                    sortedList = modelList;
                }else
                    {
                        List<SkillModel> myList = new ArrayList<>();
                        for (SkillModel model:modelList)
                        {
                            if (model.getSkill_name().toLowerCase().contains(charString))
                            {
                                myList.add(model);
                            }
                        }

                        sortedList = myList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = sortedList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sortedList = (ArrayList<SkillModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public class MainHolder extends RecyclerView.ViewHolder
    {
        private ImageView mainImage;
        private TextView mainText;
        private AppCompatButton compatButton;
        public MainHolder(View itemView) {
            super(itemView);
            mainImage = (ImageView) itemView.findViewById(R.id.background_image_skillset);
            mainText = (TextView) itemView.findViewById(R.id.txt_skill_name);
            compatButton = (AppCompatButton) itemView.findViewById(R.id.btn_skill_select);
            }
    }

}
