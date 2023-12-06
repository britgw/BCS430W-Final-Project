package com.ethanleicht.bcs430w;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    protected static String date = "";
    public String title = "";
    public int user = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        try {
            month++;
            date = year + "-" + month + "-" + day;
            String query = "INSERT IGNORE INTO watchparty (movie, time, creator) VALUES ('" +
                    title + "', '" +
                    date + " " +
                    TimePickerFragment.time + "', " +
                    user + ")";
            Log.d("SQL", query);
            SQLConnect con = new SQLConnect(query);
        }catch (Exception e){
            Log.e("SQL", e.toString());
        }
    }


}
