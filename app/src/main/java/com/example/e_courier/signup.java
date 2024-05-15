package com.example.e_courier;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.util.List;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Find the view you want to animate
        View viewToAnimate1 = findViewById(R.id.textView);
        View viewToAnimate2 = findViewById(R.id.mail);
        View viewToAnimate3 = findViewById(R.id.mobile);
        View viewToAnimate4 = findViewById(R.id.OTP);
        View viewToAnimate5 = findViewById(R.id.pw1);
        View viewToAnimate6 = findViewById(R.id.pw2);
        View viewToAnimate7 = findViewById(R.id.signup);
        View viewToAnimate8 = findViewById(R.id.login);

        // Apply the animation to the view
        viewToAnimate1.startAnimation(fadeInAnimation);
        viewToAnimate2.startAnimation(fadeInAnimation);
        viewToAnimate3.startAnimation(fadeInAnimation);
        viewToAnimate4.startAnimation(fadeInAnimation);
        viewToAnimate5.startAnimation(fadeInAnimation);
        viewToAnimate6.startAnimation(fadeInAnimation);
        viewToAnimate7.startAnimation(fadeInAnimation);
        viewToAnimate8.startAnimation(fadeInAnimation);
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this,MainActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=((TextInputEditText)findViewById(R.id.mail)).getText().toString();
                String password=((TextInputEditText)findViewById(R.id.pw1)).getText().toString();
                String password2=((TextInputEditText)findViewById(R.id.pw2)).getText().toString();

               // int mobile=Integer.parseInt(((TextInputEditText)findViewById(R.id.mobile)).getText().toString());
                //Toast.makeText(signup.this,email,Toast.LENGTH_SHORT).show();
                if(!password.equals(password2)){
                    Toast.makeText(signup.this,"Re-enter same password",Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        SignInMethodQueryResult result = task.getResult();
                                        List<String> signInMethods = result.getSignInMethods();
                                        if (signInMethods != null && !signInMethods.isEmpty()) {
                                            // User already has an account with this email address
                                            Toast.makeText(signup.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // User does not have an account, proceed with signup
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                            if (task.isSuccessful()) {
                                                                // Sign up success
                                                                MyDBHelper dbHelper = new MyDBHelper(com.example.e_courier.signup.this);
                                                                try {
                                                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                                    db.execSQL("INSERT INTO users(mail) VALUES ('"+email+"')");
                                                                    db.close();
                                                                } catch (SQLException e) {
                                                                    Toast.makeText(signup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                                                }
                                                                Toast.makeText(signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(signup.this,LoginLoad.class));
                                                            } else {
                                                                // Sign up failed
                                                                Toast.makeText(signup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Error occurred while checking sign-in methods
                                        Toast.makeText(signup.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
