package com.example.eventplanningapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Register_page extends AppCompatActivity {
    private EditText userName;
    private EditText UserEmail;
    private EditText userPassword;
    private EditText UserNumber;
    private Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        userName=findViewById(R.id.RegisterUserName);
        UserEmail=findViewById(R.id.RegisterEmail);
        userPassword=findViewById(R.id.RegisterPassword);
        signUp=findViewById(R.id.SignUpRegisterPage);
        UserNumber=findViewById(R.id.RegisterNumber);

    }

    protected void onStart() {
        super.onStart();
        ArrayList<User> UserList = new ArrayList<>();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(userName.getText())||isEmpty(UserEmail.getText())||isEmpty(userPassword.getText())){
                    Toast.makeText(Register_page.this, "Please enter all data required", Toast.LENGTH_LONG).show();
                }else{
                    User user=new User(userName.getText().toString(),
                            UserEmail.getText().toString(),userPassword.getText().toString(),UserNumber.getText().toString(),false);
                    UserList.add(user);
                    SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json =gson.toJson(UserList);
                    editor.putString("UserList",json);
                    editor.apply();
                    Intent intent=new Intent(Register_page.this,LoginPage.class);
                    startActivity(intent);



                }
            }
        });

    }
    private void loadData(int position){
        SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String json=sharedPreferences.getString("UserList",null);

        if(json!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<User>>(){}.getType();

            ArrayList<User> users=gson.fromJson(json,type);

        }else{
            Toast.makeText(this, "no user found no data ", Toast.LENGTH_SHORT).show();

        }



    }
}