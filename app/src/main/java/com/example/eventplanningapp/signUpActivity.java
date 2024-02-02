package com.example.eventplanningapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUpActivity extends AppCompatActivity {

    private EditText userName, loginEmail, phoneNumber, password;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Components
        userName = findViewById(R.id.UserName);
        loginEmail = findViewById(R.id.LoginEmail);
        phoneNumber = findViewById(R.id.ConfirmPassword); // Assuming ConfirmPassword is used for phoneNumber based on your layout
        password = findViewById(R.id.Password);
        signUpButton = findViewById(R.id.Login_LoginPage);
        rememberMe = findViewById(R.id.rememberMe);

        // Sign Up button listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String userName = this.userName.getText().toString().trim();
        String email = this.loginEmail.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String number = this.phoneNumber.getText().toString().trim();
        boolean rememberMeFlag = this.rememberMe.isChecked();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || number.isEmpty()) {
            Toast.makeText(signUpActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("userName", userName);
                            user.put("email", email);
                            user.put("password", password); // Storing passwords in Firestore is not recommended
                            user.put("phoneNumber", number);
                            user.put("rememberMe", rememberMeFlag);
                            user.put("verified", false); // You can manage verification status here

                            db.collection("users").document(userId) // Using userId as the document ID
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(signUpActivity.this, "User registered successfully.", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(signUpActivity.this, "Error adding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("FirestoreError", e.getMessage()); // Log the error
                                    });
                        }
                    } else {
                        Toast.makeText(signUpActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }}