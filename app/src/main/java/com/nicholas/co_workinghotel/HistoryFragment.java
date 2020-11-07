package com.nicholas.co_workinghotel;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    View v;
    long UserID;
    RecyclerView rvHistory;
    Button btnClear;
    LinearLayout ll404;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history, container, false);
        MainUserActivity activity = (MainUserActivity) getActivity();
        UserID = activity.getUserID();
        initUI();
        initData();
        rvHistory.addItemDecoration(new SpacesItemDecoration(10, 1));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // to show an alert interaction
                builder.setTitle("Clear Transaction History"); // title
                builder.setMessage("Are you sure to clear your transaction history? \nThis action cannot be undone!");

                // Yes button clicked
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteHistory(UserID);
                        initData();
                        Toast.makeText(getContext(), "Transaction History is cleared", Toast.LENGTH_SHORT).show();
                    }
                });

                // No button clicked
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Transaction History is not cleared", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show(); // show the alert
            }
        });
        return v;
    }

    private void initData() {

        // show data to rv
        ArrayList<History> histories = new ArrayList<>();
        Cursor historyinfo = dbHelper.allHistoryUserData(UserID);
        while (historyinfo.moveToNext()) {
            if (histories.size() == 0) {
                historyinfo.moveToFirst();
            }
            History h = new History(historyinfo.getString(0), historyinfo.getString(1), historyinfo.getString(3), historyinfo.getString(4), historyinfo.getString(2));
            histories.add(h);
        }
        HistoriesRVAdapter rvAdapter = new HistoriesRVAdapter(getContext(), histories);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvHistory.setAdapter(rvAdapter);
        rvHistory.setLayoutManager(llm);
        if (histories.size() == 0) {
            ll404.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        rvHistory = v.findViewById(R.id.rvHistory);
        btnClear = v.findViewById(R.id.btnClear);
        ll404 = v.findViewById(R.id.llNotFound);

        dbHelper = new DBHelper(getContext());
    }
}
