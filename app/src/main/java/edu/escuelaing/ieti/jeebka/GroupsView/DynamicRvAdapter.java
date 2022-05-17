package edu.escuelaing.ieti.jeebka.GroupsView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;

public class DynamicRvAdapter extends  RecyclerView.Adapter<DynamicRvAdapter.DynamicRvHolder>{
    public ArrayList<DynamicGroupRvModel> dynamicRVModels;
    UpdateRecyclerView updateRecyclerView;
    public User user;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClickItem(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener = mListener;
    }

    public DynamicRvAdapter(ArrayList<DynamicGroupRvModel> dynamicRVModels, User user, UpdateRecyclerView updateRecyclerView){
        this.dynamicRVModels = dynamicRVModels;
        this.user = user;
        this.updateRecyclerView = updateRecyclerView;
    }

    public ArrayList<DynamicGroupRvModel> getItems(){
        return dynamicRVModels;
    }

    public class DynamicRvHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, closeImage;
        public TextView nameText, detailsText;
        ConstraintLayout constraintLayout;
        public DynamicRvHolder(@NonNull View itemView, final OnItemClickListener mListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            nameText = itemView.findViewById(R.id.name);
            detailsText = itemView.findViewById(R.id.details);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            closeImage = itemView.findViewById(R.id.close_icon);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAbsoluteAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onClickItem(position);
                        }
                    }
                }
            });
        }
    }


    @NonNull
    @Override
    public DynamicRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_rv_item_layout, parent, false);
        DynamicRvHolder dynamicRvHolder = new DynamicRvHolder(view, mListener);
        return dynamicRvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicRvHolder holder, int position) {
        DynamicGroupRvModel currentItem = dynamicRVModels.get(position);
        holder.imageView.setColorFilter(Color.parseColor(currentItem.getColor()), PorterDuff.Mode.SRC_IN);
        holder.nameText.setText(currentItem.getName());
        if(currentItem.getLinks().size() != 1){
            holder.detailsText.setText(currentItem.getLinks().size() + " Links");
        } else{
            holder.detailsText.setText(currentItem.getLinks().size() + " Link");
        }
        if(currentItem.getPos() == 0){
            holder.closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateRecyclerView.callBack(0, dynamicRVModels,currentItem);
                }
            });
        } else{
            holder.closeImage.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return dynamicRVModels.size();
    }
}
