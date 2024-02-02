package com.example.eventplanningapp;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;


import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EditEvent extends AppCompatActivity {
    private TextView eventNameUI;
    private TextView eventLocationUI;
    private TextView likeCountUI;
    private ImageView eventImageUI;

    private EditText eventDateUI;
    private TextView eventDescriptionUI;
    private TextView eventPlannerUI;
    private TextView eventPricingUI;

    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent);


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


        createEventButton = findViewById(R.id.createEventtt);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject eventData = new JSONObject();
                    eventData.put("eventName", eventNameUI.getText().toString());
                    eventData.put("eventPlanner", "CS Club");
                    //should have the name of the current account here
                    eventData.put("eventLocation", eventLocationUI.getText().toString());
                    eventData.put("eventDate", eventDateUI.getText().toString());
                    eventData.put("eventDescription", eventDescriptionUI.getText().toString());
                    eventData.put("eventPricing", eventPricingUI.getText().toString());
                    eventData.put("likeCount", "0");

                    callFlasktoCreateEvent(eventData);
                } catch (JSONException e) {
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

    private void callFlasktoCreateEvent(JSONObject eventData) {
        String url = "http://10.0.2.2:5000/create";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                eventData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("This is supposed to be the response.", response.toString());
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
}
