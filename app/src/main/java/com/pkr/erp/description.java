package com.pkr.erp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pkr.erp.db.TaskContract;
import com.pkr.erp.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.List;

public class description extends AppCompatActivity {

    int day, month, year, date, count, dayLeading, monthLeading;
    String dateToTitle, dateString, desc, dayLeadingBuffer, monthLeadingBuffer;
    Button addButton, cancelButton;
    EditText descEditText;
    private TaskDbHelper mHelper = new TaskDbHelper(this);
    private static final String TAG = "MainActivity";
    List<String> list = new ArrayList<>();
    Boolean saveFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        logging();

        addButton = (Button) findViewById(R.id.add_action);
        cancelButton = (Button) findViewById(R.id.cancel_action);
        descEditText = (EditText) findViewById(R.id.desc_edit_text);
        descEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        day = getIntent().getExtras().getInt("day");
        if (day < 10)
            dayLeadingBuffer = "0" + day;
        else
            dayLeadingBuffer = String.valueOf(day);
        month = getIntent().getExtras().getInt("month");
        if (month < 10)
            monthLeadingBuffer = "0" + month;
        else
            monthLeadingBuffer = String.valueOf(month);
        year = getIntent().getExtras().getInt("year");
        dateString = year + "" + monthLeadingBuffer + "" + dayLeadingBuffer;
        date = Integer.parseInt(dateString);

        dateToTitle = dayLeadingBuffer + " - " + monthLeadingBuffer + " - " + year;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(dateToTitle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingDuplicate();
                Log.e(TAG, "Save flag : " + saveFlag);
                if (descEditText.getText().toString().equals(""))
                    descEditText.setError("Please enter something");
                else {
                    desc = descEditText.getText().toString();
                    Log.e(TAG, "desc : " + desc);
                    countingTheDb();
                    if (saveFlag) {
                        savingToDb();
                        Toast.makeText(getApplicationContext(), "Event added :)", Toast.LENGTH_SHORT).show();
                    }
                    else
                        descEditText.setError("This date already has an event");
                    logging();
                    countingTheDb();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    if (saveFlag)
                        startActivity(intent);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    public void savingToDb(){
        dayLeading = Integer.parseInt(dayLeadingBuffer);
        monthLeading = Integer.parseInt(monthLeadingBuffer);
        SQLiteDatabase savingDb = mHelper.getWritableDatabase();
        ContentValues savingDbCv = new ContentValues();
        savingDbCv.put(TaskContract.TaskEntry.COL_DATE, date);
        savingDbCv.put(TaskContract.TaskEntry.COL_DAY, dayLeading);
        savingDbCv.put(TaskContract.TaskEntry.COL_MONTH, monthLeading);
        savingDbCv.put(TaskContract.TaskEntry.COL_YEAR, year);
        savingDbCv.put(TaskContract.TaskEntry.COL_DESC, desc);
        savingDb.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                savingDbCv,
                SQLiteDatabase.CONFLICT_REPLACE);
        savingDb.close();
    }

    public void countingTheDb(){
        SQLiteDatabase countingDb = mHelper.getReadableDatabase();
        Cursor countingCursor = countingDb.rawQuery("SELECT COUNT(" + TaskContract.TaskEntry.COL_DESC + ") FROM " + TaskContract.TaskEntry.TABLE + ";",null);
        if (countingCursor.moveToFirst())
            count = countingCursor.getInt(0);
        Log.e(TAG, "count : " + count);
        countingCursor.close();
        countingDb.close();
    }

    public void logging(){
        SQLiteDatabase loggingDb = mHelper.getReadableDatabase();
        Cursor loggingCursor = loggingDb.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_DATE, TaskContract.TaskEntry.COL_DAY, TaskContract.TaskEntry.COL_MONTH, TaskContract.TaskEntry.COL_YEAR, TaskContract.TaskEntry.COL_DESC},
                null, null, null, null, null);
        while (loggingCursor.moveToNext()){
            int index = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            int index1 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_DAY);
            int index2 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_MONTH);
            int index3 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_YEAR);
            int index4 = loggingCursor.getColumnIndex(TaskContract.TaskEntry.COL_DESC);
            Log.e(TAG, "DateDb : " + loggingCursor.getInt(index) +
                    " DayDb : " + loggingCursor.getInt(index1) +
                    " MonthDb : " + loggingCursor.getInt(index2) +
                    " YearDb : " + loggingCursor.getInt(index3) +
                    " DescDb : " + loggingCursor.getString(index4));
        }
        loggingCursor.close();
        loggingDb.close();
    }

    public void checkingDuplicate(){
        SQLiteDatabase duplicateDb = mHelper.getReadableDatabase();
        Cursor duplicateCursor = duplicateDb.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_DATE},
                null, null, null, null, null);
        while (duplicateCursor.moveToNext()){
            int index = duplicateCursor.getColumnIndex(TaskContract.TaskEntry.COL_DATE);
            list.add(duplicateCursor.getInt(index)+"");
        }
        duplicateCursor.close();
        duplicateDb.close();
        if (list.contains(date+""))
            saveFlag = false;
        else
            saveFlag = true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
