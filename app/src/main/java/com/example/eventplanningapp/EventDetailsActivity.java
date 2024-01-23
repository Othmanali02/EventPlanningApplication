package com.example.eventplanningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventNameUI;
    private TextView eventLocationUI;
    private TextView likeCountUI;
    private ImageView eventImageUI;


    private TextView eventDateUI;
    private TextView eventDescriptionUI;
    private TextView eventPlannerUI;
    private TextView eventPricingUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventpage);

        eventImageUI = findViewById(R.id.eventPic);
        eventNameUI = findViewById(R.id.EVentName);
        eventLocationUI = findViewById(R.id.location);
        likeCountUI = findViewById(R.id.likeCount);
        eventDateUI = findViewById(R.id.date);
        eventDescriptionUI = findViewById(R.id.description);
        eventPlannerUI = findViewById(R.id.eventPlanner);
        eventPricingUI = findViewById(R.id.pricing);


        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        Log.d("event ID", eventID);
        fetchDataFromServer(eventID);

    }

    private void fetchDataFromServer(String eventID) {
        String url = "http://10.0.2.2:5000/events/" + eventID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response
                        Log.d("Event Details", response.toString());
                        try{
                            String eventName = response.getString("eventName");
                            String eventLocation = response.getString("eventLocation");
                            String likeCount = response.getString("likeCount");
                            String eventDate = response.getString("eventDate");
                            String eventDescription = response.getString("eventDescription");
                            String eventPlanner = response.getString("eventPlanner");
                            String eventPricing = response.getString("eventPricing");
                            int eventImageResource = response.getInt("eventImage");
                            eventImageUI.setImageResource(eventImageResource);

                            eventPlannerUI.setText(eventPlanner);
                            eventDescriptionUI.setText(eventDescription);
                            eventNameUI.setText(eventName);
                            eventLocationUI.setText(eventLocation);
                            eventDateUI.setText(eventDate);

                            eventPricingUI.setText(eventPricing);
                            likeCountUI.setText(likeCount);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private ArrayList<Event> parseJsonData(String jsonData) {
        Gson gson = new Gson();
        Type eventType = new TypeToken<List<Event>>() {}.getType();
        return gson.fromJson(jsonData, eventType);
    }
}