package com.nicholas.co_workinghotel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class HotelsRVAdapter extends RecyclerView.Adapter<HotelsRVAdapter.ViewHolder> {
    public Context c;
    public ArrayList<Hotel> hotels;

    SharedPreferences spref;
    SharedPreferences.Editor editor;

    public HotelsRVAdapter(Context c, ArrayList<Hotel> hotels) {
        this.c = c;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View hotels = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hotels, parent, false);
        return new ViewHolder(hotels);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Hotel h = hotels.get(position);
        final String hotelName, hotelLoc, hotelPrice;
        final long hotelId, userId;
        final int imgsrc;
        hotelName = h.getHotelName();
        hotelLoc = h.getHotelDesc();
        hotelPrice = h.getHotelPrice();
        hotelId = h.getHotelID();
        userId = h.getUserID();

        holder.txtName.setText(hotelName);
        holder.txtLoc.setText(hotelLoc);
        holder.txtPrice.setText("Rp " + hotelPrice + "/hour");

        switch (hotelName) {
            case "Hotel Menara Peninsula":
                imgsrc = R.drawable.peninsula;
                break;
            case "Hotel Indonesia Kempinski":
                imgsrc = R.drawable.hi_kempinski;
                break;
            case "Hotel Mulia Senayan":
                imgsrc = R.drawable.hm_senayan;
                break;
            case "Hotel Fairmont Jakarta":
                imgsrc = R.drawable.fairmont;
                break;
            default:
                imgsrc = R.drawable.ic_img_placeholder;
                break;
        }
        holder.img.setImageResource(imgsrc);

        holder.mcvHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, ViewHotelActivity.class);
                i.putExtra("hotelID", hotelId);
                i.putExtra("imgsrc", imgsrc);
                i.putExtra("userID", userId);
                spref = c.getSharedPreferences("cwHotels", Context.MODE_PRIVATE);
                editor = spref.edit();
                editor.putString("recentview", Long.toString(hotelId));
                editor.apply();
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView mcvHotels;
        ImageView img;
        TextView txtName, txtLoc, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mcvHotels = itemView.findViewById(R.id.mcvHotel);
            img = itemView.findViewById(R.id.hotelImg);
            txtName = itemView.findViewById(R.id.hotelName);
            txtLoc = itemView.findViewById(R.id.hotelAddress);
            txtPrice = itemView.findViewById(R.id.hotelPrice);
        }
    }
}
