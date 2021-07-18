package com.example.a101guesthouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button LogInButton;
    private TextView SignUpTv;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        userEmail=findViewById(R.id.email);
        userPassword=findViewById(R.id.password);

        progressBar=new ProgressBar(this);

        SignUpTv=findViewById(R.id.login_signButton);
        SignUpTv.setOnClickListener((View.OnClickListener) this);

        LogInButton=findViewById(R.id.loginButton);
        LogInButton.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_signButton:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.loginButton:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (email.isEmpty()){
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please enter a valid email address!");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            userPassword.setError("Minimum password length is 6 characters");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    startActivity(new Intent(Login.this, Dashboard.class));

                }else {
                    Toast.makeText(Login.this, "Login Failed, Check your credentials please!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}