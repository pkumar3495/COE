package com.pkr.erp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkr.erp.db.TaskContract;
import com.pkr.erp.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView eventsListView;
    TextView todaysDateTextView, todaysEvent;
    int day, monthBuffer, month, year, dateToday;
    String todaysDate, dateTodayBuffer, todaysEventBuffer = "No Event, Normal classes";
    DatePickerDialog datePickerDialog;
    TaskDbHelper mHelper = new TaskDbHelper(this);
    List<String> listToPopulate = new ArrayList<>();
    String dayLeadingBuffer, monthLeadingBuffer;
    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventsListView = (ListView) findViewById(R.id.events_list);
        todaysDateTextView = (TextView) findViewById(R.id.todays_date);
        todaysEvent = (TextView) findViewById(R.id.todays_event);
//        todaysEvent.setText("No Event, Normal classes");

        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10)
            dayLeadingBuffer = "0" + day;
        else
            dayLeadingBuffer = String.valueOf(day);
        monthBuffer = c.get(Calendar.MONTH);
        month = monthBuffer + 1;
        if (month < 10)
            monthLeadingBuffer = "0" + month;
        else
            monthLeadingBuffer = String.valueOf(month);
        year = c.get(Calendar.YEAR);
        dateTodayBuffer = year + "" + monthLeadingBuffer + "" + dayLeadingBuffer;
        dateToday = Integer.parseInt(dateTodayBuffer);

        todaysDate = dayLeadingBuffer + " - " + monthLeadingBuffer + " - " + year;
        todaysDateTextView.setText(todaysDate);

        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_event:
                Calendar c = Calendar.getInstance();
                int dayR = c.get(Calendar.DAY_OF_MONTH);
                final int monthR = c.get(Calendar.MONTH)+1;
                final int yearR = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Intent intent = new Intent(getApplicationContext(), description.class);
                                intent.putExtra("day", dayOfMonth);
                                intent.putExtra("month", month+1);
                                intent.putExtra("year", year);
                                if (yearR == year-1 || yearR == year || yearR == year+1){
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Please select valid date :(", Toast.LENGTH_SHORT).show();
                            }
                        }, dayR, monthR, yearR);
                datePickerDialog.updateDate(yearR, c.get(Calendar.MONTH), dayR);
                datePickerDialog.show();
                return true;
            case R.id.refresh:
                populateList();
                Toast.makeText(getApplicationContext(), "Page updated :)", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Intent intent = new Intent(getApplicationContext(), settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void populateList(){
        SQLiteDatabase readDb = mHelper.getReadableDatabase();
        Cursor readCursor = readDb.rawQuery("SELECT * FROM " + TaskContract.TaskEntry.TABLE + " ORDER BY " + TaskContract.TaskEntry.COL_DATE + " ASC", null);
        while (readCursor.moveToNext()){
            int index = readCursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            int index1 = readCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAY);
            int index2 = readCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTH);
            int index3 = readCursor.getColumnIndex(TaskContract.TaskEntry.COL_YEAR);
            int index4 = readCursor.getColumnIndex(TaskContract.TaskEntry.COL_DESC);
            listToPopulate.add("(" + readCursor.getInt(index1) + "/" + readCursor.getInt(index2) + "/" + readCursor.getInt(index3) + ") - " + readCursor.getString(index4));
            if (readCursor.getInt(index) <= dateToday){
                if (readCursor.getInt(index) == dateToday) {
                    todaysEventBuffer = readCursor.getString(index4);
                    listToPopulate.remove("(" + readCursor.getInt(index1) + "/" + readCursor.getInt(index2) + "/" + readCursor.getInt(index3) + ") - " + readCursor.getString(index4));
                }
                else
                    listToPopulate.remove("(" + readCursor.getInt(index1) + "/" + readCursor.getInt(index2) + "/" + readCursor.getInt(index3) + ") - " + readCursor.getString(index4));
            }
        }
        readCursor.close();
        readDb.close();

        Object[] st = listToPopulate.toArray();
        for (Object s : st){
            if (listToPopulate.indexOf(s) != listToPopulate.lastIndexOf(s))
                listToPopulate.remove(listToPopulate.lastIndexOf(s));
        }

        todaysEvent.setText(todaysEventBuffer);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listToPopulate);
        eventsListView.setAdapter(adapter);
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit)
            finish(); //finish activity
        else{
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
}
