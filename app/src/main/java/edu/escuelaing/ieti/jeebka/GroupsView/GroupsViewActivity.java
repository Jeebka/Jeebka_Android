package edu.escuelaing.ieti.jeebka.GroupsView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.escuelaing.ieti.jeebka.CreateViews.CreateGroupActivity;
import edu.escuelaing.ieti.jeebka.CreateViews.CreateLinkActivity;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.LoginView.LogInActivity;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import edu.escuelaing.ieti.jeebka.GroupDetailsView.GroupDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    EditText search;
    Retrofit retrofit;
    JeebkaApi api;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
        activity = this;
        settingUpView();
    }

    private void settingUpView(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
        Intent intent = getIntent();
        loggedUser = (new Gson()).fromJson(intent.getStringExtra("LoggedUser"), User.class);
        usernameText = findViewById(R.id.username_field);
        createGroup = findViewById(R.id.create_group_button);
        createLink = findViewById(R.id.create_link_button);
        search = findViewById(R.id.search);
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
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString();
                callUserGroups(0, userInput);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void callUserGroups(int pos, String userInput){
        ArrayList<DynamicGroupRvModel> parsedItems = new ArrayList<>();
        try{
            Call<List<Group>> callOwnGroups = api.getUsersGroups(loggedUser.email);
            callOwnGroups.enqueue(new Callback<List<Group>>() {
                @Override
                public void onResponse(Call<List<Group>> callOwnGroups, Response<List<Group>> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    for(Group group : response.body()){
                        if(group.getName().toLowerCase().contains(userInput.toLowerCase())){
                            parsedItems.add(new DynamicGroupRvModel(group, pos));
                        }
                    }
                    callBack(pos, parsedItems);
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

    private void triggerCreateGroupActivity(){
        Intent intent = new Intent(GroupsViewActivity.this, CreateGroupActivity.class);
        intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
        startActivity(intent);
    }

    private void triggerCreateLinkActivity(){
        Intent intent = new Intent(GroupsViewActivity.this, CreateLinkActivity.class);
        intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
        intent.putExtra("PreviousActivity", "GroupsView");
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
                intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
                startActivity(intent);
            }
        });
    }
}