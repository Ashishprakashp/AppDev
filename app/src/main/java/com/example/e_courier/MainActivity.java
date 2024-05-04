package com.example.e_courier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Find the view you want to animate
        View viewToAnimate1 = findViewById(R.id.mail);
        View viewToAnimate2 = findViewById(R.id.btn);
        View viewToAnimate3 = findViewById(R.id.textView);
        View viewToAnimate4 = findViewById(R.id.loginbtn);
        View viewToAnimate5 = findViewById(R.id.signupbtn);
        View viewToAnimate6 = findViewById(R.id.fpw);

        // Apply the animation to the view
        viewToAnimate1.startAnimation(fadeInAnimation);
        viewToAnimate2.startAnimation(fadeInAnimation);
        viewToAnimate3.startAnimation(fadeInAnimation);
        viewToAnimate4.startAnimation(fadeInAnimation);
        viewToAnimate5.startAnimation(fadeInAnimation);
        viewToAnimate6.startAnimation(fadeInAnimation);
        LottieAnimationView lottieView = findViewById(R.id.lottieview2);
        lottieView.setAnimation(R.raw.truck); // Assuming your JSON file is named "truck.json" and located in res/raw
        lottieView.playAnimation();
        MyDBHelper dbHelper = new MyDBHelper(this);



        mAuth=FirebaseAuth.getInstance();
        Button signup = (Button)findViewById(R.id.signupbtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,com.example.e_courier.signup.class));
            }
        });
        TextView tv = (TextView) findViewById(R.id.fpw);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });
        Button login= (Button) findViewById(R.id.loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((TextInputEditText)findViewById(R.id.mail)).getText().toString();
                String password = ((TextInputEditText)findViewById(R.id.btn)).getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(MainActivity.this, LoginLoad.class);
                                    intent.putExtra("mail", email);
                                    startActivity(intent);


                                    Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }

}