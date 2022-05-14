package edu.escuelaing.ieti.jeebka;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.escuelaing.ieti.jeebka.GroupsView.DynamicGroupRvModel;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import edu.escuelaing.ieti.jeebka.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateLinkActivity extends Activity {

    EditText linkName, linkUrl;
    FloatingActionButton createButton;
    Retrofit retrofit;
    JeebkaApi api;
    User loggedUser;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_link);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        activity = this;
        getWindow().setLayout((int) (width*.8), (int)(height*.8));
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

        linkName = findViewById(R.id.link_name);
        linkUrl = findViewById(R.id.link_url);
        createButton = findViewById(R.id.create_link_button);
        getUserGroups();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createLink();
            }
        });
    }

    private void getUserGroups(){
        try{
            String userEmail = loggedUser.getEmail();
            Call<List<Group>> callOwnGroups = api.getUsersGroups(userEmail);
            callOwnGroups.enqueue(new Callback<List<Group>>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<List<Group>> callOwnGroups, Response<List<Group>> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    List<Group> userGroups = response.body();
                    settingUpAutoCompleteGroupsView(userGroups);
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

    private void settingUpAutoCompleteGroupsView(List<Group> userGroups){
        List<String> userGroupNames =  new ArrayList<>();
        for (Group group: userGroups) {
            userGroupNames.add(group.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.drop_down_item, userGroupNames);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.group_autocomplete_items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(activity, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void createLink(){
        try{
            Call<Link> link = api.createLink("email", "groupName");
            link.enqueue(new Callback<Link>() {
                @Override
                public void onResponse(Call<Link> call, Response<Link> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                }

                @Override
                public void onFailure(Call<Link> call, Throwable t) {
                    Log.i("Failure", t.getMessage());
                }
            });

        } catch (Exception e){
            Log.i("Failure", e.getMessage());
        }
    }
}