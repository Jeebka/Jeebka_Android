package edu.escuelaing.ieti.jeebka.LoginView;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.escuelaing.ieti.jeebka.GroupsView.GroupsViewActivity;
import edu.escuelaing.ieti.jeebka.R;


public class LoginTabFragment extends Fragment {
    EditText email, pass;
    Button login;
    float v = 0;

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

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        login = root.findViewById(R.id.login_button);

        email.setTranslationX(300);
        pass.setTranslationX(300);
        login.setTranslationX(300);

        email.setAlpha(v);
        pass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(email.getText()).equals("Angie")){
                    Intent intent = new Intent(getActivity(), GroupsViewActivity.class);
                    intent.putExtra("username", "Angie");
                    startActivity(intent);
                } else{
                    Toast.makeText(getContext(), "Credenciales erroneas", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
