package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import eeyan.icelabs.bigman.volunteeapp.R;
import models.SkillModel;

/**
 * Created by The Architect on 6/16/2018.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MainHolder> implements Filterable{

    private List<SkillModel> modelList;
    private Context context;
    private Activity activity;
    private View view;
    private List<SkillModel> sortedList;
    private List<SkillModel> selectedList;
    private OnClickAction onClickAction;

    public SkillAdapter(List<SkillModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
        this.sortedList = modelList;
        this.selectedList = new ArrayList<>();
    }

    public interface OnClickAction
    {
         void OnClickAction();
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return myHolder(parent);
    }

    @Override
    public void onBindViewHolder(final MainHolder holder, int position) {

        final SkillModel model = sortedList.get(position);
        Picasso.get().load(model.getSkill_image_local()).into(holder.mainImage);
        holder.mainText.setText(model.getSkill_name());
        holder.compatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedList.contains(model))
                {
                    selectedList.remove(model);
                    unhighlightSelectedItem(holder);
                }else
                    {
                        selectedList.add(model);
                        highlightSelectedItem(holder);
                    }

                    onClickAction.OnClickAction();
            }
        });
        if (selectedList.contains(model))
            highlightSelectedItem(holder);
        else
            unhighlightSelectedItem(holder);

    }
    private MainHolder myHolder(ViewGroup parent)
    {
        view = LayoutInflater.from(context).inflate(R.layout.skillset,parent,false);
        return new MainHolder(view);
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

    public interface SkillAdapterList
    {
        void onSkillSelected(SkillModel model);
    }

    private void highlightSelectedItem(MainHolder holder)
    {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.cardview_dark_background));
    }

    private void unhighlightSelectedItem(MainHolder holder)
    {
            holder.linearLayout.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
    }

    private void clearAll(boolean isNotify)
    {
        sortedList.clear();
        selectedList.clear();
        modelList.clear();
        if (isNotify) notifyDataSetChanged();

    }

    private void clearSelection()
    {
        selectedList.clear();
        notifyDataSetChanged();
    }

    private void addALl(List<SkillModel> models)
    {
        clearAll(false);
        this.sortedList = models;
        notifyDataSetChanged();
    }

    private List<SkillModel> models()
    {
        return selectedList;
    }

    private void setActionModeReceiver(OnClickAction action)
    {
            this.onClickAction = action;
    }

    public class MainHolder extends RecyclerView.ViewHolder
    {
        private ImageView mainImage;
        private TextView mainText;
        private AppCompatButton compatButton;
        private LinearLayout linearLayout;
        public MainHolder(View itemView) {
            super(itemView);
            mainImage = (ImageView) itemView.findViewById(R.id.background_image_skillset);
            mainText = (TextView) itemView.findViewById(R.id.txt_skill_name);
            compatButton = (AppCompatButton) itemView.findViewById(R.id.btn_skill_select);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.mainColor);
        }
    }

}
