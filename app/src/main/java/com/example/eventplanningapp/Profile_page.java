package com.example.eventplanningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
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
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.EditPhone);
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

                String imageUrl = documentSnapshot.getString("imageUrl");
                String name = documentSnapshot.getString("UserName");
                String phone = documentSnapshot.getString("number");
                Boolean Verified = documentSnapshot.getBoolean("verified_flage");

                    Email.setText(email);
                    Phone.setText(phone);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    //Uri uri = Uri.parse(imageUrl);

                    Picasso.get().invalidate(imageUrl);
                    Picasso.get().load(imageUrl).into(profileImage);



                    // Set the drawable to the ImageButton

                } else {
                // Handle empty or null imageUrl
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
                            .update("imageUrl", uri.toString());
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
}