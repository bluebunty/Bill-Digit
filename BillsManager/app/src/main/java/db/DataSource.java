package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import util.BillConstants;


public class DataSource implements BillConstants{
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private static DataSource dataSource = null;

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /* add data to table */
    public long addData(String tableName, ContentValues cv) {
        return database.insert(tableName, null, cv);
    }

    public void update(String tableName, String column, int value, ContentValues cv) {
        //cv.put(MySQLiteHelper.COLUMN_LINK_TO, newLinkTo);
        database.update(tableName, cv, column + "=" + value, null);
    }
    public Cursor getAllBills()
    {
        Cursor cursor = database.rawQuery("SELECT * FROM "+MySQLiteHelper.TABLE_MASTER+" WHERE "+MySQLiteHelper.COLUMN_CATEGORY_TYPE+" = "+Element_Type.BILL.ordinal(),new String[]{});
        return cursor;
    }
    public Cursor getAllItems()
    {
        Cursor cursor = database.rawQuery("SELECT * FROM "+MySQLiteHelper.TABLE_MASTER+" WHERE "+MySQLiteHelper.COLUMN_CATEGORY_TYPE+" = "+Element_Type.ITEM.ordinal(),new String[]{});
        return cursor;
    }
    public Cursor getAllItemsData(int itemId)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_CATEGORY_DETAIL + " WHERE " + MySQLiteHelper.COLUMN_CATEGORY_ID + " = " + itemId, new String[]{});
        return cursor;
    }
    public Cursor getDueBills()
    {
        Cursor cursor = database.rawQuery("SELECT * FROM "+MySQLiteHelper.TABLE_CATEGORY_DETAIL+" where "+
                MySQLiteHelper.COLUMN_TYPE+" = "+Element_Type.BILL_DUE_DATE.ordinal()+" and "+MySQLiteHelper.COLUMN_VALUE +
                " BETWEEN datetime('now', 'localtime') AND datetime('now','5 days')"
                ,new String[]{});

        //SELECT * FROM statistics WHERE date BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime');
        return cursor;
    }
    public Cursor getFiltersKeys()
    {
        return database.rawQuery("SELECT DISTINCT "+MySQLiteHelper.COLUMN_TYPE+" FROM "
                +MySQLiteHelper.TABLE_CATEGORY_DETAIL+" where "+MySQLiteHelper.COLUMN_TYPE
                +" != "+Element_Type.BILL.ordinal()+" and "+
                MySQLiteHelper.COLUMN_TYPE +" != "+Element_Type.HASH_TAG.ordinal()+" and "+
                MySQLiteHelper.COLUMN_TYPE +" != "+Element_Type.IMAGE.ordinal(),new String[]{});
    }
    public Cursor getFilters(int type)
    {
        return database.rawQuery("SELECT *  FROM "+MySQLiteHelper.TABLE_CATEGORY_DETAIL+" where "
                +MySQLiteHelper.COLUMN_TYPE+" = "+type,new String[]{});
    }
}  