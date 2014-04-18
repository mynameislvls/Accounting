package ls.accounting.barcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.baidu.frontia.FrontiaApplication;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import ls.accounting.R;
import ls.accounting.R.drawable;
import ls.accounting.R.id;
import ls.accounting.R.layout;
import ls.accounting.R.string;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Start extends Activity {
	private Button inButton;
	ListView listview;
	ListView alllist;
	private DrawerLayout drawerLayout;
	private Button inRecordButton;

	CharSequence title;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		find();

		// inButton.setOnClickListener(new In());
		// inRecordButton.setOnClickListener(new InRecord());
		setList();
		drawer();
		drawer_listOnClick();
		FrontiaApplication.initFrontiaApplication(getApplicationContext());

	}

	private void setList() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1);
		adapter.add("入库");
		adapter.add("出库");
		adapter.add("入库记录");
		adapter.add("出库记录");
		adapter.add("添加新品");
		listview.setAdapter(adapter);
		listview.setDividerHeight(0);
		DBHelper dbHelper = new DBHelper(Start.this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("newtable", new String[] { "picture", "name",
				"price_low", "price_high", "time", "barcode" }, null, null,
				null, null, null);
		try {
			alllist.setAdapter(new AdapterForMain(this, cursor));
			alllist.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					DBHelper dbHelper = new DBHelper(Start.this);
					SQLiteDatabase db = dbHelper.getReadableDatabase();
					Cursor cursor = db.query("newtable", new String[] {
							"picture", "name", "barcode" }, null, null, null,
							null, null);
					cursor.moveToPosition(arg2);
					String barcodeString = cursor.getString(cursor
							.getColumnIndexOrThrow("barcode"));
					Intent intent = new Intent();
					intent.putExtra("barcode", barcodeString);
					intent.setClass(Start.this, Start_Main.class);
					startActivity(intent);

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

		// setListAdapter(mAdapter);
		cursor.close();
		db.close();
	}

	private void drawer() {
		drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this,
				drawerLayout, R.drawable.launcher_icon, R.string.app_name,
				R.string.app_picker_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
		});
	}

	private void drawer_listOnClick() {
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Intent intent = new Intent();
					intent.putExtra("any", "in");
					intent.setClass(Start.this, CaptureActivity.class);
					startActivity(intent);

				}
				if (position == 1) {
					Intent intent = new Intent();
					intent.putExtra("any", "out");
					intent.setClass(Start.this, CaptureActivity.class);
					startActivity(intent);

				}
				if (position == 2) {
					Intent intent = new Intent();
					intent.setClass(Start.this, ListView_In.class);
					startActivity(intent);
				}
				if (position == 3) {
					Intent intent = new Intent();
					intent.setClass(Start.this, ListView_Out.class);
					startActivity(intent);

				}
				if (position == 4) {
					Intent intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.addCategory(intent.CATEGORY_DEFAULT);
					startActivityForResult(intent, 1);

				}

			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			picture(data);

		}

	}

	private void picture(Intent data) {
		try {
			Intent intent = new Intent();
			intent = data;
			Uri uri = intent.getData();
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					getContentResolver(), uri);
			bitmap = zoomBitmap(bitmap, 100, 100);
			byte[] mcontent = Bitmap2Bytes(bitmap);

			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();

			}
			System.gc();
			intent.putExtra("picture", mcontent);
			intent.setClass(Start.this, NewGoods.class);
			startActivity(intent);
			finish();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return bitmap2;

	}

	public static byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

		return baos.toByteArray();

	}

	public static byte[] readStream(InputStream inputStream) throws IOException {

		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		byte[] data = outputStream.toByteArray();
		outputStream.close();
		inputStream.close();
		return data;
	}

	class In implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Start.this, CaptureActivity.class);
			startActivity(intent);

		}

	}

	class InRecord implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(Start.this, ListView_In.class);
			startActivity(intent);

		}

	}

	private void find() {
		// inButton=(Button)findViewById(R.id.in_button);
		// inRecordButton=(Button)findViewById(R.id.in_record_button);
		listview = (ListView) findViewById(R.id.listview);
		alllist = (ListView) findViewById(R.id.all_list);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}
}
