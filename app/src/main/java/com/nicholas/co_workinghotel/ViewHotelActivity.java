package com.nicholas.co_workinghotel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class ViewHotelActivity extends AppCompatActivity {
    AppBarLayout abl;
    CollapsingToolbarLayout ctb;
    MaterialToolbar mtb;
    TextView address, description, hourlyrate, resvDate, resvTime, outDate, outTime, totalPrice, errDate, errTime;
    ImageView htlimg;
    Button btnDatePick, btnTimePick, btnBook;
    TextInputLayout tilDuration;
    EditText etxDuration;
    LinearLayout llBook, llTotal;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    DateFormat datetxt, timetxt;
    long UserID, HotelID;
    String dateTimeIn, dateTimeOut, dateIn, timeIn, dateOut, timeOut;
    int amount, imgsrc;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int vl = R.layout.activity_view_hotel;
        setContentView(vl);

        UserID = getIntent().getLongExtra("userID", 0);
        HotelID = getIntent().getLongExtra("hotelID", 0);
        imgsrc = getIntent().getIntExtra("imgsrc", 0);

        initUI();
        Cursor hotel = dbHelper.getSpecificHotel(HotelID);
        hotel.moveToFirst();

        final int hRate = Integer.parseInt(hotel.getString(4));

        ctb.setTitle(hotel.getString(1));
        address.setText(hotel.getString(3));
        description.setText(hotel.getString(2));
        hourlyrate.setText("Rp " + hRate + "/hour");

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        datetxt = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        timetxt = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);

        // Date picker
        btnDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar today = Calendar.getInstance();
                dpd = new DatePickerDialog(ViewHotelActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(i, i1, i2);
                        boolean dateValidationNoYesterday = (today.get(Calendar.YEAR) < newDate.get(Calendar.YEAR) || today.get(Calendar.DAY_OF_YEAR) < newDate.get(Calendar.DAY_OF_YEAR));
                        boolean yearValYesterday = today.get(Calendar.YEAR) > newDate.get(Calendar.YEAR);
                        if (!dateValidationNoYesterday) {
                            btnTimePick.setEnabled(false);
                            errDate.setVisibility(View.VISIBLE);
                        } else if (yearValYesterday) {
                            btnTimePick.setEnabled(false);
                            errDate.setVisibility(View.VISIBLE);
                        } else {
                            btnTimePick.setEnabled(true);
                            errDate.setVisibility(View.INVISIBLE);
                        }
                        resvDate.setText(datetxt.format(newDate.getTime()) + "");
                        dateIn = dateFormat.format(newDate.getTime());
                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        // Time picker
        btnTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar today = Calendar.getInstance();
                tpd = new TimePickerDialog(ViewHotelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Calendar newDate = Calendar.getInstance();

                        tilDuration.setEnabled(true);

                        newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), i, i1);
                        resvTime.setText(timetxt.format(newDate.getTime()) + "");
                        timeIn = timeFormat.format(newDate.getTime());
                        dateTimeIn = dateIn + " " + timeIn;
                    }
                }, today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), true);
                tpd.show();
            }
        });

        // When duration text field filled
        etxDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String duration = etxDuration.getText().toString();
                if (duration.length() == 0) {
                    tilDuration.setError(null);
                    llBook.setVisibility(View.GONE);
                } else if (duration.equals("0")) {
                    tilDuration.setError("Cannot be zero");
                    llBook.setVisibility(View.GONE);
                } else {
                    // Tampilin section + ngitung ongkos total + waktu exit
                    int drtn = Integer.parseInt(duration);
                    tilDuration.setError(null);
                    if (resvDate.getText().toString().equals("") && resvTime.getText().toString().equals("")) {
                        Toast.makeText(ViewHotelActivity.this, "Please choose start date and time!", Toast.LENGTH_SHORT).show();
                    } else {
                        llBook.setVisibility(View.VISIBLE);
                        amount = hRate * drtn;
                        totalPrice.setText("Rp" + amount + "");
                        SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar dateStart = Calendar.getInstance(), dateEnd = Calendar.getInstance();
                        try {
                            dateStart.setTime(datetimeformat.parse(dateTimeIn));
                            dateEnd = dateStart;
                            dateEnd.add(Calendar.HOUR_OF_DAY, + drtn);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dateTimeOut = datetimeformat.format(dateEnd.getTime());
                        String[] dateArray = dateTimeOut.split(" ");
                        dateOut = dateArray[0];
                        timeOut = dateArray[1];
                        outDate.setText(datetxt.format(dateEnd.getTime()) + "");
                        outTime.setText(timetxt.format(dateEnd.getTime()) + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Booking
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateTimeIn.equals("") || dateTimeOut.equals("")) {
                    Toast.makeText(ViewHotelActivity.this, "Please Confirm Duration!", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.getUserBalance(UserID) < amount) {
                        Toast.makeText(ViewHotelActivity.this, "Insufficient Balance!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewHotelActivity.this, "Hotel Room Booked Successfully!", Toast.LENGTH_SHORT).show();
                        dbHelper.insertTransaction(HotelID, UserID, dateTimeIn, dateTimeOut, amount);
                        finish();
                    }
                }
            }
        });
    }

    private void initUI() {
        abl = findViewById(R.id.appBarLayout);
        ctb = findViewById(R.id.CollTool);
        mtb = findViewById(R.id.toolbar);
        address = findViewById(R.id.addressPlaceHolder);
        description = findViewById(R.id.descPlaceHolder);
        hourlyrate = findViewById(R.id.pricePlaceH);
        resvDate = findViewById(R.id.datePlc);
        resvTime = findViewById(R.id.timePlc);
        outDate = findViewById(R.id.dateOutPlc);
        outTime = findViewById(R.id.timeOutPlc);
        errDate = findViewById(R.id.errDate);
        errTime = findViewById(R.id.errTime);
        totalPrice = findViewById(R.id.totalPlc);
        btnDatePick = findViewById(R.id.btnPickDate);
        btnTimePick = findViewById(R.id.btnPickTime);
        btnBook = findViewById(R.id.btnBookNow);
        tilDuration = findViewById(R.id.tilDuration);
        etxDuration = findViewById(R.id.etxDuration);
        llBook = findViewById(R.id.checkOutTimeDetail);
        htlimg = findViewById(R.id.imgplc);

        dbHelper = new DBHelper(getApplicationContext());

        mtb.setNavigationIcon(R.drawable.ic_back);
        mtb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        htlimg.setImageResource(imgsrc);

        errTime.setVisibility(View.INVISIBLE);
        errDate.setVisibility(View.INVISIBLE);
        llBook.setVisibility(View.GONE);
        btnTimePick.setEnabled(false);
        etxDuration.setEnabled(false);
    }
}