package com.sd.billsmanager;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;

import db.DataSource;
import model.BillDetailPair;

/**
 * Created by maheshsagar on 10/05/15.
 */
public class FilterFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.filter_fragment,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        ArrayList<BillDetailPair> mFilterList = new ArrayList();

        DataSource dataSource = new DataSource(getActivity());
        try {
            dataSource.open();
            Cursor filterCursor = dataSource.getFiltersKeys();
            int p = filterCursor.getColumnCount();
           for(filterCursor.moveToFirst();!filterCursor.isAfterLast();filterCursor.moveToNext())
            {
                Cursor myFilterCursor = dataSource.getFilters(filterCursor.getInt(0));
                if(myFilterCursor.moveToFirst())
                {
                    BillDetailPair bdp = new BillDetailPair();
                    bdp.setKey(myFilterCursor.getString(2));
                    bdp.setValue(myFilterCursor.getString(3));
                    bdp.setType(myFilterCursor.getInt(5));
                    mFilterList.add(bdp);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataSource.close();
        }
        FIlterAdapter listAdapter = new FIlterAdapter(mFilterList,getActivity());
        listView.setAdapter(listAdapter);
        return rootView;
    }
}
