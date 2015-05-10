package com.sd.billsmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import model.BillDetailPair;
import util.BillConstants;

/**
 * Created by maheshsagar on 10/05/15.
 */
public class FIlterAdapter extends BaseAdapter implements BillConstants {

    Context context;
    ArrayList<BillDetailPair> mList;

    public FIlterAdapter(ArrayList<BillDetailPair> _mList,Context _context)
    {
        mList = _mList;
        context = _context;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
        {
            view = inflater.inflate(R.layout.item_filter,null);

        }
        TextView keyText = (TextView)view.findViewById(R.id.textKey);
        EditText valiueText = (EditText)view.findViewById(R.id.textValue);
        BillDetailPair bdp = mList.get(i);
        keyText.setText(elementKey[bdp.getType()]);
        addCallBacksInstructions(valiueText,bdp.getType());
        return view;
    }
    private void addCallBacksInstructions(EditText valueText,int type)
    {
        if(type == Element_Type.BILL_DUE_DATE.ordinal() ||
                type == Element_Type.WARRANTY.ordinal() ||
                type == Element_Type.PURCHASE_DATE.ordinal() ||
                type == Element_Type.BILL_DUE_DATE.ordinal()
                )
        {
            valueText.setHint("choose date");
            valueText.setTag("date");
            valueText.setEnabled(false);
        }
        else {
            valueText.setHint("enter filter value");
            valueText.setTag("string");
        }
    }
}
