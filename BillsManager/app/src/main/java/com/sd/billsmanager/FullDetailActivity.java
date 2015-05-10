package com.sd.billsmanager;


import java.util.ArrayList;
import java.util.Calendar;






import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import model.BillDetailPair;
import util.BillConstants;
import util.MyDatePickerDialog;
import util.MySpinner;

public class FullDetailActivity extends Activity implements OnItemSelectedListener,BillConstants{

	
	private LinearLayout attachmentLayout;
	float deviceDensityFactor = 0;
	ScrollView scrollView;
	BillDetailPair mBillDetailPair = new BillDetailPair();
	ArrayList<BillDetailPair> listDetail = new ArrayList<BillDetailPair>();
	int EVENT_START_DATE_DIALOG_ID = 1;
	int EVENT_END_DATE_DIALOG_ID = 2;
	boolean isFirstTime = false;
	String warrantyDateString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_detail);
		
		Intent intent = getIntent();
		listDetail = (ArrayList<BillDetailPair>) intent.getExtras().getSerializable("list");
		
		
		
		attachmentLayout=(LinearLayout)findViewById(R.id.attachmentLayout);
		scrollView = (ScrollView)findViewById(R.id.scrollView);
		MySpinner spinner = (MySpinner)findViewById(R.id.spinner);
		
		Button btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent listBackIntent = new Intent();
				listBackIntent.putExtra("listDetail", listDetail);
				setResult(Activity.RESULT_OK, listBackIntent);
				finish();
			}
		});
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(-1);
		spinner.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		System.out.println("diwanshu bool"+isFirstTime);
		System.out.println("diwanshu pos"+pos);
		if(pos == 0)
		{
			System.out.println("diwanshu boo"+isFirstTime);
			if(isFirstTime)
			{
				showAlertDialog("Item Name");
			}
			isFirstTime = true;
		}else if(pos == 1)
		{
			showAlertDialog("Item Location");
		}else if(pos == 2)
		{
			showDialog(1);
			
		}else if (pos == 3)
		{
			showDialog(3);
;		}else if (pos == 4)
		{
			showDialog(2);
		}
		
       
    }
	
	@Override
	public Dialog onCreateDialog(int id) {

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String monthString = month < 10  ? "0"+ String.valueOf(month) : String.valueOf(month);
		String dayString   = day < 10  ?  "0"+ String.valueOf(day) : String.valueOf(day);


		switch (id) {
		case 1:
			return new MyDatePickerDialog(this, new DatePickerLister(1),year, Integer.parseInt(monthString), Integer.parseInt(dayString));
		case 2:
			return new MyDatePickerDialog(this, new DatePickerLister(2),year, Integer.parseInt(monthString), Integer.parseInt(dayString));
		case 3:
			return new MyDatePickerDialog(this, new DatePickerLister(3),year, Integer.parseInt(monthString), Integer.parseInt(dayString));
		}
		return null;
	}
	
	class DatePickerLister implements MyDatePickerDialog.OnDateSetListener {	
		private int id;

		public DatePickerLister(int id) {
			this.id = id;
			//setTitle("soniaaa");
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			monthOfYear++;



			String monthString = monthOfYear < 10  ? "0"+ String.valueOf(monthOfYear) : String.valueOf(monthOfYear);
			String dayString   = dayOfMonth < 10  ?  "0"+ String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
			switch (id) {
			case 1:

				warrantyDateString = year+"-"+monthString+"-"+dayString;
				attachmentLayout.addView(getAttachFileLayout(2,"Warranty date",warrantyDateString));
				attachmentLayout.addView(getSeperatorView(), getSeperatorParams());
				scrollView.post(new Runnable() { 
					public void run() {    
						scrollView.fullScroll(View.FOCUS_DOWN);   
					}
				});

				break;	
			case 2:
				String billCycle = year+"-"+monthString+"-"+dayString;
				attachmentLayout.addView(getAttachFileLayout(4,"Warranty date",billCycle));
				attachmentLayout.addView(getSeperatorView(), getSeperatorParams());
				scrollView.post(new Runnable() { 
					public void run() {    
						scrollView.fullScroll(View.FOCUS_DOWN);   
					}
				});
				
			case 3:
				String serviceDateString = year+"-"+monthString+"-"+dayString;
				attachmentLayout.addView(getAttachFileLayout(3,"Service date",serviceDateString));
				attachmentLayout.addView(getSeperatorView(), getSeperatorParams());
				scrollView.post(new Runnable() { 
					public void run() {    
						scrollView.fullScroll(View.FOCUS_DOWN);   
					}
				});

				break;

			
			}
		}
	}
	
	private View getSeperatorView() {
		View seperatorView =new View(this);
		seperatorView.setBackgroundColor(Color.TRANSPARENT);

		return seperatorView;
		//viewParams.setMarginEnd(margin10);relativeLayout.addView(seperatorView, viewParams);
	}

	private LinearLayout.LayoutParams getSeperatorParams() {
		LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(-1, convertDpToPixel(5));
		viewParams.topMargin = 5;
		return viewParams;
	}

    public void onNothingSelected(AdapterView<?> parent) {
    }
    
    private void setList(final int pos,String key,String value)
    {
		mBillDetailPair = new BillDetailPair();
    	mBillDetailPair.setKey(key);
    	mBillDetailPair.setValue(value);
    	if(pos == 0)
    	{
    		mBillDetailPair.setType(BillConstants.Element_Type.ITEM.ordinal());
    	}else if (pos == 1)
    	{
    		mBillDetailPair.setType(BillConstants.Element_Type.LOCATION.ordinal());
    	}else if(pos == 2)
    	{
    		mBillDetailPair.setType(BillConstants.Element_Type.WARRANTY.ordinal());
    	}else if(pos == 3)
    	{
    		//Bill for service
    		//mBillDetailPair.setType(BillConstants.Element_Type..ordinal());
    	}else if(pos == 4)
    	{
    		//Bill for BillCycle
    		//mBillDetailPair.setType(BillConstants.Element_Type.BILL_DUE_DATE.ordinal());
    	}
    	listDetail.add(mBillDetailPair);
    }
    
    private RelativeLayout getAttachFileLayout(final int pos,String key,String value) {


    	setList(pos, key, value);
		int margin5 = convertDpToPixel(5);
		int margin10 = convertDpToPixel(10);
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setTag("string"+pos);
		relativeLayout.setBackgroundColor(Color.parseColor("#13172b"));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
		//params.topMargin = 20;
		//params.bottomMargin = 20;
		params.leftMargin = margin10;
		params.rightMargin = margin10;
		relativeLayout.setLayoutParams(params);
		relativeLayout.setPadding(0, margin10, 0, margin10);


		/*ImageView mThumbView = new ImageView(this);
		
			//mThumbView.setImageBitmap(BitmapFactory.decodeFile(eventAttach.getThumbnailPath()));
		mThumbView.setId(123);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(-2, -2);
		imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		imageParams.leftMargin = margin10;
		relativeLayout.addView(mThumbView,imageParams);*/

		TextView headingTV = new TextView(this);
		headingTV.setId(123);
		headingTV.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
		headingTV.setText(key);
		headingTV.setSingleLine(true);
		headingTV.setEllipsize(TextUtils.TruncateAt.END);
		headingTV.setTextColor(Color.WHITE);
		headingTV.setPadding(0, 0, convertDpToPixel(40), 0);
		
		RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(-2, -2);
		txtParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		txtParams.leftMargin = margin10;
		relativeLayout.addView(headingTV,txtParams);
		
		
		TextView imageNameTV = new TextView(this);
		imageNameTV.setId(124);
		imageNameTV.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
		imageNameTV.setText(value);
		imageNameTV.setSingleLine(true);
		imageNameTV.setEllipsize(TextUtils.TruncateAt.END);
		imageNameTV.setTextColor(Color.WHITE);
		imageNameTV.setPadding(0, 0, convertDpToPixel(40), 0);
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(-2, -2);
		imgParams.addRule(RelativeLayout.BELOW, 123);
		imgParams.setMargins(10, 10, 0, 0);
		imgParams.leftMargin = margin10;
		relativeLayout.addView(imageNameTV, imgParams);



		Button delImgBtn = new Button(this);
		delImgBtn.setBackgroundResource(R.drawable.close_icon);
		delImgBtn.setTextColor(Color.WHITE);
		delImgBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(listDetail.contains(mBillDetailPair) ) {
					listDetail.remove(mBillDetailPair);
				}
				attachmentLayout.removeView(scrollView.findViewWithTag("string"+pos));
			}
		});

		RelativeLayout.LayoutParams delBtnParams = new RelativeLayout.LayoutParams(convertDpToPixel(32),convertDpToPixel(32));
		delBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		delBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
		delBtnParams.rightMargin = margin10;
		relativeLayout.addView(delImgBtn, delBtnParams);

		return relativeLayout;
	}
    
    public int convertDpToPixel(float dp){
    	DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
    	deviceDensityFactor = metrics.density;
		if(deviceDensityFactor == 0) {
			DisplayMetrics metrices =  getApplicationContext().getResources().getDisplayMetrics();
			deviceDensityFactor = metrices.density;
		}
		float px = dp * deviceDensityFactor + 0.5f ;
		return (int)px;
	}
    
    private void showAlertDialog(final String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FullDetailActivity.this);
		View view = getLayoutInflater().inflate( R.layout.add_full_detail, null );
		builder.setView(view);
		builder.setCancelable(false);
		final AlertDialog dlg = builder.create();
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView txtView = (TextView)view.findViewById(R.id.txtHeading);
		txtView.setText(text);
		final EditText editName = (EditText)view.findViewById(R.id.editName);
		Button acceptButton = (Button) view.findViewById(R.id.buttonUpdate);
		Button rejectButton = (Button) view.findViewById(R.id.buttonCancel);

		acceptButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if(text.equalsIgnoreCase("Item Name"))
				{
					attachmentLayout.addView(getAttachFileLayout(0,"Item Name",editName.getText().toString()));
				}else if(text.equalsIgnoreCase("Your item service duration")){
					attachmentLayout.addView(getAttachFileLayout(3,"Your item service duration",editName.getText().toString()));
				}else if(text.equalsIgnoreCase("Item Location")){
					attachmentLayout.addView(getAttachFileLayout(1,"Item Location",editName.getText().toString()));
				}
				attachmentLayout.addView(getSeperatorView(), getSeperatorParams());
				scrollView.post(new Runnable() { 
					public void run() {    
						scrollView.fullScroll(View.FOCUS_DOWN);   
					}
				});
				dlg.dismiss();
			}
		});

		rejectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		dlg.show();
	}
    

}


