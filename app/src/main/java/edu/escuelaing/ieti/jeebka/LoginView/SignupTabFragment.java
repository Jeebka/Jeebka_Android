package edu.escuelaing.ieti.jeebka.LoginView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.escuelaing.ieti.jeebka.R;

public class SignupTabFragment extends Fragment {

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
        return root;
    }
}