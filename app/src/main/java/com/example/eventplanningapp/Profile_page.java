package com.example.eventplanningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Profile_page extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
     private SharedPreferences sharedPreferences;
    private ImageButton profileImage;
    private EditText Email;
    private EditText Phone;
    private ImageButton editEmail;
    private ImageButton editPhone;
    private  String email;
    private TextView username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        email=sharedPreferences.getString("email","No Email");
        setContentView(R.layout.activity_profile_page);
        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> openGallery(v));
        Email = findViewById(R.id.profileUserEmail);
        Phone = findViewById(R.id.profileUserNumber);

        editPhone = findViewById(R.id.EditPhone);
        username=findViewById(R.id.UserNameProfile);
        String email=sharedPreferences.getString("email","No Email");
//        editEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String emailText = Email.getText().toString();
//
//                    updateEmail(emailText);
//
//            }
//        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneText = Phone.getText().toString();

                updatePhone(PhoneText);

            }
        });

        if(!email.equals("No Email")){
            fireStore.collection("users").document(email).get().addOnSuccessListener( documentSnapshot -> {

                String base64Image = documentSnapshot.getString("imageUrl");
                String name = documentSnapshot.getString("userName");
                String phone = documentSnapshot.getString("number");
                Boolean Verified = documentSnapshot.getBoolean("verified_flage");

                    Email.setText(email);
                    Phone.setText(phone);
                    username.setText(name);
                if (base64Image != null && !base64Image.isEmpty()) {
                    byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    profileImage.setImageBitmap(decodedBitmap);
                } else {
                    // Handle empty or null base64Image
                    // For example, load a default image or set a placeholder
                    profileImage.setImageResource(R.drawable.profilelogo);
                }
            });

        }


    }
    private void updateEmail(String Email) {
        fireStore.collection("users").document(email)
                .update("email", Email);
    }
    private void updatePhone( String phone) {
        fireStore.collection("users").document(email)
                .update("number", phone);
    }
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                 sharedPreferences= getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("ImageUri",uri.toString());
               // String email=sharedPreferences.getString("email","No Email");

                editor.apply();
                if(!email.equals("No Email")){
                    fireStore.collection("users").document(email)
                            .update("imageUrl",  bitmapToBase64(uri));
                }else {
                    Toast.makeText(this, "Please Login correctly , error  getting user email", Toast.LENGTH_SHORT).show();
                }

                InputStream inputStream = getContentResolver().openInputStream(uri);
                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());

                // Set the drawable to the ImageButton
                profileImage.setImageDrawable(drawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String bitmapToBase64(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream inputStream = resolver.openInputStream(uri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}