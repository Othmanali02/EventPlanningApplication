package com.example.eventplanningapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference myRef;
    RecycleAdapter myAdapter;
    ArrayList<Event> eventList;
    Button createBtn;
    Button profileBtn;

    Button homeBtn;
    private Button logout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Event planner app
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.followingEvents);
        profileBtn = findViewById(R.id.ProfilePage);
        homeBtn = findViewById(R.id.homePage);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putBoolean("remeberMe",false);
                editor.apply();
                Intent intent=new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile_page.class);
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();


        fetchDataFromServer();
        createBtn = findViewById(R.id.CreateEvent);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Create Event", "it is working");

                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }
        });
//        Event eventTest = new Event(
//                "Design Showcase",
//                R.drawable.arts,
//                "2024-02-13",
//                "This event will be held in the Ground floor of the Arts building, and it will showcase the work of our senior year fine arts majors for their seminar.",
//                "3",
//                "Arts Building",
//                "Fine Arts Club",
//                "Free for everyone."
//        );

//        myRef.push().setValue(eventTest);
    }

    private void fetchDataFromServer() {
        String url = "http://10.0.2.2:5000/events";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("TaGGAGEGEA", response.toString());
                    ArrayList<Event> eventList = parseJsonData(response.toString());
                    myAdapter = new RecycleAdapter(this, eventList);
                    recyclerView.setAdapter(myAdapter);
                },
                error -> {
                    error.printStackTrace();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private ArrayList<Event> parseJsonData(String jsonData) {
        Gson gson = new Gson();
        Type eventType = new TypeToken<List<Event>>() {}.getType();
        return gson.fromJson(jsonData, eventType);

    }
}