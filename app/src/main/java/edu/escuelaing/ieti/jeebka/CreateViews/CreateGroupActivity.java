package edu.escuelaing.ieti.jeebka.CreateViews;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.RequiresApi;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Random;

import edu.escuelaing.ieti.jeebka.DTOs.GroupDto;
import edu.escuelaing.ieti.jeebka.GroupsView.GroupsViewActivity;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateGroupActivity extends Activity {

    EditText groupName, groupDescription;
    ImageView groupColor;
    Switch publicSwitch;
    FloatingActionButton createButton;
    Retrofit retrofit;
    JeebkaApi api;
    User loggedUser;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int)(height*.7));
        settingUpView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void settingUpView(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
        Intent intent = getIntent();
        loggedUser = (new Gson()).fromJson(intent.getStringExtra("LoggedUser"), User.class);
        settingUpComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void settingUpComponents(){
        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);
        createButton = findViewById(R.id.create_group_button);
        publicSwitch = findViewById(R.id.public_switch);
        groupColor = findViewById(R.id.group_color);

        int randomColor = generateRandomColor(Color.valueOf(227, 209, 209));
        String color = String.format("#%06X", (0xFFFFFF & randomColor));
        ColorStateList buttonStates = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{Color.parseColor(color), Color.parseColor("#FFAAAAAA")}
        );
        publicSwitch.getTrackDrawable().setTintList(buttonStates);
        groupColor.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createGroup(color);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createGroup(String color){
        try{
            String groupNameText = String.valueOf(groupName.getText()), groupDescriptionText = String.valueOf(groupDescription.getText());
            String userEmail = String.valueOf(loggedUser.getEmail());

            GroupDto groupDto = new GroupDto(groupNameText, groupDescriptionText, publicSwitch.isChecked(), color);
            Call<Group> group = api.createGroup(groupDto, userEmail);
            group.enqueue(new Callback<Group>() {
                @Override
                public void onResponse(Call<Group> call, Response<Group> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful", response.code() + "");
                        return;
                    }
                    Intent intent = new Intent(CreateGroupActivity.this, GroupsViewActivity.class);
                    intent.putExtra("LoggedUser", (new Gson()).toJson(loggedUser));
                    startActivity(intent);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int generateRandomColor(Color mix) {
        Random random = new Random();
        float red = random.nextInt(256);
        float green = random.nextInt(256);
        float blue = random.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (red + (int)mix.red()) / 2;
            green = (green + (int)mix.green()) / 2;
            blue = (blue + (int)mix.blue()) / 2;
        }

        Color color = Color.valueOf(red, green, blue);
        return color.toArgb();
    }
}