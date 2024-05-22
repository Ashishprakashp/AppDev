package com.example.e_courier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class LoginLoad extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String value = intent.getStringExtra("mail");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginload);
        LottieAnimationView lottie;


        lottie =findViewById(R.id.lottieview1);
        lottie.animate();
        lottie.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),NewHome.class);
                i.putExtra("mail",value);
                startActivity(i);
            }
        },1000);

    }
}
