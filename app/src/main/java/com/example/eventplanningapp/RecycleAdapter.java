package com.example.eventplanningapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> list;

    public RecycleAdapter(Context context, ArrayList<Event> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.event, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = list.get(position);
        holder.title.setText(event.getEventName());
        holder.description.setText(event.getEventLocation());
        Log.d("Image", holder.image.getContext().toString());
        Log.d("Event IDD", event.getEventID() + "");

//        Drawable drawable = ContextCompat.getDrawable(holder.image.getContext(), event.getEventImage());
//
        String base64Image = event.getEventImage();
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        holder.image.setImageBitmap(bitmap);

        holder.count.setText(event.getLikeCount());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
            intent.putExtra("eventID", event.getEventID() + "");

            view.getContext().startActivity(intent);
        }); 

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, count;
        ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.headerText);
            description = itemView.findViewById(R.id.smallerText);
            image = itemView.findViewById(R.id.imageView);
            count = itemView.findViewById(R.id.smallerText2);



        }
    }
}
