package com.example.eventplanningapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditEvent extends AppCompatActivity {
    private TextView eventNameUI;
    private TextView eventLocationUI;
    private TextView likeCountUI;
    private ImageView eventImageUI;
    private SharedPreferences sharedPreferences;

    private EditText eventDateUI;
    private TextView eventDescriptionUI;
    private TextView eventPlannerUI;
    private TextView eventPricingUI;
    private Button deleteButton;

    private Button createEventButton;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private String email;
    private String name;
    private boolean Verified;
    private ImageButton Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editevent);

         email=sharedPreferences.getString("email","No Email");
        getname();

        Log.d("Check Create Event", "it is loading properly");


//        eventImageUI = findViewById(R.id.eventPic);
        eventNameUI = findViewById(R.id.nameEditText);
        eventLocationUI = findViewById(R.id.locationEditText);
//        likeCountUI = findViewById(R.id.likeCount);
        eventDescriptionUI = findViewById(R.id.descriptionEdit);
//        eventPlannerUI = findViewById(R.id.eventPlanner);
        //should get it from Firebase info
        eventPricingUI = findViewById(R.id.eventPricing);
        eventDateUI = findViewById(R.id.dateInput);
        Menu = findViewById(R.id.burgerMenu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavigationDrawer();
            }
        });


        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        Log.d("event ID", eventID);

        createEventButton = findViewById(R.id.createEventtt);
        deleteButton = findViewById(R.id.deleteEventtt);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     if(Verified){
                try {

                    JSONObject eventData = new JSONObject();
                    eventData.put("eventName", eventNameUI.getText().toString());
                    eventData.put("eventPlanner", name);
                    eventData.put("eventLocation", eventLocationUI.getText().toString());
                    eventData.put("eventDate", eventDateUI.getText().toString());
                    eventData.put("eventDescription", eventDescriptionUI.getText().toString());
                    eventData.put("eventPricing", eventPricingUI.getText().toString());
                    eventData.put("likeCount", "0");

                    callFlasktoCreateEvent(eventID, eventData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                         Toast.makeText(EditEvent.this, "only verfied users can edit", Toast.LENGTH_SHORT).show();
                     }
        }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                   deleteEvent(eventID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        eventDateUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                // Handle the selected date
                                String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                eventDateUI.setText(selectedDate);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                // Customize the appearance and behavior of the dialog if needed

                dpd.show(getSupportFragmentManager(), "DatePickerDialog");
            }
        });
    }

    private void deleteEvent(String id) {
        String url = "http://10.0.2.2:5000/delete/" +id;
        JSONObject mock = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                mock,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("This is supposed to be the response.", response.toString());
                        Intent intent = new Intent(EditEvent.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void callFlasktoCreateEvent(String id, JSONObject eventData) {
        String url = "http://10.0.2.2:5000/update/" +id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                eventData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("This is supposed to be the response.", response.toString());
                        Intent intent = new Intent(EditEvent.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void getname(){
        if(!email.equals("No Email")){
            fireStore.collection("users").document(email).get().addOnSuccessListener( documentSnapshot -> {

                String base64Image = documentSnapshot.getString("imageUrl");
                 name = documentSnapshot.getString("userName");
                String phone = documentSnapshot.getString("number");
                 Verified = documentSnapshot.getBoolean("verified_flage");



            });

        }
    }
    private void openNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);

        }
    }
}
