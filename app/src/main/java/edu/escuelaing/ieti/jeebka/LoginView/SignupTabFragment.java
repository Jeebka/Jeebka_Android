package edu.escuelaing.ieti.jeebka.LoginView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.escuelaing.ieti.jeebka.DTOs.UserDto;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.LoginResponse;
import edu.escuelaing.ieti.jeebka.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupTabFragment extends Fragment {

    Retrofit retrofit;
    JeebkaApi api;
    Button signUp;
    EditText username, email, pass;

    public SignupTabFragment() {

    }

    public static SignupTabFragment newInstance() {
        SignupTabFragment fragment = new SignupTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);
        settingUpView(root);
        return root;
    }

    private void settingUpView(ViewGroup root){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);
        username = root.findViewById(R.id.username);
        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        signUp = root.findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    public void createUser(){
        String userNameText = String.valueOf(username.getText()), emailText = String.valueOf(email.getText()), passText = String.valueOf(pass.getText());
        UserDto newUser = new UserDto(userNameText, emailText, passText);
        try{
            Call<UserDto> call = api.createUser(newUser);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful log in", response.code() + "");
                        return;
                    }
                    Log.i("Create user", "User created");
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.i("Create user Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            Log.i("Create user Failure", e.getMessage());
        }
    }
}