package com.android.cis195.birthday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText nameEt = (EditText) findViewById(R.id.name);
        final Spinner monthSp = (Spinner) findViewById(R.id.month);

        monthSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                month = getResources().getStringArray(R.array.months)[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month = "";
            }
        });
        final EditText dayEt = (EditText) findViewById(R.id.day);

        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                int day = Integer.parseInt(dayEt.getText().toString());
                User user = new User(name, month, day);
                Intent intent = new Intent(view.getContext(), RecyclerSQLActivity.class);
                intent.putExtra(getString(R.string.users), user);
                startActivity(intent);
            }
        });
    }
}
