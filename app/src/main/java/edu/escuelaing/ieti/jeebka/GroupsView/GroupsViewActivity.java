package edu.escuelaing.ieti.jeebka.GroupsView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.CreateGroupActivity;
import edu.escuelaing.ieti.jeebka.CreateLinkActivity;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import edu.escuelaing.ieti.jeebka.GroupDetailsView.GroupDetailsActivity;

public class GroupsViewActivity extends AppCompatActivity implements UpdateRecyclerView{

    private RecyclerView dynamicRecyclerView, staticRecyclerView;
    StaticRvAdapter staticRvAdapter;
    ArrayList<DynamicGroupRvModel> items = new ArrayList();
    DynamicRvAdapter dynamicRvAdapter;
    Activity activity;
    User loggedUser;
    int pos;
    TextView usernameText;
    FloatingActionButton createGroup, createLink;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
        activity = this;
        settingUpView();
    }


    private void settingUpView(){
        Intent intent = getIntent();
        loggedUser = (new Gson()).fromJson(intent.getStringExtra("LoggedUser"), User.class);
        usernameText = findViewById(R.id.username_field);
        createGroup = findViewById(R.id.create_group_button);
        createLink = findViewById(R.id.create_link_button);
        usernameText.setText(loggedUser.getName() + "!");
        settingUpListeners();
        settingUpAdapters();

    }

    private void settingUpListeners(){
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerCreateGroupActivity();
            }
        });
        createLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               triggerCreateLinkActivity();
            }
        });
    }

    private void triggerCreateGroupActivity(){
        Intent intent = new Intent(GroupsViewActivity.this, CreateGroupActivity.class);
        intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
        startActivity(intent);
    }

    private void triggerCreateLinkActivity(){
        Intent intent = new Intent(GroupsViewActivity.this, CreateLinkActivity.class);
        intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
        startActivity(intent);
    }

    private void settingUpAdapters(){
        final ArrayList<StatiGroupTypeRvModel> groupTypes = new ArrayList<>();
        groupTypes.add(new StatiGroupTypeRvModel(R.drawable.own_groups, "Mis grupos"));
        groupTypes.add(new StatiGroupTypeRvModel(R.drawable.suggested_group, "Grupos Sugeridos"));
        staticRecyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(groupTypes, this, this, loggedUser);
        staticRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        staticRecyclerView.setAdapter(staticRvAdapter);

        dynamicRecyclerView = findViewById(R.id.rv_2) ;
        dynamicRvAdapter = new DynamicRvAdapter(items, loggedUser);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvAdapter);
    }

    @Override
    public void callBack(int position, ArrayList<DynamicGroupRvModel> items) {
        dynamicRvAdapter = new DynamicRvAdapter(items, loggedUser);
        dynamicRvAdapter.notifyDataSetChanged();
        dynamicRecyclerView.setAdapter(dynamicRvAdapter);

        dynamicRvAdapter.setOnItemClickListener(new DynamicRvAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                pos = items.get(position).getPos();
                Intent intent = new Intent(activity, GroupDetailsActivity.class);
                intent.putExtra("CurrentGroup", (new Gson()).toJson(items.get(position)));
                intent.putExtra("pos", pos);
                startActivity(intent);
            }
        });
    }
}