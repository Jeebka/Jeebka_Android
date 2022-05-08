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

import java.util.ArrayList;

import edu.escuelaing.ieti.jeebka.R;

public class GroupDetailsActivity extends AppCompatActivity {

    private RecyclerView dynamicRecyclerView;
    ImageView backgroundImage, publicImageIcon;
    TextView groupNameText, groupDescriptionText, groupMembersText, groupPublicText, groupLinksText;
    ArrayList<DynamicLinksRvModel> items = new ArrayList();
    DynamicGroupDetailsRvAdapter dynamicRvResAdapter;
    int pos, groupMembers, groupLinks;
    String backgroundColor, groupName, groupDescription;
    boolean isPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_group_details);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        backgroundColor = intent.getStringExtra("color");
        groupName = intent.getStringExtra("name");
        groupDescription = intent.getStringExtra("description");
        isPublic = intent.getBooleanExtra("public", true);
        groupMembers = intent.getIntExtra("members", 0);
        groupLinks = intent.getIntExtra("links", 0);

        backgroundImage =  findViewById(R.id.imageView);
        backgroundImage.setColorFilter(Color.parseColor(backgroundColor), PorterDuff.Mode.SRC_IN);

        groupNameText = findViewById(R.id.group_name);
        groupNameText.setText(groupName);

        groupDescriptionText = findViewById(R.id.group_description);
        groupDescriptionText.setText(groupDescription);

        groupMembersText = findViewById(R.id.members_text);
        if(groupMembers == 1){
            groupMembersText.setText(groupMembers + " Miembro");
        } else {
            groupMembersText.setText(groupMembers + " Miembros");
        }
        groupPublicText = findViewById(R.id.public_text);
        publicImageIcon = findViewById(R.id.public_icon);
        if(isPublic){
            groupPublicText.setText("Publico");
            publicImageIcon.setImageResource(R.drawable.public_group);
        } else {
            groupPublicText.setText("Privado");
            publicImageIcon.setImageResource(R.drawable.private_group);
        }
        groupLinksText = findViewById(R.id.number);
        if(groupLinks == 1){
            groupLinksText.setText(groupLinks + " Link");
        } else {
            groupLinksText.setText(groupLinks + " Links");
        }

        items.add(new DynamicLinksRvModel("Link 1", "http:google.com", pos));
        items.add(new DynamicLinksRvModel("Link 2", "http:youtube.com", pos));
        items.add(new DynamicLinksRvModel("Link 3", "http:google.com", pos));
        items.add(new DynamicLinksRvModel("Link 4", "http:youtube.com", pos));
        items.add(new DynamicLinksRvModel("Link 5", "http:google.com", pos));
        items.add(new DynamicLinksRvModel("Link 6", "http:youtube.com", pos));
        items.add(new DynamicLinksRvModel("Link 7", "http:google.com", pos));
        items.add(new DynamicLinksRvModel("Link 8", "http:youtube.com", pos));
        items.add(new DynamicLinksRvModel("Link 9", "http:google.com", pos));
        items.add(new DynamicLinksRvModel("Link 10", "http:youtube.com", pos));

        dynamicRecyclerView = findViewById(R.id.rv_2) ;
        dynamicRvResAdapter = new DynamicGroupDetailsRvAdapter(items);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvResAdapter);
    }
}