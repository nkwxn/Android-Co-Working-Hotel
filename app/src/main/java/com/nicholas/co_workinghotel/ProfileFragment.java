package com.nicholas.co_workinghotel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    View v;
    TextView txtName, txtUsername, txtPhoneNum, txtBalance;
    MaterialCardView mcvTopUp, mcvLogOut;
    DBHelper helper;
    long UserID;

    // SharedPreferences to clear recently viewed and
    SharedPreferences spref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainUserActivity activity = (MainUserActivity) getActivity();
        UserID = activity.getUserID();
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        initData();
        return v;
    }

    private void initUI() {
        mcvTopUp = v.findViewById(R.id.btnTopUp);
        mcvLogOut = v.findViewById(R.id.btnLogOut);
        txtName = v.findViewById(R.id.userFullName);
        txtUsername = v.findViewById(R.id.userUsrnm);
        txtPhoneNum = v.findViewById(R.id.usrPhoneNum);
        txtBalance = v.findViewById(R.id.balance);

        spref = this.getActivity().getSharedPreferences("cwHotels", Context.MODE_PRIVATE);

        helper = new DBHelper(getContext());

        mcvTopUp.setOnClickListener(this);
        mcvLogOut.setOnClickListener(this);
    }

    private void initData() {
        Cursor userInfo = helper.getUsernameBalance(UserID);
        userInfo.moveToFirst();
        String name = userInfo.getString(0);
        String username = userInfo.getString(1);
        String balance = userInfo.getString(2);
        txtName.setText(name);
        txtUsername.setText(username);
        txtBalance.setText("Rp " + balance);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTopUp:
                Intent i = new Intent(getContext(), TopUpActivity.class);
                i.putExtra("userID", UserID);
                startActivity(i);
                break;
            case R.id.btnLogOut:
                Intent il = new Intent(getContext(), LoginActivity.class);
                editor = spref.edit();
                editor.putString("loginkey", "");
                editor.putString("recentview", "");
                editor.apply();
                startActivity(il);
                getActivity().finish();
                break;
        }
    }
}
