package edu.escuelaing.ieti.jeebka;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.Link;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_link);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int)(height*.8));
        test();
        //settingUpView();
    }

    private void test(){
        String[] type = new String[]{"Test1","Prueba", "Test2", "Prueba2", "Test3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                type
        );
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.tags_autocomplete_items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CreateLinkActivity.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT);
            }
        });
    }

    private void settingUpView(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);

        linkName = findViewById(R.id.link_name);
        linkUrl = findViewById(R.id.link_url);
        createButton = findViewById(R.id.create_link_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createLink();
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