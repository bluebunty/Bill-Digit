package com.sd.billsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import alarm.SampleAlarmReceiver;
import alarm.SmsService;
import db.DataSource;
import db.MySQLiteHelper;
import util.BillConstants;


public class MainActivity extends FragmentActivity implements BillConstants {

    private DataSource dataSource;
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm.setAlarm(this);
        Button button = (Button)findViewById(R.id.btnAddBill);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*createNewBill("image1", "image2");
                exportDb(getApplicationContext());
                DataSource dataSource = new DataSource(getApplicationContext());
                try {
                    dataSource.open();
                    Cursor elementCursor = dataSource.getDueBills();
                   if(elementCursor.moveToFirst())
                    Log.i("sagarwagar"," row count = "+elementCursor.getCount());

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    dataSource.close();
                }*/
                Intent fullDetainIntent = new Intent(MainActivity.this, AddBillActivity.class);
                startActivity(fullDetainIntent);
            }
        });
        Button showBill = (Button)findViewById(R.id.btnWarranty);
        showBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllBills();
            }
        });
        Button btnShowAllBill = (Button)findViewById(R.id.btnuser);
        btnShowAllBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent allBillIntent = new Intent(MainActivity.this,ListAllBills.class);
                startActivity(allBillIntent);
            }
        });
        Button btnWarranty = (Button)findViewById(R.id.btnWarranty);
        Button btnUtil = (Button)findViewById(R.id.btnUtility);
        btnWarranty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent fullDetainIntent = new Intent(MainActivity.this,WarrantyActivity.class);
                startActivity(fullDetainIntent);
            }
        });
        btnUtil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent fullDetainIntent = new Intent(MainActivity.this,WarrantyActivity.class);
                startActivity(fullDetainIntent);
            }
        });
    }

    private void showAllBills() {

        try {
            dataSource = new DataSource(getApplicationContext());
            dataSource.open();
            Cursor itemCursor = dataSource.getAllItems();
            for(itemCursor.moveToFirst();!itemCursor.isAfterLast();itemCursor.moveToNext())
            {
                int itemId = itemCursor.getInt(0);
                Cursor itemDataCursor = dataSource.getAllItemsData(itemId);
                for(itemDataCursor.moveToFirst();!itemDataCursor.isAfterLast();itemDataCursor.moveToNext())
                {
                    Log.i("itemData","key = "+itemDataCursor.getString(2)+" value = "+itemDataCursor.getString(3));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void exportDb(Context context)
    {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                //"//data//com.pulp.framework//databases//contents.db";
                String currentDBPath = "//data//"+context.getPackageName()+"//databases//contents.db";
                String backupDBPath = "contents.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        exportDb(getApplicationContext());
    }
    private void createNewBill(String uri_bill_image,String uri_item_image)
    {
        long rowId = -1;
        try {
            dataSource = new DataSource(getApplicationContext());
            dataSource.open();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MySQLiteHelper.COLUMN_PARENT_CATEGORY_ID,0);
            contentValues.put(MySQLiteHelper.COLUMN_CATEGORY_TYPE,Category_Type.BILL_ONE_TIME.ordinal());
            rowId = dataSource.addData(MySQLiteHelper.TABLE_MASTER,contentValues);
            if(rowId != -1)
            {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(MySQLiteHelper.COLUMN_PARENT_CATEGORY_ID, rowId);
                contentValues1.put(MySQLiteHelper.COLUMN_CATEGORY_TYPE,Category_Type.ITEM.ordinal());
                long rowId2 = dataSource.addData(MySQLiteHelper.TABLE_MASTER,contentValues1);
                if(rowId2 != -1)
                {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
                    contentValues2.put(MySQLiteHelper.COLUMN_KEY, billName);
                    contentValues2.put(MySQLiteHelper.COLUMN_VALUE, "bill " + rowId2);
                    contentValues2.put(MySQLiteHelper.COLUMN_TYPE, Element_Type.TEXT.ordinal());
                    dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues2);

                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
                    contentValues3.put(MySQLiteHelper.COLUMN_KEY, bill_image);
                    contentValues3.put(MySQLiteHelper.COLUMN_VALUE, uri_bill_image);
                    contentValues3.put(MySQLiteHelper.COLUMN_TYPE, Element_Type.IMAGE.ordinal());
                    dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues3);

                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
                    contentValues4.put(MySQLiteHelper.COLUMN_KEY, "due_date");
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String dateString = fmt.format(date);
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(fmt.parse(dateString));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DAY_OF_MONTH, 6);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    String output = fmt.format(c.getTime());
                    contentValues4.put(MySQLiteHelper.COLUMN_VALUE, output);
                    contentValues4.put(MySQLiteHelper.COLUMN_TYPE,Element_Type.BILL_DUE_DATE.ordinal());
                    dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL,contentValues4);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SmsService.class);
        startService(intent);
    }
}
