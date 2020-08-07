package com.example.contactsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

    ArrayList<UserObject> userList;
    HashSet<String> selectedList;

    private Button mDone;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        mDone = findViewById(R.id.done);

        sqLiteDatabase = this.openOrCreateDatabase("Contacts", MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS contacts (name VARCHAR, phone VARCHAR PRIMARY KEY)");

        setSelectedList();

        userList = new ArrayList<>();
        initializeRecyclerView();
        getContactList();

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItems();
            }
        });
    }

    private void setSelectedList () {

        selectedList = new HashSet<>();
        try {
            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM contacts", null);

            c.moveToFirst();

            int phoneIndex = c.getColumnIndex("phone");

            while(c != null) {
                String phone = c.getString(phoneIndex);
                selectedList.add(phone);
                c.moveToNext();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkItems() {

                for(int i = 0; i < userList.size(); i++) {
                    String name = userList.get(i).getName();
                    String phone = userList.get(i).getPhone();

                    if(userList.get(i).getChecked()) {
                        if(!selectedList.contains(phone)) {
                            String sql = "INSERT INTO contacts VALUES (?, ?)";
                            final SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
                            statement.bindString(1, name);
                            statement.bindString(2, phone);
                            statement.execute();
                        }
                    }
                    else {
                            String sql = "DELETE FROM contacts WHERE phone = ?";
                            SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
                            statement.bindString(1, phone);
                            statement.execute();
                        }
                }
                setSelectedList();
                finish();
                return;
            }



    private void getContactList() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mContact = new UserObject(name, phone);
            if(selectedList.contains(phone))
                mContact.setChecked(true);
            userList.add(mContact);
            mUserListAdapter.notifyDataSetChanged();
        }
    }

    private void initializeRecyclerView() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        mUserListAdapter = new UserListAdapter(userList, selectedList);
        mUserList.setAdapter(mUserListAdapter);
    }
}
