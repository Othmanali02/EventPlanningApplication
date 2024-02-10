package com.example.eventplanningapp;

import static com.example.eventplanningapp.Profile_page.PICK_IMAGE_REQUEST;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CreateEvent extends AppCompatActivity {
    private TextView eventNameUI;
    private TextView eventLocationUI;
    private TextView likeCountUI;
    private ImageView eventImageUI;

    private EditText eventDateUI;
    private TextView eventDescriptionUI;
    private TextView eventPlannerUI;
    private TextView eventPricingUI;

    private String fileURI = "";
    private Button chooseImage;
    private Button createEventButton;
    private static final int PERMISSION_REQUEST_CODE = 100;

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
        chooseImage = findViewById(R.id.chooseImage);
        eventDateUI = findViewById(R.id.dateInput);
        createEventButton = findViewById(R.id.createEventtt);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to choose an image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    Log.d("Bro I messed up a lot last year", fileURI);

                    JSONObject eventData = new JSONObject();
                eventData.put("eventName", eventNameUI.getText().toString());
                eventData.put("eventPlanner", "CS Club");
                //should have the name of the current account here
                eventData.put("eventLocation", eventLocationUI.getText().toString());
                eventData.put("eventDate", eventDateUI.getText().toString());
                eventData.put("eventDescription", eventDescriptionUI.getText().toString());
                eventData.put("eventPricing", eventPricingUI.getText().toString());
                eventData.put("likeCount", "0");
                String image64 = convertImageToBase64(fileURI);
                    Log.d("makes sense?", image64);
                eventData.put("eventImage", image64);

                callFlasktoCreateEvent(eventData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CreateEvent.this, MainActivity.class);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String imagePath = "";
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                imagePath = filePath;
            }
            fileURI = imagePath;
        }
    }


    public String convertImageToBase64(String imagePath) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE);
            byte[] bytes = readFileToByteArray(new File(imagePath));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } else {
            byte[] bytes = readFileToByteArray(new File(imagePath));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
    }

    private byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) fis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
