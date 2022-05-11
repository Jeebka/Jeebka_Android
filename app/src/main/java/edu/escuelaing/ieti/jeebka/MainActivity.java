package edu.escuelaing.ieti.jeebka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView mJsonTxtView;

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fb, google, twitter;
    float v = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRetrofit();

    }

    private void getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JeebkaApi api = retrofit.create(JeebkaApi.class);
        Call<User> call = api.getUserByEmail("pruebaemail2@prueba.com");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.i("Not succesful", "Codigo: "+ response.code());
                    return;
                }
                User user =  response.body();
                Log.i("User info", response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("On failure", t.getMessage());
            }
        });

    }

}