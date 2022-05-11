 package edu.escuelaing.ieti.jeebka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import edu.escuelaing.ieti.jeebka.LoginView.LogInActivity;

 public class IntroductoryActivity extends AppCompatActivity {

    ImageView splashImage;
    TextView welcomeText, appName;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        splashImage = findViewById(R.id.splash_image);
        welcomeText = findViewById(R.id.welcome_text);
        appName = findViewById(R.id.app_name);

        splashImage.animate().translationY(-2300).setDuration(1000).setStartDelay(800);
        welcomeText.animate().translationY(1400).setDuration(1000).setStartDelay(800);
        appName.animate().translationY(1400).setDuration(1000).setStartDelay(800);

        anim = AnimationUtils.loadAnimation(this, R.anim.intro_transition);

        Utils.delay(2000, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                startLogInActivity();
            }
        });
    }

    private void startLogInActivity(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

}