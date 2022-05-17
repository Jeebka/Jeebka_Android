package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.escuelaing.ieti.jeebka.CreateViews.CreateLinkActivity;
import edu.escuelaing.ieti.jeebka.GroupsView.DynamicGroupRvModel;
import edu.escuelaing.ieti.jeebka.GroupsView.DynamicRvAdapter;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import edu.escuelaing.ieti.jeebka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DynamicGroupDetailsRvAdapter extends  RecyclerView.Adapter<DynamicGroupDetailsRvAdapter.DynamicRvResHolder>{

    public ArrayList<DynamicLinksRvModel> dynamicLinksRVModels;
    int p;
    boolean check;
    Group group;
    Retrofit retrofit;
    JeebkaApi api;
    GroupDetailsActivity activity;
    UpdateLinkRecyclerView updateLinkRecyclerView;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClickItem();
    }

    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener = mListener;
    }

    public DynamicGroupDetailsRvAdapter(ArrayList<DynamicLinksRvModel> dynamicLinksRVModels, Group group, GroupDetailsActivity activity, UpdateLinkRecyclerView updateLinkRecyclerView){
        this.dynamicLinksRVModels = dynamicLinksRVModels;
        this.group = group;
        this.activity = activity;
        this.updateLinkRecyclerView = updateLinkRecyclerView;
        check = true;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
    }

    public class DynamicRvResHolder extends RecyclerView.ViewHolder {
        public TextView linkName, linkUrl;
        ChipGroup tagsContainer;
        ConstraintLayout constraintLayout;
        ImageView closeImage;
        public DynamicRvResHolder(@NonNull View itemView) {
            super(itemView);
            linkName = itemView.findViewById(R.id.link_name);
            linkUrl = itemView.findViewById(R.id.link_url);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            tagsContainer = itemView.findViewById(R.id.tags_container);
            closeImage = itemView.findViewById(R.id.close_icon);
        }
    }

    public ArrayList<DynamicLinksRvModel> getItems(){
        return dynamicLinksRVModels;
    }

    @NonNull
    @Override
    public DynamicRvResHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_rv_item_layout, parent, false);
        DynamicGroupDetailsRvAdapter.DynamicRvResHolder dynamicRvResHolder = new DynamicGroupDetailsRvAdapter.DynamicRvResHolder(view);
        return dynamicRvResHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicRvResHolder holder, int position) {
        DynamicLinksRvModel currentItem = dynamicLinksRVModels.get(position);

        holder.linkName.setText(currentItem.getName());
        holder.linkUrl.setText(currentItem.getUrl());
        for(String tagName : currentItem.getTags()){
            Chip chip =  new Chip(activity);
            chip.setText(tagName);
            chip.setCheckable(false);
            chip.setClickable(false);
            holder.tagsContainer.addView(chip);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tagsContainer.removeView(view);
                }
            });
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Link link = new Link(currentItem);
                triggerCreateLinkActivity(link);
            }
        });
        if(currentItem.getPos() == 0){
            holder.closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateLinkRecyclerView.callBack(0, dynamicLinksRVModels,currentItem);
                }
            });
        } else{
            holder.closeImage.setVisibility(View.INVISIBLE);
        }
        p = currentItem.getPos();
    }

    private void triggerCreateLinkActivity(Link link){
        activity.triggerCreateLinkActivity(link);
    }

    @Override
    public int getItemCount() {
        return dynamicLinksRVModels.size();
    }

}
