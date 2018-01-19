package com.android.cis195.birthday;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Jason on 10/13/2017.
 */

public class RecyclerSQLActivity extends AppCompatActivity {

    public static final String TAG = "RecyclerSQLActivity";
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        User user = getIntent().getParcelableExtra(getString(R.string.users));

        DBHelper dbhelper = new DBHelper(this);
        dbhelper.insertUser(user);

        Collection<User> set = dbhelper.getUsers().values();

        ArrayList<User> users = new ArrayList<>(set);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new RecyclerAdapter(users, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d(TAG, "I am here!");
        recyclerView.setAdapter(adapter);
         }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Can't proceed if you deny the permission!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; i++) {
            final RecyclerAdapter.UserViewHolder holder = (RecyclerAdapter.UserViewHolder)
                    recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (holder.tv.getTag() != null) {
                User user = (User) holder.tv.getTag();
                adapter.takePhotoForUser(user);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RecyclerAdapter.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show();
            } else {
                for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; i++) {
                    final RecyclerAdapter.UserViewHolder holder = (RecyclerAdapter.UserViewHolder)
                            recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (holder.tv.getTag() != null) {
                        holder.tv.setTag(null);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }


}
