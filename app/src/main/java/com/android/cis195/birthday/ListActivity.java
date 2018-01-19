package com.android.cis195.birthday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ArrayList<User> users = getIntent().getParcelableArrayListExtra(getString(R.string.users));

        ListView listView = (ListView) findViewById(R.id.list);
        ListAdapter adapter = new ListAdapter(this, users);
        listView.setAdapter(adapter);
    }
}
