package com.pkr.erp.db;

import android.provider.BaseColumns;

/**
 * Created by Prashant on 5/26/2017.
 */

public class TaskContract {

    public static final String DB_NAME = "com.pkr.erp.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "Events";
        public static final String COL_DATE = "date";
        public static final String COL_DAY = "day";
        public static final String COL_MONTH = "month";
        public static final String COL_YEAR = "year";
        public static final String COL_DESC = "description";
    }
}
