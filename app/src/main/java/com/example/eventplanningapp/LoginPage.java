package com.example.eventplanningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginPage extends AppCompatActivity {
    private EditText UserEmail;
    private EditText userPassword;
    private Button LOGIN;
    private ArrayList<User> Userlist;
    private HashMap<String,String> loginInfo;
    private CheckBox rememberMe;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        UserEmail=findViewById(R.id.LoginEmail);
        userPassword=findViewById(R.id.LoginPassword);
        LOGIN=findViewById(R.id.Login_LoginPage);
        rememberMe=findViewById(R.id.rememberMe);
        loadData();
        LOGIN.setOnClickListener(view -> login());
        //flag=false;


    }

    // @Override
   /* protected void onStart() {
        super.onStart();
        loadFlag();

        if(flag){
            Intent intent=new Intent(Login_page.this,display_page.class);
            startActivity(intent);
        }

    }*/
    private void loadData(){
        SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String json=sharedPreferences.getString("UserList",null);

        if(json!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<User>>(){}.getType();

            Userlist=gson.fromJson(json,type);
            if(Userlist==null){
                Userlist=new ArrayList<>();
            }
            // Toast.makeText(this, "email"+users.get(position).UserName+" "+users.get(position).UserName, Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "no user found no data ", Toast.LENGTH_SHORT).show();

        }



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
        String email = UserEmail.getText().toString();
        String password = userPassword.getText().toString();
        boolean Check=false;

        for(int i=0;i<Userlist.size();i++){
            User user=Userlist.get(i);
            if(user.getEmail().equals(email)&&user.getPassword().equals(password)){
                Userlist.get(i).setRememberMe_flage(rememberMe.isChecked());
                Check=true;
                flag=  Userlist.get(i).isRememberMe_flage();
                SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                Gson gson = new Gson();
                String json =gson.toJson(flag);
                editor.putString("flag",json);
                editor.apply();
                Toast.makeText(this, "login succesfull", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginPage.this,fyp_Page.class);
                startActivity(intent);
            }

        }
        if(!Check){
            Toast.makeText(this, "error login in: incorrect email or password", Toast.LENGTH_SHORT).show();

        }

    }

}