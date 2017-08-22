package com.pkr.erp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prashant on 5/26/2017.
 */

public class TaskDbHelper extends SQLiteOpenHelper{

    public TaskDbHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_DATE + " NUMBER NOT NULL, " +
                TaskContract.TaskEntry.COL_DAY + " NUMBER NOT NULL, " +
                TaskContract.TaskEntry.COL_MONTH + " NUMBER NOT NULL, " +
                TaskContract.TaskEntry.COL_YEAR + " NUMBER NOT NULL, " +
                TaskContract.TaskEntry.COL_DESC + " TEXT NOT NULL);";

        String insertIntoTable = "INSERT INTO " + TaskContract.TaskEntry.TABLE + " (" + TaskContract.TaskEntry.COL_DATE + "," + TaskContract.TaskEntry.COL_DAY + "," + TaskContract.TaskEntry.COL_MONTH + "," + TaskContract.TaskEntry.COL_YEAR + "," + TaskContract.TaskEntry.COL_DESC + ") VALUES\n" +
                "(20170702,02,07,2017,'Sunday')," +
                "(20170709,09,07,2017,'Sunday')," +
                "(20170716,16,07,2017,'Sunday')," +
                "(20170709,09,07,2017,'Faculty Seminar : MATHS Dept')," +

                "(20170729,29,07,2017,'Improvement Test')," +
                "(20170530,30,05,2017,'Improvement Test')," +
                "(20170531,31,05,2017,'Improvement Test')," +
                "(20170601,01,06,2017,'Improvement Test')," +
                "(20170602,02,06,2017,'Lab IAT')," +
                "(20170603,03,06,2017,'Graduation Day')," +
                "(20170604,04,06,2017,'Sunday')," +
                "(20170605,05,06,2017,'Lab IAT')," +
                "(20170606,06,06,2017,'Lab IAT')," +
                "(20170607,07,06,2017,'Lab IAT')," +
                "(20170608,08,06,2017,'Lab IAT')," +
                "(20170610,10,06,2017,'Project Final review 6th sem - MCA Dept.')," +
                "(20170611,11,06,2017,'Sunday')," +
                "(20170617,17,06,2017,'Project Review 6th sem - MCA Dept.')," +
                "(20170618,18,06,2017,'Sunday')," +
                "(20170620,20,06,2017,'Lab maintenance & stock verification (Till 30 june)')," +
                "(20170624,24,06,2017,'Project Final review 6th sem - MCA Dept.')," +
                "(20170625,25,06,2017,'Sunday')," +
                "(20170626,26,06,2017,'Ramzan/Idul Fitr');";

        db.execSQL(createTable);
        db.execSQL(insertIntoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
