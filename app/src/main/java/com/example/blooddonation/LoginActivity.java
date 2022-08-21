package com.example.blooddonation;


import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextView backButton;
    private TextInputEditText loginEmail, loginPassword;
    private TextView forgotPassword;
    private Button loginButton;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if(user !=null){
                Intent intent = new Intent(getBaseContext(),SelectRegistrationActivity.class);
                startActivity(intent);
                //finish();
            }
        };

        backButton=findViewById(R.id.backButton);
        loginEmail =findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        loginButton = findViewById(R.id.loginButton);
        loader = new ProgressDialog(this);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(),SelectRegistrationActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(v -> {
            final String email = Objects.requireNonNull(loginEmail.getText()).toString().trim();
            final String password = Objects.requireNonNull(loginPassword.getText()).toString().trim();

            if(TextUtils.isEmpty(email)){
                loginEmail.setError("Email is required");
            }
            if(TextUtils.isEmpty(password)){
                loginPassword.setError("Password is required");
            }
            else{
                loader.setMessage("Log in in Progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}