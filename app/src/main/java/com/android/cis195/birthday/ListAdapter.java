package com.android.cis195.birthday;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jason on 10/5/2017.
 */

public class ListAdapter extends ArrayAdapter<User> {

    LayoutInflater inflater;
    public ListAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.birthday_item, users);
        inflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.birthday_item, parent, false);
        }
        User user = getItem(position);
        String text = user.getName() + " Birthday: " + user.getMonth() + " " + user.getDay();
        ((TextView) view.findViewById(R.id.result)).setText(text);
        return view;
    }
}
