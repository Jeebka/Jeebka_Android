package edu.escuelaing.ieti.jeebka.GroupsView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import edu.escuelaing.ieti.jeebka.GroupDetailsView.GroupDetailsActivity;

public class GroupsViewActivity extends AppCompatActivity implements UpdateRecyclerView{

    private RecyclerView dynamicRecyclerView, staticRecyclerView;
    StaticRvAdapter staticRvAdapter;
    ArrayList<DynamicGroupRvModel> items = new ArrayList();
    DynamicRvAdapter dynamicRvAdapter;
    Activity activity;
    int pos;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
        activity = this;
        Intent intent = getIntent();
        String username = intent.getStringExtra("UserLogged");
        User test = (new Gson()).fromJson(username, User.class);
        usernameText = findViewById(R.id.username_field);
        usernameText.setText(test.getName() + "!");
        final ArrayList<StatiGroupTypeRvModel> groupTypes = new ArrayList<>();
        groupTypes.add(new StatiGroupTypeRvModel(R.drawable.own_groups, "Mis grupos"));
        groupTypes.add(new StatiGroupTypeRvModel(R.drawable.suggested_group, "Grupos Sugeridos"));
        staticRecyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(groupTypes, this, this);
        staticRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        staticRecyclerView.setAdapter(staticRvAdapter);

        dynamicRecyclerView = findViewById(R.id.rv_2) ;
        dynamicRvAdapter = new DynamicRvAdapter(items);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvAdapter);

    }

    @Override
    public void callBack(int position, ArrayList<DynamicGroupRvModel> items) {
        dynamicRvAdapter = new DynamicRvAdapter(items);
        dynamicRvAdapter.notifyDataSetChanged();
        dynamicRecyclerView.setAdapter(dynamicRvAdapter);

        dynamicRvAdapter.setOnItemClickListener(new DynamicRvAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                pos = items.get(position).getPos();
                Intent intent = new Intent(activity, GroupDetailsActivity.class);
                intent.putExtra("pos", pos);
                intent.putExtra("color", items.get(position).getColor());
                intent.putExtra("name", items.get(position).getName());
                intent.putExtra("description", items.get(position).getDescription());
                intent.putExtra("public", items.get(position).isPublic());
                intent.putExtra("members", items.get(position).getMembers().size());
                intent.putExtra("links", items.get(position).getLinks().size());
                startActivity(intent);
            }
        });
    }
}