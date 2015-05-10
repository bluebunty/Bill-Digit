package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contents.db";
    private static final int DATABASE_VERSION = 1;

    //Database columns
    public static final String TABLE_MASTER = "table_master";
    public static final String COLUMN_ROW_ID = "auto_increment_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_PARENT_CATEGORY_ID = "parent_category_id";
    public static final String COLUMN_CATEGORY_TYPE = "category_type";
    public static final String COLUMN_TIMESTAMP = "time_stamp";


    public static final String TABLE_CATEGORY_DETAIL = "table_category";
    public static final String COLUMN_ELEMENT_ID = "element_id";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_TYPE = "element_type";

    public static final String COLUMN_FK = "column_fk";
    //Creating database table for screens

    private static final String DATABASE_CREATE_TABLE_MASTER = "create table "
            + TABLE_MASTER + "(" + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PARENT_CATEGORY_ID + " INTEGER, "
            + COLUMN_CATEGORY_TYPE + " text, "
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP );";
    // Creating database table for components

    private static final String DATABASE_CREATE_TABLE_DETAIL = "create table "
            + TABLE_CATEGORY_DETAIL
            + "(" + COLUMN_ELEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_ID + " INTEGER, "
            + COLUMN_KEY + " text, "
            + COLUMN_VALUE + " text, "
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_TYPE + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TABLE_MASTER);
        database.execSQL(DATABASE_CREATE_TABLE_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_DETAIL);
        onCreate(db);

    }

}