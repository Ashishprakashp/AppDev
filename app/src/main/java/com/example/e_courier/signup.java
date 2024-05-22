package com.example.e_courier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Handler handler;
    private Runnable runnable;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Find the views you want to animate
        View viewToAnimate1 = findViewById(R.id.textView);
        View viewToAnimate2 = findViewById(R.id.mail);
        View viewToAnimate3 = findViewById(R.id.mobile);
        View viewToAnimate4 = findViewById(R.id.OTP);
        View viewToAnimate5 = findViewById(R.id.pw1);
        View viewToAnimate6 = findViewById(R.id.pw2);
        View viewToAnimate7 = findViewById(R.id.signup);
        View viewToAnimate8 = findViewById(R.id.login);

        // Apply the animation to the views
        viewToAnimate1.startAnimation(fadeInAnimation);
        viewToAnimate2.startAnimation(fadeInAnimation);
        viewToAnimate3.startAnimation(fadeInAnimation);
        viewToAnimate4.startAnimation(fadeInAnimation);
        viewToAnimate5.startAnimation(fadeInAnimation);
        viewToAnimate6.startAnimation(fadeInAnimation);
        viewToAnimate7.startAnimation(fadeInAnimation);
        viewToAnimate8.startAnimation(fadeInAnimation);

        Button login = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(v -> startActivity(new Intent(signup.this, MainActivity.class)));

        signup.setOnClickListener(v -> {
            String email = ((TextInputEditText) findViewById(R.id.mail)).getText().toString();
            String password = ((TextInputEditText) findViewById(R.id.pw1)).getText().toString();
            String password2 = ((TextInputEditText) findViewById(R.id.pw2)).getText().toString();
            String mobile = ((TextInputEditText) findViewById(R.id.mobile)).getText().toString();

            if (!password.equals(password2)) {
                Toast.makeText(signup.this, "Re-enter the same password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if (signInMethods != null && !signInMethods.isEmpty()) {
                                    // User already has an account with this email address
                                    Toast.makeText(signup.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    // User does not have an account, proceed with signup
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    currentUser = mAuth.getCurrentUser();
                                                    if (currentUser != null) {
                                                        currentUser.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                                                            if (emailVerificationTask.isSuccessful()) {
                                                                Toast.makeText(signup.this, "Signup successful. Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show();
                                                                startEmailVerificationCheck();
                                                            } else {
                                                                Toast.makeText(signup.this, "Failed to send verification email: " + emailVerificationTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Toast.makeText(signup.this, "Signup failed: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(signup.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void startEmailVerificationCheck() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentUser.reload().addOnCompleteListener(task -> {
                    if (currentUser.isEmailVerified()) {
                        handler.removeCallbacks(runnable);
                        saveUserData(currentUser.getEmail());
                    } else {
                        handler.postDelayed(runnable, 2000); // Check again after 2 seconds
                    }
                });
            }
        };
        handler.post(runnable);
    }

    private void saveUserData(String email) {
        if (email != null) {
            String mobile = ((TextInputEditText) findViewById(R.id.mobile)).getText().toString();
            String key = email.replace(".", ",");
            DataClass dataClass = new DataClass("", "", mobile, email, "");
            FirebaseDatabase.getInstance().getReference().child("users").child(key).setValue(dataClass);
            Intent intent = new Intent(signup.this, LoginLoad.class);
            intent.putExtra("mail", email);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
