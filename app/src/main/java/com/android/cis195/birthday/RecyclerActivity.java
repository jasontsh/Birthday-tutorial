package com.android.cis195.birthday;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecyclerActivity extends AppCompatActivity {

    private final String MONTH_POSTFIX = "_m";
    private final String DAY_POSTFIX = "_d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        User user = getIntent().getParcelableExtra(getString(R.string.users));

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        Set<String> usernames = sp.getStringSet(getString(R.string.users), new HashSet<String>());

        usernames.add(user.getName());
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(getString(R.string.users), usernames);
        editor.putString(user.getName() + MONTH_POSTFIX, user.getMonth());
        editor.putInt(user.getName() + DAY_POSTFIX, user.getDay());
        editor.apply();

        ArrayList<User> users = new ArrayList<>();

        for (String name : usernames) {
            String month = sp.getString(name + MONTH_POSTFIX, "");
            int day = sp.getInt(name + DAY_POSTFIX, 1);
            User nuser = new User(name, month, day);
            users.add(nuser);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerAdapter adapter = new RecyclerAdapter(users, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
