package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;

public class GroupDetailsActivity extends AppCompatActivity {

    private RecyclerView dynamicRecyclerView;
    ImageView backgroundImage, publicImageIcon;
    TextView groupNameText, groupDescriptionText, groupMembersText, groupPublicText, groupLinksText;
    ArrayList<DynamicLinksRvModel> items = new ArrayList();
    DynamicGroupDetailsRvAdapter dynamicRvResAdapter;
    int pos;
    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group_details);
        settingUpComponents();
        dynamicRecyclerView = findViewById(R.id.rv_2) ;
        items.add(new DynamicLinksRvModel("Link1", "test url", pos));
        dynamicRvResAdapter = new DynamicGroupDetailsRvAdapter(items, group, this);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvResAdapter);
    }

    private void settingUpComponents(){
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        group = (new Gson()).fromJson(intent.getStringExtra("CurrentGroup"), Group.class);

        backgroundImage =  findViewById(R.id.imageView);
        backgroundImage.setColorFilter(Color.parseColor(group.getColor()), PorterDuff.Mode.SRC_IN);

        groupNameText = findViewById(R.id.group_name);
        groupNameText.setText(group.getName());

        groupDescriptionText = findViewById(R.id.group_description);
        groupDescriptionText.setText(group.getDescription());

        settingUpConditionalComponents();
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
    }
}