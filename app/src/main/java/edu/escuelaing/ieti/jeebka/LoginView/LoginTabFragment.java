package edu.escuelaing.ieti.jeebka.LoginView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.gson.Gson;

import edu.escuelaing.ieti.jeebka.DTOs.UserDto;
import edu.escuelaing.ieti.jeebka.GroupsView.GroupsViewActivity;
import edu.escuelaing.ieti.jeebka.Interface.JeebkaApi;
import edu.escuelaing.ieti.jeebka.Models.LoginResponse;
import edu.escuelaing.ieti.jeebka.Models.User;
import edu.escuelaing.ieti.jeebka.R;
import edu.escuelaing.ieti.jeebka.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginTabFragment extends Fragment {
    EditText email, pass;
    Button login, dialogButton;
    float v = 0;
    Retrofit retrofit;
    JeebkaApi api;
    Dialog dialog;

    public LoginTabFragment() {
        // Required empty public constructor
    }

    public static LoginTabFragment newInstance() {
        LoginTabFragment fragment = new LoginTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        settingUpView(root);
        return root;
    }

    private void settingUpView(ViewGroup root){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jeebka-backend.azurewebsites.net/v1/jeebka/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JeebkaApi.class);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        login = root.findViewById(R.id.login_button);
        dialog = new Dialog(getContext());
        viewAnimations();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().equals("") && !pass.getText().toString().equals(""))
                    checkUserCredentials();
            }
        });

    }

    private void openErrorWindow(){
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView errorText = dialog.findViewById(R.id.error_text);
        errorText.setText("Ocurrio un problema al intentar acceder a su cuenta.");
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

    private void viewAnimations(){
        email.setTranslationX(300);
        pass.setTranslationX(300);
        login.setTranslationX(300);
        email.setAlpha(v);
        pass.setAlpha(v);
        login.setAlpha(v);
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
    }

    private void checkUserCredentials(){

        UserDto userLogin = new UserDto("as", String.valueOf(email.getText()), String.valueOf(pass.getText()));
        try{
            Call<LoginResponse> call = api.login(userLogin);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(!response.isSuccessful()){
                        Log.i("Not successful log in", response.code() + "");
                        openErrorWindow();
                        return;
                    }
                    if(response.body().getMsg().equals("loged")){
                        triggerGroupViewActivity();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    openErrorWindow();
                    Log.i("Log in Failure", t.getMessage());
                }
            });
        } catch (Exception e){
            openErrorWindow();
            Log.i("Log in Failure", e.getMessage());
        }
    }

    private void triggerGroupViewActivity(){
        Call<User> call = api.getUserByEmail(String.valueOf(email.getText()));;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.i("Not successful get user", response.code() + "");
                    return;
                }
                User userLogged = response.body();
                Intent intent = new Intent(getActivity(), GroupsViewActivity.class);
                intent.putExtra("LoggedUser", (new Gson()).toJson(userLogged));
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Get user Failure", t.getMessage());
            }
        });
    }
}
