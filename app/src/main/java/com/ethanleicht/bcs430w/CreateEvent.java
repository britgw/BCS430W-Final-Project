package com.ethanleicht.bcs430w;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

public class CreateEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        int user = getIntent().getIntExtra("userid", 0);

        Spinner title = findViewById(R.id.moviePicker);
        Button add = findViewById(R.id.addEventConfirm);

        String[] items = new String[]{"Kung Fu Panda", "Die Hard", "Twilight", "GhostBusters"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        title.setAdapter(adapter);
        title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add.setOnClickListener(l -> {
                    DatePickerFragment dateFragment = new DatePickerFragment();
                    TimePickerFragment timeFragment = new TimePickerFragment();
                    dateFragment.title = items[position];
                    dateFragment.user = user;
                    dateFragment.show(getSupportFragmentManager(), "datePicker");
                    timeFragment.show(getSupportFragmentManager(), "timePicker");
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}