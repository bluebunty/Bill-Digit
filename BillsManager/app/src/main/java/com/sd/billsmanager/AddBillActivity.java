package com.sd.billsmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.w3c.dom.Text;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import db.DataSource;
import db.MySQLiteHelper;
import model.BillDetailPair;
import util.BillConstants;

public class AddBillActivity extends Activity implements ImageChooserListener,BillConstants {

	private ImageChooserManager imageChooserManager;
	private ImageView imageViewBill;
	private ImageView imageViewItem;
	private int chooserType;
	private String chooserSource;
	int addMoreFlag = 1000;
	private String filePath;
	private String originalFilePath;
	private String pathOfBillImage;
	private String pathOfItemImage;
	private Bundle bundle;
	private DataSource dataSource;
	private EditText editTextHash;
	ArrayList<BillDetailPair> listDetail = new ArrayList<>();
	private LinearLayout itemAddedLinear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bill);
		itemAddedLinear = (LinearLayout)findViewById(R.id.itemLinearView);
		TextView addMoreDetail = (TextView)findViewById(R.id.addPair);
		imageViewBill = (ImageView)findViewById(R.id.image_bill);
		imageViewItem = (ImageView)findViewById(R.id.image_item);
		editTextHash = (EditText)findViewById(R.id.hashTag);
		addMoreDetail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent fullDetainIntent = new Intent(AddBillActivity.this,FullDetailActivity.class);
				fullDetainIntent.putExtra("list", listDetail);
				startActivityForResult(fullDetainIntent, addMoreFlag);
				
			}
		});
		View btnBillPhoto = findViewById(R.id.btnBillPhoto);
		btnBillPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooserSource = "bill";
				showPictureUpdateDialogue();
			}
		});


		View btnItemPhoto = findViewById(R.id.btnItemPhoto);
		btnItemPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				chooserSource = "item";
				showPictureUpdateDialogue();
			}
		});
		Button saveBtn = (Button) findViewById(R.id.btnSave);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveForm();
			}
		});
	}

	private void saveForm()
	{
		long rowId = -1;
		try {
			dataSource = new DataSource(getApplicationContext());
			dataSource.open();
			ContentValues contentValues = new ContentValues();
			contentValues.put(MySQLiteHelper.COLUMN_PARENT_CATEGORY_ID,0);
			contentValues.put(MySQLiteHelper.COLUMN_CATEGORY_TYPE, BillConstants.Category_Type.BILL_ONE_TIME.ordinal());
			rowId = dataSource.addData(MySQLiteHelper.TABLE_MASTER,contentValues);
			if(rowId != -1)
			{
				ContentValues contentValues1 = new ContentValues();
				contentValues1.put(MySQLiteHelper.COLUMN_PARENT_CATEGORY_ID, rowId);
				contentValues1.put(MySQLiteHelper.COLUMN_CATEGORY_TYPE, BillConstants.Element_Type.ITEM.ordinal());
				long rowId2 = dataSource.addData(MySQLiteHelper.TABLE_MASTER,contentValues1);
				if(rowId2 != -1)
				{
					ContentValues contentValues2 = new ContentValues();
					contentValues2.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
					contentValues2.put(MySQLiteHelper.COLUMN_KEY, hash_tag);
					contentValues2.put(MySQLiteHelper.COLUMN_VALUE, editTextHash.getText().toString());
					contentValues2.put(MySQLiteHelper.COLUMN_TYPE, Element_Type.HASH_TAG.ordinal());
					dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues2);

					ContentValues contentValues3 = new ContentValues();
					contentValues3.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
					contentValues3.put(MySQLiteHelper.COLUMN_KEY, bill_image);
					contentValues3.put(MySQLiteHelper.COLUMN_VALUE, pathOfBillImage);
					contentValues3.put(MySQLiteHelper.COLUMN_TYPE, BillConstants.Element_Type.IMAGE.ordinal());
					dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues3);

					ContentValues contentValues5 = new ContentValues();
					contentValues5.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
					contentValues5.put(MySQLiteHelper.COLUMN_KEY, item_image);
					contentValues5.put(MySQLiteHelper.COLUMN_VALUE, pathOfItemImage);
					contentValues5.put(MySQLiteHelper.COLUMN_TYPE, BillConstants.Element_Type.IMAGE.ordinal());
					dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues5);
					for(int i = 0; i<listDetail.size();i++)
					{
						BillDetailPair bdp = listDetail.get(i);
						ContentValues contentValues4 = new ContentValues();
						contentValues4.put(MySQLiteHelper.COLUMN_CATEGORY_ID, rowId2);
						contentValues4.put(MySQLiteHelper.COLUMN_KEY, bdp.getKey());
						contentValues4.put(MySQLiteHelper.COLUMN_VALUE, bdp.getValue());
						contentValues4.put(MySQLiteHelper.COLUMN_TYPE, bdp.getType());
						dataSource.addData(MySQLiteHelper.TABLE_CATEGORY_DETAIL, contentValues4);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataSource.close();
			AddBillActivity.this.finish();
			Toast.makeText(getApplicationContext(),"Bill Added Succesfully",Toast.LENGTH_SHORT).show();
		}
	}
	public void takePicture() {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this,ChooserType.REQUEST_CAPTURE_PICTURE,"myFolder",true);
		imageChooserManager.setImageChooserListener(this);

		try{
			filePath = imageChooserManager.choose();


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		if (image != null) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(chooserSource.equals("item")) {
						pathOfItemImage = image.getFileThumbnail();
						Log.i("sagarwagar", "choosen image path = " + image.getFilePathOriginal());
						imageViewItem.setImageURI(Uri.parse(new File(image
								.getFileThumbnail()).toString()));
					}
					else {
						pathOfBillImage = image.getFilePathOriginal();
						Log.i("sagarwagar", "choosen image path = " + image.getFilePathOriginal());
						imageViewBill.setImageURI(Uri.parse(new File(image
								.getFileThumbnail()).toString()));
					}

				}
			});



		}
	}

	@Override
	public void onError(String s) {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		outState.putInt("chooser_type", chooserType);
		outState.putString("chooser_source", chooserSource);
		outState.putString("media_path", filePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE) {
				if (imageChooserManager == null) {
					reinitializeImageChooser();
				}

				imageChooserManager.submit(requestCode, data);

				super.onActivityResult(requestCode, resultCode, data);
			}
			else if(requestCode == addMoreFlag)
			{
				if(data!= null)
				{
					listDetail.clear();
					listDetail = (ArrayList<BillDetailPair>) data.getExtras().getSerializable("listDetail");
					LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
					{
						for(int i = 0;i<listDetail.size();i++)
						{
							BillDetailPair bdp = listDetail.get(i);
							View item = inflater.inflate(R.layout.item_field_added,null);
							TextView textKey = (TextView)item.findViewById(R.id.textKey);
							TextView textValue = (TextView)item.findViewById(R.id.textValue);
							textKey.setText(elementKey[bdp.getType()]);
							textValue.setText(bdp.getValue());
							itemAddedLinear.addView(item);
						}

					}

					System.out.println("diwanshu size"+listDetail.size());

				}
			}
		}
	}


	private void reinitializeImageChooser() {
		int chooser = bundle.getInt("chooser_type");
		imageChooserManager = new ImageChooserManager(this, bundle.getInt("chooser_type"),
				"myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(bundle.getString("media_path"));

	}


	public void showPictureUpdateDialogue(){
		AlertDialog dialog;
		PackageManager packageManager = this.getPackageManager();
		final String[] items ;
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			items = new String[]{"Camera", "Gallery", "Cancel"};

		}else{
			items = new String[]{"Gallery", "Cancel"};
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter(adapter,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (items[which].equals("Camera")) {
					takePicture();
				}
				if (items[which].equals("Gallery")) {
					chooseImageFromGallery();
				}
				else if (items[which].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		dialog = builder.create();
		dialog.show();
	}


	public void chooseImageFromGallery(){
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		try {
			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
