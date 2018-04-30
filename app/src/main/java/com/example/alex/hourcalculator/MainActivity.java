package com.example.alex.hourcalculator;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button addHoursBtn;
    final List<Pair<Integer, Integer>> hoursList = new ArrayList<>();
    EditText hoursSumText;

    int startHour;
    int startMinutes;
    int endHour;
    int endMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hoursSumText = (EditText) findViewById(R.id.hoursSumText);

        final TimePicker startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        startTimePicker.setOnTimeChangedListener(startTimeChangedListener);

        TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
        endTimePicker.setOnTimeChangedListener(endTimeChangedListener);

        addHoursBtn = (Button) findViewById(R.id.addHoursBtn);
        addHoursBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO add check end more than start
                hoursList.add(GetDifferenceBetweenTimes());
                UpdateHours();
            }
        });

        Button resetButton = (Button) findViewById(R.id.resetHoursBtn);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hoursList.clear();
                hoursSumText.setText("Aggiungi un orario");
            }
        });

    }

    private TimePicker.OnTimeChangedListener startTimeChangedListener =
            new TimePicker.OnTimeChangedListener(){
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    startHour = hourOfDay;
                    startMinutes = minute;
                }
            };

    private TimePicker.OnTimeChangedListener endTimeChangedListener =
            new TimePicker.OnTimeChangedListener(){
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    endHour = hourOfDay;
                    endMinutes = minute;
                }
            };

    private void UpdateHours(){
        if(hoursList.size() == 0){
            hoursSumText.setText("Aggiungi un orario");
        }
        else{
            int sum = 0;

            for(Pair<Integer,Integer> pair:hoursList){
                int hours = pair.first;
                int minutes = pair.second;

                int mins = hours * 60 + minutes;
                sum += mins;
            }

            hoursSumText.setText(Integer.toString((int)Math.floor(sum/60))+": " + Integer.toString(sum % 60 ));
        }
    }

    private Pair<Integer,Integer> GetDifferenceBetweenTimes(){
        Date startDate = new Date();
        Date endDate = new Date();

        startDate.setHours(startHour);
        startDate.setMinutes(startMinutes);
        endDate.setHours(endHour);
        endDate.setMinutes(endMinutes);

        long millis = endDate.getTime() - startDate.getTime();

        int differenceHours = (int) millis/(1000 * 60 * 60);
        int differenceMinutes = (int) (millis/(1000*60)) % 60;

        return new Pair<>(differenceHours,differenceMinutes);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        if (requestCode == 0 && data != null) {
            if (resultCode == RESULT_OK) {
                int finalHour = data.getIntExtra("finalHour",0);
                int finalMinutes = data.getIntExtra("finalMinutes", 0);

                hoursList.add(new Pair<>(finalHour, finalMinutes));

                UpdateHours();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }


    }


}
