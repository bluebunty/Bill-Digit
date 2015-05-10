package com.sd.billsmanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import db.DataSource;
import model.ItemBill;
import util.BillConstants;
import util.RecyclerAdapterBill;


public class ListAllBills extends FragmentActivity implements BillConstants {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    ArrayList<ItemBill> myBillList;
    RecyclerAdapterBill myRecyclerAdapter;
    DataSource dataSource;
    Button filterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_bills);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_rootview);
        mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        createBillsList();
        myRecyclerAdapter = new RecyclerAdapterBill(getApplicationContext(),myBillList);
        mRecyclerView.setAdapter(myRecyclerAdapter);
        filterButton = (Button)findViewById(R.id.btnFilter);
        filterAction();
    }
    private void filterAction()
    {
        int q = 0;
        dataSource = new DataSource(getApplicationContext());
        try {
            dataSource.open();
            q = dataSource.getFiltersKeys().getCount();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
        if(q == 0)
        {
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"No filter can be Applied",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ListAllBills.this.getSupportFragmentManager();
                    FilterFragment filterFragment = new FilterFragment();
                    filterFragment.show(fm, "filter");
                }
            });
        }
    }
    private void createBillsList()
    {
        myBillList = new ArrayList<>();
        try {
            dataSource = new DataSource(getApplicationContext());
            dataSource.open();
            Cursor itemCursor = dataSource.getAllItems();
            for(itemCursor.moveToFirst();!itemCursor.isAfterLast();itemCursor.moveToNext())
            {
                int itemId = itemCursor.getInt(0);
                Cursor itemDataCursor = dataSource.getAllItemsData(itemId);
                ItemBill itemBill = new ItemBill();
                for(itemDataCursor.moveToFirst();!itemDataCursor.isAfterLast();itemDataCursor.moveToNext())
                {
                    int type = itemDataCursor.getInt(5);
                    String key = itemDataCursor.getString(2);
                    int valueInt = 3;
                    if(type ==  Element_Type.IMAGE.ordinal() && key.equals("item_image"))
                    {
                        itemBill.imagePath = itemDataCursor.getString(valueInt);
                    }
                    else if(type ==  Element_Type.HASH_TAG.ordinal())
                    {
                        itemBill.hashTag = itemDataCursor.getString(valueInt);
                    }
                    Log.i("itemData", "key = " + itemDataCursor.getString(2) + " value = " + itemDataCursor.getString(3));
                }
                myBillList.add(itemBill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_all_bills, menu);
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
}
