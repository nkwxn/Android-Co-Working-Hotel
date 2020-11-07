package com.nicholas.co_workinghotel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    View v;
    LinearLayout lRecents;
    RecyclerView rvHotels;
    DBHelper dbHelper;
    MaterialCardView cvRecent;
    TextView tvHotelName, tvHotelAddress, tvHotelPrice;
    ImageView imgHotelPlc;
    long UserID;

    // to get recently viewed hotel
    SharedPreferences spref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainUserActivity activity = (MainUserActivity) getActivity();
        UserID = activity.getUserID();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        initData();
        rvHotels.addItemDecoration(new SpacesItemDecoration(10, 0));
        return v;
    }

    private void initUI() {
        lRecents = v.findViewById(R.id.recentView);
        rvHotels = v.findViewById(R.id.rvHotels);
        dbHelper = new DBHelper(getContext());
        spref = this.getContext().getSharedPreferences("cwHotels", Context.MODE_PRIVATE);
        lRecents.setVisibility(View.GONE);

        cvRecent = v.findViewById(R.id.recentHotel);
        tvHotelName = v.findViewById(R.id.hotelName);
        tvHotelAddress = v.findViewById(R.id.hotelAddress);
        tvHotelPrice = v.findViewById(R.id.hotelPrice);
        imgHotelPlc = v.findViewById(R.id.hotelImage);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Cursor htlCount = dbHelper.getHotelCount();
        htlCount.moveToFirst();
        int hotelcount = htlCount.getInt(0);

        // Insert hotel data to db if no data
        if (hotelcount == 0) {
            String[][] hotels = {
                    {
                        "Hotel Menara Peninsula",
                            "The Menara Peninsula Hotel Jakarta is a " +
                            "fantastic hotel for business travelers in Jakarta " +
                            "which enjoys an exceptional location on the edge of" +
                            " the main business district in West Jakarta, " +
                            "placing it within easy reach to most multinational companies " +
                            "and government offices and shopping malls in the Indonesian capital. " +
                            "Ideal for business travelers, it is also a great value-for-money option for leisure travelers thanks to its excellent facilities and efficient and friendly staff.",
                            "Letjen S. Parman St No.Kav. 78, RT.6/RW.3, Slipi, Palmerah, West Jakarta City, Jakarta 11410",
                            "20000"
                    },
                    {
                            "Hotel Indonesia Kempinski",
                            "Enter our world and discover the city’s landmarks and its history, along with our unique European luxury service. Hotel Indonesia Kempinski Jakarta features 289 rooms and suites, " +
                                    "an array of gastronomic restaurants and shops, state-of-the-art meeting rooms, lavish ballrooms and a traditional spa. " +
                                    "Whether you are in the mood for a refreshing swim on top of Jakarta or just want to enjoy your sophisticated and stylish room, Hotel Indonesia Kempinski Jakarta awaits you.",
                            "Jl. M.H. Thamrin No.1, RT.1/RW.5, Menteng, Kec. Menteng, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10310",
                            "65000"
                    },
                    {
                            "Hotel Mulia Senayan",
                            "Hotel Mulia Senayan, Jakarta features spacious rooms that radiate an elegant atmosphere, complete with warm hospitality that is unique to Indonesia. The Suites at Hotel Mulia Senayan, Jakarta boast opulent suites as well as your very own butler, ready to cater to your every need. Both experiences promise a level of service that goes above and beyond, redefining luxury at every step. The award-winning hotel boasts an array of premium amenities to satisfy every guest. Taste the finest of Indonesian, Asian and Continental cuisines at one of the nine restaurants and bars at the hotel. Pamper yourself to a blissful state at the Mulia Spa or boost your energy at the spacious Fitness Center overlooking Jakarta.",
                            "Jl. Asia Afrika No.6, RT.1/RW.3, Gelora, Kecamatan Tanah Abang, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10270",
                            "85000"
                    },
                    {
                            "Hotel Fairmont Jakarta",
                            "Luxury in the Heart of Jakarta. Fairmont Jakarta is known for its unbeatable location in Senayan, with very close proximity to the Indonesia Stock Exchange, Jakarta Convention Center and linked directly to one of the city’s most stylish shopping venues, Plaza Senayan and Sentral Senayan office towers. ",
                            "Jl. Asia Afrika No.8, Gelora, JAKARTA, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10270",
                            "115000"
                    }
            };
            for (int i = 0; i < hotels.length; i++) {
                dbHelper.insertHotels(hotels[i][0], hotels[i][1], hotels[i][2], hotels[i][3]);
            }
        }

        // show data to rv
        ArrayList<Hotel> hotels = new ArrayList<>();
        Cursor hotelsInfo = dbHelper.getAllHotels();
        while (hotelsInfo.moveToNext()) {
            if (hotels.size() == 0) {
                hotelsInfo.moveToFirst();
            }
            Hotel h = new Hotel(hotelsInfo.getLong(0), UserID, hotelsInfo.getString(1), hotelsInfo.getString(3), hotelsInfo.getString(4));
            hotels.add(h);
        }

        HotelsRVAdapter rvAdapter = new HotelsRVAdapter(getContext(), hotels);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvHotels.setAdapter(rvAdapter);
        rvHotels.setLayoutManager(sglm);

        // Show recently viewed hotel
        String recenthotel = spref.getString("recentview", "");
        if (recenthotel.equals("")) {
            lRecents.setVisibility(View.GONE);
        } else {
            final long hotelID = Long.parseLong(recenthotel),
                    userID = UserID;
            lRecents.setVisibility(View.VISIBLE);
            Cursor hotel = dbHelper.getSpecificHotel(hotelID);
            hotel.moveToFirst();
            String hotelName = hotel.getString(1),
                    hotelAddress = hotel.getString(3),
                    hotelPrice = hotel.getString(4);
            final int imgsrc;
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
            imgHotelPlc.setImageResource(imgsrc);
            tvHotelName.setText(hotelName);
            tvHotelAddress.setText(hotelAddress);
            tvHotelPrice.setText("Rp " + hotelPrice + "/hour");
            cvRecent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent im = new Intent(getContext(), ViewHotelActivity.class);
                    im.putExtra("hotelID", hotelID);
                    im.putExtra("imgsrc", imgsrc);
                    im.putExtra("userID", userID);
                    startActivity(im);
                }
            });
        }
    }
}
