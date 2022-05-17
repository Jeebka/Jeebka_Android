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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
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
    EditText searchBar;
    TextInputLayout searchOptionsLayout;
    AutoCompleteTextView searchOptionsAutoCompleteText;
    ChipGroup searchItemsChipGroup;


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
                    settingUpSearchComponents();
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

    public void settingUpSearchComponents(){
        searchBar = findViewById(R.id.search);
        searchOptionsLayout = findViewById(R.id.options_drop_down);
        searchOptionsAutoCompleteText = findViewById(R.id.options_autocomplete_items);
        searchItemsChipGroup = findViewById(R.id.search_items_container);
        settingUpAutoCompleteOptionsView();

    }

    private void settingUpAutoCompleteOptionsView(){
        List<String> optionsNames =  new ArrayList<>();
        optionsNames.add("Nombre"); optionsNames.add("Tags"); optionsNames.add("Url");
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.drop_down_item, optionsNames);
        searchOptionsAutoCompleteText.setAdapter(adapter);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(searchOptionsAutoCompleteText.getText().toString().equals("Tags")){
                        createChip(searchBar.getText().toString());
                    }
                    triggerSearchAction(searchOptionsAutoCompleteText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void triggerSearchAction(String options){
        if(options.equals("Tags")){
            searchByTags();
        } else if(options.equals("Nombre")){
            searchByName();
        } else if(options.equals("Url")){
            searchByUrl();
        }
    }

    private void searchByTags(){
        try{
            String userEmail = user.getEmail(), groupName = group.getName();
            List<String> tagsToSearch = new ArrayList<>();
            for (int i = 0; i < searchItemsChipGroup.getChildCount();i++){
                Chip chip = (Chip)searchItemsChipGroup.getChildAt(i);
                tagsToSearch.add(chip.getText().toString());
            }
            Call<List<Link>> linksSearched = api.getLinksByTags(userEmail,groupName, tagsToSearch);
            linksSearched.enqueue(new Callback<List<Link>>() {
                @Override
                public void onResponse(Call<List<Link>> call, Response<List<Link>> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    settingUpAdapterSearch(response.body());
                }

                @Override
                public void onFailure(Call<List<Link>> call, Throwable t) {
                    Log.i("Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            Log.i("Failure", e.getMessage());
        }
    }

    private void searchByName(){
        List<Link> searchedLinks = new ArrayList<>();
        for(Link link : group.getLinks()){
            if(link.getName().toLowerCase().contains(searchBar.getText().toString().toLowerCase())){
                searchedLinks.add(link);
            }
        }
        settingUpAdapterSearch(searchedLinks);
    }

    private void searchByUrl(){
        List<Link> searchedLinks = new ArrayList<>();
        for(Link link : group.getLinks()){
            if(link.getUrl().toLowerCase().contains(searchBar.getText().toString().toLowerCase())){
                searchedLinks.add(link);
            }
        }
        settingUpAdapterSearch(searchedLinks);
    }

    private void settingUpAdapterSearch(List<Link> searchedLinks){
        items =  new ArrayList<>();
        for (Link link : searchedLinks){
            items.add(new DynamicLinksRvModel(link, pos));
        }
        dynamicRvResAdapter = new DynamicGroupDetailsRvAdapter(items, group, this);
        dynamicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicRecyclerView.setAdapter(dynamicRvResAdapter);
    }

    private void createChip(String name){
        boolean alreadyInGroup =  false;
        for (int i = 0; i < searchItemsChipGroup.getChildCount();i++){
            Chip chip = (Chip)searchItemsChipGroup.getChildAt(i);
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
            searchItemsChipGroup.addView(chip);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchItemsChipGroup.removeView(view);
                    if(searchItemsChipGroup.getChildCount() == 0){
                        searchItemsChipGroup.setVisibility(View.INVISIBLE);
                    }
                    searchByTags();

                }
            });
            searchItemsChipGroup.setVisibility(View.VISIBLE);
        }
    }

    public void triggerCreateLinkActivity(Link link){
        Intent intent = new Intent(GroupDetailsActivity.this, CreateLinkActivity.class);
        intent.putExtra("PreviousActivity", "GroupDetailsActivity");
        intent.putExtra("LoggedUser", (new Gson()).toJson(user));
        intent.putExtra("CurrentGroup", (new Gson()).toJson(group));
        if(link != null) {
            intent.putExtra("CurrentLink", (new Gson()).toJson(link));
            intent.putExtra("Action", "Update");
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