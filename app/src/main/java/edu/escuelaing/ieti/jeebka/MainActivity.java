package edu.escuelaing.ieti.jeebka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

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

    }

    /*private void getUserById(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7203/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JeebkaApi jeebkaApi = retrofit.create(JeebkaApi.class);
        Call<User> call = jeebkaApi.getUserByEmail("esteesunemail");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    mJsonTxtView.setText("Codigo: " + response.code());
                    return;
                }
                mJsonTxtView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mJsonTxtView.setText(t.getMessage());
            }
        });
    }*/
}