package com.example.eventplanningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

public class LoginPage extends AppCompatActivity {
    private EditText UserEmail;
    private EditText userPassword;
    private Button LOGIN;
    private ArrayList<User> Userlist;
    private SharedPreferences sharedPreferences;

    private HashMap<String,String> loginInfo;
    private CheckBox rememberMe;
    private boolean flag=false;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button fingerprint;
    private Button signUpButton;
    private Button forgetPassword;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String emailsave ,passwordsave;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setContentView(R.layout.activity_login_page);
        UserEmail=findViewById(R.id.LoginEmail);
        userPassword=findViewById(R.id.LoginPassword);
        LOGIN=findViewById(R.id.Login_LoginPage);
        rememberMe=findViewById(R.id.rememberMe);
        forgetPassword=findViewById(R.id.forgetPassword);



        LOGIN.setOnClickListener(view -> login());
        fingerprint = findViewById(R.id.fingerprint);
        signUpButton = findViewById(R.id.signUpLogin);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, Register_page.class);
            startActivity(intent);
        });
        createBiometricPrompt();
        createPromptInfo();
        // showBiometricPrompt();


        forgetPassword.setOnClickListener(v -> {

                String email = UserEmail.getText().toString().trim();
                // validate email regular expression
            if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    mAuth.sendPasswordResetEmail(email);
                    Toast.makeText(this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
                }


        });
        //flag=false;


    }
    protected void onStart() {
        super.onStart();
        loadData();

        if(flag){

            Intent intent=new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);
        }
        if(emailsave!=null){
            fingerprint.setOnClickListener(view -> {showBiometricPrompt();});
        }else {
            fingerprint.setVisibility(View.GONE);
        }


    }

    private void createBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Fingerprint authentication succeeded

                // Navigate to DashboardActivity
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Fingerprint authentication failed
                Toast.makeText(LoginPage.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Place your finger on the sensor")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void showBiometricPrompt() {
        // Display the biometric prompt
        biometricPrompt.authenticate(promptInfo);
    }

    // @Override

    private void loadData(){
        SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        //SharedPreferences.Editor editor=sharedPreferences.edit();
       // String json=sharedPreferences.getString("UserList",null);
        flag=sharedPreferences.getBoolean("remeberMe",false);
        emailsave=sharedPreferences.getString("email",null);

        //flag=false;


//        if(json!=null){
//
//            // Toast.makeText(this, "email"+users.get(position).UserName+" "+users.get(position).UserName, Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(this, "no user found no data ", Toast.LENGTH_SHORT).show();
//
//        }



    }
    private void saveData(){

        SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Toast.makeText(this, "flag "+flag, Toast.LENGTH_SHORT).show();
        editor.putBoolean("remeberMe", flag);
        editor.putString("email",UserEmail.getText().toString());
        editor.putString("password",userPassword.getText().toString());
        editor.apply();
    }
    /*  private void loadFlag(){
          SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
          SharedPreferences.Editor editor=sharedPreferences.edit();
          String json=sharedPreferences.getString("flag",null);

          if(json!=null){
              Gson gson = new Gson();
             // Type type = new TypeToken<ArrayList<User>>(){}.getType();

              flag=gson.fromJson(json,Boolean.class);
              Toast.makeText(this, "flag "+flag, Toast.LENGTH_SHORT).show();

              // Toast.makeText(this, "email"+users.get(position).UserName+" "+users.get(position).UserName, Toast.LENGTH_SHORT).show();

          }else{
              Toast.makeText(this, "no user found no data ", Toast.LENGTH_SHORT).show();

          }



      }*/
    public void login(){
        String email = UserEmail.getText().toString().trim();
        String password = userPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Toast.makeText(this, "succsefull", Toast.LENGTH_SHORT).show();
                //loadData();
                flag=rememberMe.isChecked();
                if(rememberMe.isChecked()){
                    saveData();
                    fireStore.collection("users").document(email)
                            .update("rememberMe_flage", true);

                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);


                    // Toast.makeText(this, "email"+users.get(position).UserName+" "+users.get(position).UserName, Toast.LENGTH_SHORT).show();
                }else{
                    saveData();
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        });



    }

}