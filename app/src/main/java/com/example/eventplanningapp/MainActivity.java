package com.example.eventplanningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference myRef;
    RecycleAdapter myAdapter;
    ArrayList<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myRef = FirebaseDatabase.getInstance().getReference().child("Events");
        recyclerView = findViewById(R.id.followingEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        myAdapter = new RecycleAdapter(this, eventList);
        recyclerView.setAdapter(myAdapter);

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Event event = ds.getValue(Event.class);
                    eventList.add(event);
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}