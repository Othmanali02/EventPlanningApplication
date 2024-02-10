package com.example.eventplanningapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference myRef;
    RecycleAdapter myAdapter;
    ArrayList<Event> eventList;
    Button createBtn;
    Button profileBtn;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;

    Button homeBtn;
    private Button logout;
    private TextView userName;
    private TextView UserEmail;
    private ImageButton Menu;
    private   Boolean Verified;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Event planner app
        setContentView(R.layout.activity_main);
        int resourceId = getResources().getIdentifier("arts", "drawable", getPackageName());

        Log.d("IMAGE RESOURCE ID", resourceId + "");

        recyclerView = findViewById(R.id.followingEvents);
        profileBtn = findViewById(R.id.ProfilePage);
        homeBtn = findViewById(R.id.homePage);
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.UsernameMainPage);
        UserEmail = findViewById(R.id.UserEmailMainPage);
        Menu = findViewById(R.id.MenuMianPage);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigationDrawer();
            }
        });
        sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);


        String email=sharedPreferences.getString("email","No Email");
        if(!email.equals("No Email")){
            fireStore.collection("users").document(email).get().addOnSuccessListener( documentSnapshot -> {

                String base64Image = documentSnapshot.getString("imageUrl");
                String name = documentSnapshot.getString("userName");
                String phone = documentSnapshot.getString("number");
                Verified = documentSnapshot.getBoolean("verified_flage");

                UserEmail.setText(email);
                userName.setText(name);

            });

        }



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
                if(Verified){
                Log.d("Create Event", "it is working");

                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }else {
                    Toast.makeText(MainActivity.this, "Only verfied users can create events ", Toast.LENGTH_SHORT).show();
                }
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
                    Log.d("ERROR", error.getMessage());
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
    private void openNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);

        }
    }
    @Override
    public void onBackPressed() {
        // Check if the current activity is the DashboardActivity
        if (getClass().getSimpleName().equals("MainActivity")) {
            // You can show a Toast message or take any other action
            Toast.makeText(this, "Back button press disabled on mainActivity", Toast.LENGTH_SHORT).show();
        } else {
            // Call the default behavior to allow navigating back for other activities
            super.onBackPressed();
        }
    }
}
