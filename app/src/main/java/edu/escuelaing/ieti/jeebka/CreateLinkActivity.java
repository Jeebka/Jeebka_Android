package edu.escuelaing.ieti.jeebka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateLinkActivity extends AppCompatActivity {

    EditText groupName, groupDescription;
    FloatingActionButton createButton;
    Retrofit retrofit;
    JeebkaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_link);
        settingUpView();
    }

    private void settingUpView(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);

        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);
        createButton = findViewById(R.id.create_link_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLink();
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