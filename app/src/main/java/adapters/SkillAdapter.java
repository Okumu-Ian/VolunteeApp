package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eeyan.icelabs.bigman.volunteeapp.R;
import models.SkillModel;

/**
 * Created by The Architect on 6/16/2018.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MainHolder>{

    private List<SkillModel> modelList;
    private Context context;
    private Activity activity;
    private View view;
    private LayoutInflater inflater;

    public SkillAdapter(List<SkillModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return myHolder(parent);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {

        SkillModel model = modelList.get(position);
        Picasso.get().load(model.getSkill_image_local()).into(holder.mainImage);
        holder.mainText.setText(model.getSkill_name());
        holder.compatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
            }
        });

    }
    private MainHolder myHolder(ViewGroup parent)
    {
        view = LayoutInflater.from(context).inflate(R.layout.skillset,parent,false);
        return new MainHolder(view);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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
