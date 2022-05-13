package edu.escuelaing.ieti.jeebka.GroupsView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.LoginResponse;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaticRvAdapter  extends  RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder> {
    private ArrayList<StatiGroupTypeRvModel> items;
    private ArrayList<DynamicGroupRvModel> dynamicItems;
    int row_index = -1;
    UpdateRecyclerView updateRecyclerView;
    Activity activity;
    boolean check = true;
    boolean select = true;
    User user;
    Retrofit retrofit;
    JeebkaApi api;

    public StaticRvAdapter(ArrayList<StatiGroupTypeRvModel> items, Activity activity, UpdateRecyclerView updateRecyclerView, User user) {
        this.items = items;
        this.activity = activity;
        this.updateRecyclerView = updateRecyclerView;
        this.user = user;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
    }

    public class StaticRVViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        ImageView imageView;
        LinearLayout linearLayout;

        public StaticRVViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            nameText = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    @NonNull
    @Override
    public StaticRvAdapter.StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item, parent, false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRvAdapter.StaticRVViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        StatiGroupTypeRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.nameText.setText(currentItem.getText());
        if (check) {
            callUserGroups(0);
            check = false;
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                if (position == 0) {
                    callUserGroups(position);
                } else if (position == 1) {
                    callPublicGroups(position);
                }
            }
        });

        if (select) {
            if (position == 0)
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            select = false;
        } else {
            if (row_index == position) {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
            } else {
                holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void callUserGroups(int pos){
        ArrayList<DynamicGroupRvModel> parsedItems = new ArrayList<>();
        try{
            Call<List<Group>> callOwnGroups = api.getUsersGroups(user.email);
            callOwnGroups.enqueue(new Callback<List<Group>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<Group>> callOwnGroups, Response<List<Group>> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    for(Group group : response.body()){
                        parsedItems.add(new DynamicGroupRvModel(group, pos));
                    }
                    updateRecyclerView.callBack(pos, parsedItems);

                }

                @Override
                public void onFailure(Call<List<Group>> callOwnGroups, Throwable t) {
                    Log.i("Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            Log.i("Failure", e.getMessage());
        }
    }

    private void callPublicGroups(int pos){
        ArrayList<DynamicGroupRvModel> parsedItems = new ArrayList<>();
        Call<Map<Group, Integer>> callPublicGroups = api.showPublicGroups(user.email);
        callPublicGroups.enqueue(new Callback<Map<Group, Integer>>() {
            @Override
            public void onResponse(Call<Map<Group, Integer>> call, Response<Map<Group, Integer>> response) {
                if(!response.isSuccessful()){
                    Log.i("Not successful", response.code() + "");
                    return;
                }
                Log.i("Info", response.body().size() + "");
                for (Group group : response.body().keySet()){
                    Log.i("Info", group.toString());
                }
                updateRecyclerView.callBack(pos, parsedItems);
            }

            @Override
            public void onFailure(Call<Map<Group, Integer>> call, Throwable t) {
                Log.i("Failure", t.getMessage());
            }
        });
    }

}
