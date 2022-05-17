package edu.escuelaing.ieti.jeebka.CreateViews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.escuelaing.ieti.jeebka.DTOs.LinkDto;
import edu.escuelaing.ieti.jeebka.DTOs.TaggedLinkDto;
import edu.escuelaing.ieti.jeebka.GroupDetailsView.GroupDetailsActivity;
import edu.escuelaing.ieti.jeebka.GroupsView.DynamicGroupRvModel;
import edu.escuelaing.ieti.jeebka.GroupsView.GroupsViewActivity;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import edu.escuelaing.ieti.jeebka.Models.LinkUpdateRequest;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
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
    Group group;
    Link link;
    String previousActivityName, actionName;
    Activity activity;
    TextInputLayout tagsLayout;
    ChipGroup tagsContainer;
    AutoCompleteTextView autoCompleteTagsList, autoCompleteGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_link);
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
        group =  (new Gson()).fromJson(intent.getStringExtra("CurrentGroup"), Group.class);
        link = (new Gson()).fromJson(intent.getStringExtra("CurrentLink"), Link.class);
        previousActivityName = intent.getStringExtra("PreviousActivity");
        actionName = intent.getStringExtra("Action");

        settingUpComponents();
        triggerActions();

    }

    private void settingUpComponents(){
        linkName = findViewById(R.id.link_name);
        linkUrl = findViewById(R.id.link_url);
        tagsLayout = findViewById(R.id.tags_drop_down);
        autoCompleteTagsList = findViewById(R.id.tags_autocomplete_items);
        autoCompleteGroupsList = findViewById(R.id.group_autocomplete_items);
        tagsContainer = findViewById(R.id.
                tags_container);
        createButton = findViewById(R.id.create_link_button);
        if(actionName != null && actionName.equals("Update")){
            TextView viewName = findViewById(R.id.view_name);
            viewName.setText("Actualizar Link");
            linkUrl.setEnabled(false);
            autoCompleteGroupsList.setEnabled(false);

        }
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionName != null && actionName.equals("Update")){
                    updateLink();
                } else{
                    createLink();
                }

            }
        });
    }

    private void triggerActions(){
        if(link != null){
            linkName.setText(link.getName());
            linkUrl.setText(link.getUrl());
            for(String tag : link.getTags()){
                createChip(tag);
            }
        }
        getUserTags();
        if(group != null){
            autoCompleteGroupsList.setEnabled(false);
            autoCompleteGroupsList.setText(group.getName());
        }else{ getUserGroups();}
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
        autoCompleteGroupsList.setAdapter(adapter);
    }

    private void getUserTags(){
        try{
            String userEmail = loggedUser.getEmail();
            Call<HashSet<String>> userTags = api.getUserTags(userEmail);
            userTags.enqueue(new Callback<HashSet<String>>() {
                @Override
                public void onResponse(Call<HashSet<String>> call, Response<HashSet<String>> response) {
                    if(!response.isSuccessful()){
                        return;
                    }
                    settingUpAutoCompleteTagsView(response.body());

                }

                @Override
                public void onFailure(Call<HashSet<String>> call, Throwable t) {
                    Log.i("Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            Log.i("Failure", e.getMessage());
        }
    }

    private void settingUpAutoCompleteTagsView(HashSet<String> userTags){
        List<String> userTagsNames =  new ArrayList<>();
        for (String tag: userTags) {
            userTagsNames.add(tag);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.drop_down_item, userTagsNames);
        autoCompleteTagsList.setAdapter(adapter);
        autoCompleteTagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tagName = String.valueOf(autoCompleteTagsList.getText());
                autoCompleteTagsList.setText("");
                createChip(tagName);
            }
        });
        autoCompleteTagsList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String tagName = String.valueOf(autoCompleteTagsList.getText());
                    autoCompleteTagsList.setText("");
                    createChip(tagName);
                    return true;
                }
                return false;
            }
        });
    }

    private void createChip(String name){
        boolean alreadyInGroup =  false;
        for (int i = 0; i < tagsContainer.getChildCount();i++){
            Chip chip = (Chip)tagsContainer.getChildAt(i);
            if (chip.getText().toString().equals(name)){
                alreadyInGroup = true;
                break;
            }
        }
        if(!alreadyInGroup){
            Chip chip =  new Chip(activity);
            chip.setText(name);
            chip.setCloseIconVisible(true);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setTextSize(15);
            tagsContainer.addView(chip);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tagsContainer.removeView(view);
                }
            });
            tagsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void updateLink(){
        Log.i("Update", "update Link");
        List<String> tags = getTagsFromChips();
        LinkUpdateRequest request = new LinkUpdateRequest(linkName.getText().toString(), tags);
        Call<List<Link>> call = api.updateLink(loggedUser.getEmail(), group.getName(), link.getName(), request);
        call.enqueue(new Callback<List<Link>>() {
            @Override
            public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {
                if(!response.isSuccessful()){
                    Log.i("Not successful BaseLink", response.code() + "");
                    return;
                }
                startPreviousActivity();
            }

            @Override
            public void onFailure(Call<List<Link>> call, Throwable t) {
                Log.i("Update Link Failure", t.getMessage());
            }
        });
    }
    private void createLink(){
        String linkNameString =  String.valueOf(linkName.getText()), linkUrlString = String.valueOf(linkUrl.getText());
        String groupName = String.valueOf(autoCompleteGroupsList.getText());
        String userEmail =  String.valueOf(loggedUser.getEmail());
        if(!linkNameString.equals("") && !linkUrlString.equals("") && !groupName.equals("")){
            List<String> tags = getTagsFromChips();
            LinkDto baseLink = new LinkDto(linkUrlString, linkNameString);
            TaggedLinkDto linkTags = new TaggedLinkDto(linkUrlString, linkNameString, tags);
            if (tags.size() > 0) {
                createLinkTags(linkTags, userEmail, groupName);
            } else {
                createBaseLink(baseLink, userEmail, groupName);
            }


        }
    }

    private List<String> getTagsFromChips(){
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < tagsContainer.getChildCount();i++){
            Chip chip = (Chip)tagsContainer.getChildAt(i);
            tags.add(chip.getText().toString());
        }
        return tags;
    }

    private void createBaseLink(LinkDto baseLink, String userEmail, String groupName){
        Call<LinkDto> call = api.createLinkNoTags(baseLink, userEmail, groupName);
        call.enqueue(new Callback<LinkDto>() {
            @Override
            public void onResponse(Call<LinkDto> call, Response<LinkDto> response) {
                if(!response.isSuccessful()){
                    Log.i("Not successful BaseLink", response.code() + "");
                    return;
                }
                Log.i("Created", "Base Link created");
                startPreviousActivity();
            }

            @Override
            public void onFailure(Call<LinkDto> call, Throwable t) {
                Log.i("Create BaseLink Failure", t.getMessage());
            }
        });
    }

    private void createLinkTags(TaggedLinkDto linkTags, String userEmail, String groupName){
        Call<TaggedLinkDto> call = api.createLinkTags(linkTags, userEmail, groupName);
        call.enqueue(new Callback<TaggedLinkDto>() {
            @Override
            public void onResponse(Call<TaggedLinkDto> call, Response<TaggedLinkDto> response) {
                if(!response.isSuccessful()){
                    Log.i("Not successful TagLink", response.code() + "");
                    return;
                }
                Log.i("Created", "Tagged Link created");
                startPreviousActivity();
            }

            @Override
            public void onFailure(Call<TaggedLinkDto> call, Throwable t) {
                Log.i("Create Tag Link Failure", t.getMessage());
            }
        });
    }

    private void startPreviousActivity(){
        if(previousActivityName.equals("GroupsView")){
            Intent intent = new Intent(CreateLinkActivity.this, GroupsViewActivity.class);
            intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
            startActivity(intent);
        } else if(previousActivityName.equals("GroupDetailsActivity")){
            Intent intent2 = new Intent(CreateLinkActivity.this, GroupDetailsActivity.class);
            intent2.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
            intent2.putExtra("CurrentGroup", (new Gson()).toJson(group));
            startActivity(intent2);
        }

    }


}