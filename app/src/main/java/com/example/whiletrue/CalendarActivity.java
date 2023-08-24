package com.example.whiletrue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Set the appbar title and home button listener
        TextView activityTitle = findViewById(R.id.activityTitle);
        activityTitle.setText("Macro Calendar");

        ImageView homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set the listener for the CalendarView. When a day is pressed, send the date to new activity as extra.
        calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String date = Integer.toString(month) + '/' + Integer.toString(day) + '/' + Integer.toString(year);
                Intent intent = new Intent(CalendarActivity.this, MacroDayActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    // When back button is pressed, intent to MainActivity
    @Override
    public void onBackPressed() {
        if (this.equals(CalendarActivity.this)) {
            Intent newIntent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(newIntent);
        } else {
            super.onBackPressed();
        }
    }
}