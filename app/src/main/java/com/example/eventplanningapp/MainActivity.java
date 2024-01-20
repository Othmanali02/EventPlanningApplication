package com.example.eventplanningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference myRef;
    RecycleAdapter myAdapter;
    ArrayList<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.followingEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();


        fetchDataFromServer();

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
        String url = "http://10.0.2.2:5000/books";

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