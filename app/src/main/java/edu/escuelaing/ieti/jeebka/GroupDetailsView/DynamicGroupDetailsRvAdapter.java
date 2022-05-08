package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.R;

public class DynamicGroupDetailsRvAdapter extends  RecyclerView.Adapter<DynamicGroupDetailsRvAdapter.DynamicRvResHolder>{

    public ArrayList<DynamicLinksRvModel> dynamicLinksRVModels;
    int p;
    boolean check;
    public DynamicGroupDetailsRvAdapter(ArrayList<DynamicLinksRvModel> dynamicLinksRVModels){
        this.dynamicLinksRVModels = dynamicLinksRVModels;
    }

    public class DynamicRvResHolder extends RecyclerView.ViewHolder {
        public TextView linkName, linkUrl;

        ConstraintLayout constraintLayout;
        public DynamicRvResHolder(@NonNull View itemView) {
            super(itemView);
            linkName = itemView.findViewById(R.id.link_name);
            linkUrl = itemView.findViewById(R.id.link_url);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    @NonNull
    @Override
    public DynamicRvResHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_rv_item_layout, parent, false);
        Log.i("ONCreate", dynamicLinksRVModels.size() + "");
        DynamicGroupDetailsRvAdapter.DynamicRvResHolder dynamicRvResHolder = new DynamicGroupDetailsRvAdapter.DynamicRvResHolder(view);
        return dynamicRvResHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicRvResHolder holder, int position) {
        DynamicLinksRvModel currentItem = dynamicLinksRVModels.get(position);
        holder.linkName.setText(currentItem.getName());
        holder.linkUrl.setText(currentItem.getUrl());

        p = currentItem.getPos();
        if(check){
            if(p==0){
                dynamicLinksRVModels = new ArrayList<>();
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 1", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 2", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 3", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 4", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 5", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 6", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 7", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 8", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 9", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 10", "http:youtube.com", p));
                notifyDataSetChanged();
            } else if(p==1){
                dynamicLinksRVModels = new ArrayList<>();
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 10", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 20", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 30", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 40", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 50", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 60", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 70", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 80", "http:youtube.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 90", "http:google.com", p));
                dynamicLinksRVModels.add(new DynamicLinksRvModel("Link 100", "http:youtube.com", p));
                notifyDataSetChanged();
            }
            check = false;
        }
    }

    @Override
    public int getItemCount() {
        return dynamicLinksRVModels.size();
    }
}
