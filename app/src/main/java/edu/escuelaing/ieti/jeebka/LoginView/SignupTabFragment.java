package edu.escuelaing.ieti.jeebka.LoginView;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    Dialog dialog;

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
        dialog = new Dialog(getContext());
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().equals("") && !email.getText().toString().equals("") && !pass.getText().toString().equals(""))
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
                        openErrorWindow();
                        return;
                    }
                    username.setText(""); email.setText(""); pass.setText("");
                    Log.i("Create user", "User created");
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    openErrorWindow();
                    Log.i("Create user Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            openErrorWindow();
            Log.i("Create user Failure", e.getMessage());
        }
    }

    private void openErrorWindow(){
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView errorText = dialog.findViewById(R.id.error_text);
        errorText.setText("Ocurrio un problema al intentar registrarse.");
        Button closeDialogButton = dialog.findViewById(R.id.ok_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView closeIcon = dialog.findViewById(R.id.close_icon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}