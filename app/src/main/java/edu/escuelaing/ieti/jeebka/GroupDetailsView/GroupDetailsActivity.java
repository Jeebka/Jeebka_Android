package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import java.util.ArrayList;
import edu.escuelaing.ieti.jeebka.CreateViews.CreateLinkActivity;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupDetailsActivity extends AppCompatActivity {

    private RecyclerView dynamicRecyclerView;
    ImageView backgroundImage, publicImageIcon;
    TextView groupNameText, groupDescriptionText, groupMembersText, groupPublicText, groupLinksText;
    ArrayList<DynamicLinksRvModel> items = new ArrayList();
    DynamicGroupDetailsRvAdapter dynamicRvResAdapter;
    FloatingActionButton addingButton;
    int pos;
    User user;
    Group group;
    Activity activity;
    Retrofit retrofit;
    JeebkaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group_details);
        settingUpComponents();
    }

    private void settingUpComponents(){
        activity = this;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        group = (new Gson()).fromJson(intent.getStringExtra("CurrentGroup"), Group.class);
        user = (new Gson()).fromJson(intent.getStringExtra("LoggedUser"), User.class);
        getGroup();

        backgroundImage =  findViewById(R.id.imageView);
        backgroundImage.setColorFilter(Color.parseColor(group.getColor()), PorterDuff.Mode.SRC_IN);

        groupNameText = findViewById(R.id.group_name);
        groupNameText.setText(group.getName());

        groupDescriptionText = findViewById(R.id.group_description);
        groupDescriptionText.setText(group.getDescription());

        addingButton = findViewById(R.id.create_link_button);
        addingButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(group.getColor())));
        addingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerCreateLinkActivity(null);
            }
        });

    }

    private void getGroup(){
        try{
            String userEmail = user.getEmail();
            Call<Group> userGroup = api.getGroup(userEmail, group.getName());
            userGroup.enqueue(new Callback<Group>() {
                @Override
                public void onResponse(Call<Group> call, Response<Group> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    group = response.body();
                    group.setPublic(response.body().isPublic());
                    settingUpConditionalComponents();
                }

                @Override
                public void onFailure(Call<Group> call, Throwable t) {
                    Log.i("Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            Log.i("Failure", e.getMessage());
        }
    }

    public void triggerCreateLinkActivity(Link link){
        Intent intent = new Intent(GroupDetailsActivity.this, CreateLinkActivity.class);
        intent.putExtra("PreviousActivity", "GroupDetailsActivity");
        intent.putExtra("LoggedUser", (new Gson()).toJson(user));
        intent.putExtra("CurrentGroup", (new Gson()).toJson(group));
        if(link != null) {
            intent.putExtra("CurrentLink", (new Gson()).toJson(link));
        }
        startActivity(intent);
    }



    private void settingUpConditionalComponents(){
        groupMembersText = findViewById(R.id.members_text);
        if(group.getMembers().size() == 1){
            groupMembersText.setText(group.getMembers().size() + " Miembro");
        } else {
            groupMembersText.setText(group.getMembers().size() + " Miembros");
        }
        groupPublicText = findViewById(R.id.public_text);
        publicImageIcon = findViewById(R.id.public_icon);
        publicImageIcon.setVisibility(View.VISIBLE);
        if(group.isPublic()){
            groupPublicText.setText("Publico");
            publicImageIcon.setImageResource(R.drawable.public_group);
        } else {
            groupPublicText.setText("Privado");
            publicImageIcon.setImageResource(R.drawable.private_group);
        }
        groupLinksText = findViewById(R.id.number);
        if(group.getLinks().size() == 1){
            groupLinksText.setText(group.getLinks().size() + " Link");
        } else {
            groupLinksText.setText(group.getLinks().size() + " Links");
        }
        settingUpAdapter();
    }

    private void settingUpAdapter(){
        dynamicRecyclerView = findViewById(R.id.rv_2) ;
        for (Link link : group.getLinks()){
            items.add(new DynamicLinksRvModel(link, pos));
        }
        dynamicRvResAdapter = new DynamicGroupDetailsRvAdapter(items, group, this);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvResAdapter);
    }

}