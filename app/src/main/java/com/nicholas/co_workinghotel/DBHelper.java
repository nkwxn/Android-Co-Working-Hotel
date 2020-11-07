package com.nicholas.co_workinghotel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {
    // Nama Database dan Tabel
    public static final String DB_NAME = "db_cwhotel",
            TABLE_USER = "User",
            TABLE_HOTELS = "Hotel",
            TABLE_TRANSACTION = "UserHotelTransaction";

    // Nama column untuk tabel user
    public static final String user_id = "UserID",
            user_name = "Name",
            user_username = "UserName",
            user_pwd = "Password",
            user_phone_num = "PhoneNumber",
            user_gender = "Gender",
            user_balance = "Balance";

    // Nama column untuk tabel transaction
    public static final String transaction_id = "TransactionID",
            transaction_user_id = "UserID",
            transaction_hotel_id = "HotelID",
            transaction_date = "TransactionDate",
            transaction_in_date = "CheckInDate",
            transaction_out_date = "CheckOutDate";

    // Nama column untuk tabel transaction
    public static final String hotel_id = "HotelID",
            hotel_name = "Name",
            hotel_desc = "Description",
            hotel_loc = "Address",
            hotel_hourly_rate = "PricePerHour";

    private SQLiteDatabase database;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 3);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qUser, qHotel, qTrans;

        // Query create table user
        qUser = "CREATE TABLE " + TABLE_USER + " (" +
                user_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                user_name + " TEXT NOT NULL, " +
                user_username + " TEXT NOT NULL, " +
                user_pwd + " TEXT NOT NULL, " +
                user_phone_num + " TEXT NOT NULL, " +
                user_gender + " TEXT NOT NULL, " +
                user_balance + " INTEGER NOT NULL)";

        qHotel = "CREATE TABLE " + TABLE_HOTELS + " (" +
                hotel_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                hotel_name + " TEXT NOT NULL, " +
                hotel_desc + " TEXT NOT NULL, " +
                hotel_loc + " TEXT NOT NULL, " +
                hotel_hourly_rate + " TEXT NOT NULL)";

        qTrans = "CREATE TABLE " + TABLE_TRANSACTION + " (" +
                transaction_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                transaction_user_id + " INTEGER NOT NULL, " +
                transaction_hotel_id + " INTEGER NOT NULL, " +
                transaction_date + " TEXT NOT NULL, " +
                transaction_in_date + " TEXT NOT NULL, " +
                transaction_out_date + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + transaction_user_id + ") " +
                "REFERENCES " + TABLE_USER + " (" + user_id + "), " +
                "FOREIGN KEY (" + transaction_hotel_id + ") " +
                "REFERENCES " + TABLE_HOTELS + " (" + hotel_id + "))";

        sqLiteDatabase.execSQL(qUser);
        sqLiteDatabase.execSQL(qHotel);
        sqLiteDatabase.execSQL(qTrans);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
    }

    public void insertTransaction
            (long hotelID, long userID, String inDate,
             String outDate, int payamount) {
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance(); // get now date and time
        String trandate = sdf.format(c.getTime());
        cv.put(transaction_user_id, userID);
        cv.put(transaction_hotel_id, hotelID);
        cv.put(transaction_date, trandate);
        cv.put(transaction_in_date, inDate);
        cv.put(transaction_out_date, outDate);
        database.insert(TABLE_TRANSACTION, null, cv);

        int currentBal = getUserBalance(userID);

        // Buat update saldo
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(user_balance, currentBal - payamount);
        database.update(TABLE_USER, cvUpdate, user_id
                + " = " + userID, null);
    }

    public int getUserBalance(long UserID) {
        Cursor c = database.rawQuery("SELECT " + user_balance +
                " FROM " + TABLE_USER + " WHERE " + user_id + " = "
                + UserID, null);
        c.moveToFirst();
        return c.getInt(0);
    }

    // method untuk mendapatkan semua data History
    public Cursor allHistoryUserData(long UserID) {
        Cursor c = database.rawQuery("SELECT "+hotel_name+
                ", "+hotel_hourly_rate+", "+transaction_date+
                ", "+transaction_in_date+", "+transaction_out_date+
                " FROM " + TABLE_TRANSACTION +
                " INNER JOIN " + TABLE_HOTELS + " on " +
                TABLE_HOTELS + "." + hotel_id + " = " +
                TABLE_TRANSACTION + "." + transaction_hotel_id +
                " WHERE " + transaction_user_id + " = " +
                UserID, null);
        return c;
    }

    // method untuk mendapatkan semua data Username + password
    public Cursor allUsernameData() {
        Cursor c = database.rawQuery("SELECT " + user_username +
                ", " + user_pwd + ", " + user_id +
                " FROM " + TABLE_USER, null);
        return c;
    }

    // method untuk mendapatkan Username dan Balance setelah user login
    public Cursor getUsernameBalance(long UserID) {
        Cursor c = database.rawQuery("SELECT " + user_name + ", "
                + user_username + ", " + user_balance + ", " + user_pwd
                + " FROM " + TABLE_USER + " WHERE " + user_id + " = "
                + UserID, null);
        return c;
    }

    // method untuk memasukkan data user (sign up)
    public void insertUserData(ContentValues cv) {
        database.insert(TABLE_USER, null, cv);
    }

    public Cursor getHotelCount() {
        Cursor c = database.rawQuery("SELECT COUNT(*)" +
                " FROM " + TABLE_HOTELS, null);
        return c;
    }

    public Cursor getAllHotels() {
        Cursor c = database.rawQuery("SELECT *" +
                " FROM " + TABLE_HOTELS, null);
        return c;
    }

    public Cursor getSpecificHotel(long hotelID) {
        Cursor c = database.rawQuery("SELECT * " +
                " FROM " + TABLE_HOTELS +
                " WHERE " + hotel_id + " = " + hotelID, null);
        return c;
    }

    public void insertHotels
            (String name, String desc,
             String address, String priceperh) {
        ContentValues cv = new ContentValues();
        cv.put(hotel_name, name);
        cv.put(hotel_desc, desc);
        cv.put(hotel_loc, address);
        cv.put(hotel_hourly_rate, priceperh);
        database.insert(TABLE_HOTELS, null, cv);
    }

    // method untuk memperbarui saldo dompet
    public void updateUserBalance
    (ContentValues cv, long userID) {
        database.update(TABLE_USER, cv, user_id
                + " = " + userID, null);
    }

    // method untuk menghapus semua history milik 1 user
    public void deleteHistory(long userID) {
        database.delete(TABLE_TRANSACTION, transaction_user_id
                + " = " + userID, null);
    }
}
