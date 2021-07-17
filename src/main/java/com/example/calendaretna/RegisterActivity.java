package com.example.calendaretna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {
    MaterialEditText userName, email, password, mobile;
    RadioGroup radioGroup;
    Button register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mobile = findViewById(R.id.mobile);
        radioGroup = findViewById(R.id.radiogp);
        register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        register.setOnClickListener(view -> {
            String txtUserName = userName.getText().toString().trim();
            String txtEmail = email.getText().toString().trim();
            String txtPassword = password.getText().toString().trim();
            String txtMobile = mobile.getText().toString();
            if (TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtMobile)){
                Toast.makeText(RegisterActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
            }
            else{
                int genderId = radioGroup.getCheckedRadioButtonId();
                RadioButton selected_Gender = radioGroup.findViewById(genderId);
                if (selected_Gender == null){
                    Toast.makeText(RegisterActivity.this, "Select gender Please", Toast.LENGTH_SHORT).show();
                }
                else {
                    String selectGender = selected_Gender.getText().toString();

                    mAuth.createUserWithEmailAndPassword(txtEmail,txtPassword)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }


}