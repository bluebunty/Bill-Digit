package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class MyDatePickerDialog extends DatePickerDialog implements OnDateChangedListener {

private DatePickerDialog mDatePicker;
private Context mContext;

@SuppressLint("NewApi")
public MyDatePickerDialog(Context context, OnDateSetListener callBack,
        int year, int monthOfYear, int dayOfMonth) {
    super(context,callBack, year, monthOfYear, dayOfMonth);
    mContext = context;
    this.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    //this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; 
    updateTitle(year, monthOfYear,dayOfMonth);

}
public void onDateChanged(DatePicker view, int year,
        int month, int day) {
    updateTitle(year, month,day);
}	
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public void updateTitle(int year, int month,int day) {
   Calendar mCalendar = Calendar.getInstance();
    mCalendar.set(Calendar.YEAR, year);
    mCalendar.set(Calendar.MONTH, month);
    mCalendar.set(Calendar.DAY_OF_MONTH, day);
	month++;
	String monthString = month < 10  ? "0"+ String.valueOf(month) : String.valueOf(month);
	String dayString   = day < 10  ?  "0"+ String.valueOf(day) : String.valueOf(day);
    SimpleDateFormat fromDateFormat1=new SimpleDateFormat("dd/MM/yyyy");
	  try {
		  Locale myLocale = mContext.getResources().getConfiguration().locale;
		  Date dt1=fromDateFormat1.parse(dayString+"/"+monthString+"/"+year);
		  SimpleDateFormat format2=new SimpleDateFormat("EEEE"); 
		  String finalDay=format2.format(dt1);
		  String dateSubString = finalDay.substring(0, 3);
		 // this.setTitle(dateSubString+" "+dayString+"-"+mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, myLocale)+"-"+year);
	} catch (Exception e) {
		e.printStackTrace();
	}

}   

public DatePickerDialog getPicker(){

    return this.mDatePicker;
}
    /*
     * the format for dialog tile,and you can override this method
     */
public SimpleDateFormat getFormat(){
    return new SimpleDateFormat("MMM, yyyy");
};   
}