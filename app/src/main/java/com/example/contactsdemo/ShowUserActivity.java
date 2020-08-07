package com.example.contactsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ShowUserActivity extends AppCompatActivity {

    private RecyclerView mSelectedUsers;
    private RecyclerView.LayoutManager mSelectUsersLayoutManager;
    private RecyclerView.Adapter mSelectUsersAdapter;

    ArrayList<UserObject> selectedUsersList;

    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        selectedUsersList = new ArrayList<>();

        try {
            sqLiteDatabase = this.openOrCreateDatabase("Contacts", MODE_PRIVATE, null);
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM contacts ORDER BY name", null);

            int nameIndex = c.getColumnIndex("name");
            int phoneIndex = c.getColumnIndex("phone");
            c.moveToFirst();
            while(c != null) {
                String name = c.getString(nameIndex);
                String phone = c.getString(phoneIndex);
                selectedUsersList.add(new UserObject(name, phone));
                c.moveToNext();
            }
            mSelectUsersAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        mSelectedUsers = findViewById(R.id.selectedList);
        mSelectedUsers.setHasFixedSize(false);
        mSelectedUsers.setNestedScrollingEnabled(false);
        mSelectUsersLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mSelectedUsers.setLayoutManager(mSelectUsersLayoutManager);
        mSelectedUsers.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        mSelectUsersAdapter = new SelectUsersAdapter(selectedUsersList);
        mSelectedUsers.setAdapter(mSelectUsersAdapter);

    }
}
