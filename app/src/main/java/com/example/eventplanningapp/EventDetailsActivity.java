package com.example.eventplanningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {
    private TextView eventNameUI;
    private TextView eventLocationUI;
    private TextView likeCountUI;
    private ImageView eventImageUI;

    private Button editEvent;
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
//        eventLocationUI = findViewById(R.id.location);
        likeCountUI = findViewById(R.id.likeCount);
        eventDateUI = findViewById(R.id.date);
        editEvent = findViewById(R.id.editEvent);
//        eventDescriptionUI = findViewById(R.id.description);
//        eventPlannerUI = findViewById(R.id.eventPlanner);
//        eventPricingUI = findViewById(R.id.pricing);


        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        Log.d("event ID", eventID);
        fetchDataFromServer(eventID, this);

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventDetailsActivity.this, EditEvent.class);
                intent.putExtra("eventID", eventID + "");

                startActivity(intent);
            }
        });

    }

    private void fetchDataFromServer(String eventID, Context context) {
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
                            String eventImageResource = response.getString("eventImage");

                            String base64Image = eventImageResource;
                            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            eventImageUI.setImageBitmap(bitmap);

//                            eventPlannerUI.setText(eventPlanner);
//                            eventDescriptionUI.setText(eventDescription);
                            eventNameUI.setText(eventName);
//                            eventLocationUI.setText(eventLocation);
                            eventDateUI.setText(eventDate);

//                            eventPricingUI.setText(eventPricing);
                            likeCountUI.setText(likeCount);
                            ArrayList<String> eventList = new ArrayList<String>();


                            eventList.add(eventLocation);
                            eventList.add(eventDescription);
                            eventList.add(eventPlanner);
                            eventList.add(eventPricing);
                            Log.d("EventList", eventList.toString());

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, eventList);
                            ListView listView = findViewById(R.id.MetadatalistView);
                            listView.setAdapter(adapter);

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