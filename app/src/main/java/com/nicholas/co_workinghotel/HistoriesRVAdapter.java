package com.nicholas.co_workinghotel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoriesRVAdapter extends RecyclerView.Adapter<HistoriesRVAdapter.ViewHolder> {
    public Context c;
    public ArrayList<History> histories;

    public HistoriesRVAdapter(Context c, ArrayList<History> histories) {
        this.c = c;
        this.histories = histories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View histories = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        return new ViewHolder(histories);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History h = histories.get(position);
        String hotelName, tranDateTime, tranInDateTime, tranOutDateTime;
        long duration, diffintime = 0;
        hotelName = h.getHotelName();
        tranDateTime = h.getTranDate();
        tranInDateTime = h.getInDate();
        tranOutDateTime = h.getOutDate();

        String tDate = "", tTime = "", iDate = "", iTime = "", oDate = "", oTime = "";
        Locale locale = new Locale("en", "US");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat date = DateFormat.getDateInstance(DateFormat.FULL, locale);
        DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT);
        Date newTDate = null, newIDate = null, newODate = null;

        try {
            newTDate = sdf.parse(tranDateTime);
            newIDate = sdf.parse(tranInDateTime);
            newODate = sdf.parse(tranOutDateTime);
            tDate = date.format(newTDate);
            tTime = time.format(newTDate);
            iDate = date.format(newIDate);
            iTime = time.format(newIDate);
            oDate = date.format(newODate);
            oTime = time.format(newODate);
            diffintime = newODate.getTime() - newIDate.getTime();
        } catch (Exception e) {
            System.err.println(e);
        }

        duration = (diffintime
                / (1000 * 60 * 60))
                % 24;

        holder.tvHotelName.setText(hotelName);
        holder.tvTranDate.setText(tDate);
        holder.tvTranTime.setText(tTime);
        holder.tvInDate.setText("Start: " + iDate);
        holder.tvInTime.setText(iTime);
        holder.tvOutDate.setText("End: " + oDate);
        holder.tvOutTime.setText(oTime);
        holder.tvDuration.setText(duration + " hours");
        int idur = (int) duration;
        int price = idur * Integer.parseInt(h.getHotelHourlyRate());
        holder.tvPrice.setText("Rp " + price);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvTranDate, tvTranTime, tvInDate, tvInTime,tvOutDate, tvOutTime, tvDuration, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHotelName = itemView.findViewById(R.id.hotelNameH);
            tvTranDate = itemView.findViewById(R.id.txtTranDateH);
            tvTranTime = itemView.findViewById(R.id.txtTranTimeH);
            tvInDate = itemView.findViewById(R.id.txtStartDate);
            tvInTime = itemView.findViewById(R.id.txtStartTime);
            tvOutDate = itemView.findViewById(R.id.txtEndDate);
            tvOutTime = itemView.findViewById(R.id.txtEndTime);
            tvDuration = itemView.findViewById(R.id.txtDuration);
            tvPrice = itemView.findViewById(R.id.txtTotal);
        }
    }
}
