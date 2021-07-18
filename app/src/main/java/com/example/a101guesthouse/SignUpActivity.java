package com.example.a101guesthouse;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstName, lastName, userEmail, userName, userPassword, userCountry;
    private Button SignUpButton;
    private TextView SignInTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox termsCheckbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        firstName=findViewById(R.id.firstname);
        lastName=findViewById(R.id.lastname);
        userEmail=findViewById(R.id.email);
        userName=findViewById(R.id.username);
        userPassword=findViewById(R.id.password);
        userCountry=findViewById(R.id.country);
        SignUpButton=findViewById(R.id.sign_inButton);
        progressDialog=new ProgressDialog(this);
        SignInTv=findViewById(R.id.sign_loginButton);
        termsCheckbox = findViewById(R.id.checkbox);

        termsCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Terms();
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
        SignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void Register(){
        String firstname=firstName.getText().toString().trim();
        String lastname=lastName.getText().toString().trim();
        String email=userEmail.getText().toString().trim();
        String username=userName.getText().toString().trim();
        String password=userPassword.getText().toString().trim();
        String country=userCountry.getText().toString().trim();
        if (TextUtils.isEmpty(firstname)){
            firstName.setError("Enter your first name");
            firstName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(lastname)){
            lastName.setError("Enter your last name");
            lastName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(email)){
            userEmail.setError("Enter your email");
            userEmail.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(username)){
            userName.setError("Enter your username");
            userName.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(password)){
            userPassword.setError("Enter your password");
            userPassword.requestFocus();
            return;
        }
        else if (password.length()<6){
            userPassword.setError("Length should be greater than 6");
            userPassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(country)){
            userCountry.setError("Enter your country");
            userCountry.requestFocus();
            return;
        }
        else if (!isValidEmail(email)){
            userEmail.setError("Invalid Email");
            userEmail.requestFocus();
            return;
        }




        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(firstname,lastname,email,username,country);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SignUpActivity.this,Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Sign up failed!",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private Boolean isValidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void Terms(){
        if (termsCheckbox.hasFocus()){
            Register();
        }
        else{
            Toast.makeText(this, "You need to first accept our terms and conditions...",
                    Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(SignUpActivity.this, Terms.class);
            startActivity(intent);
            finish();
        }
    }
}
