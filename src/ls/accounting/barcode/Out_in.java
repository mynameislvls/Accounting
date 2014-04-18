package ls.accounting.barcode;

import java.text.SimpleDateFormat;

import ls.accounting.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Out_in extends Activity {
	private TextView nameTextView;
	private TextView out_inTextView;
	private ImageView drawImageView;
	private EditText price;
	private EditText number;
	private Button okButton;
	byte[] content;
	int priceint;
	String nameString;
	String barcode;
	String any;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.out_in);
		find();
		Intent intent = getIntent();
		any = intent.getExtras().getString("any");
		barcode = intent.getExtras().getString("barcode");
		if (any.equals("in")) {
			out_inTextView.setText("入库");
		}else if(any.equals("out")) {
			out_inTextView.setText("出库");
		}
		
		System.out.println("barcode" + barcode);
		DBHelper dbHelper = new DBHelper(Out_in.this);
		SQLiteDatabase db = null;
		db = dbHelper.getReadableDatabase();
		if (any.equals("in")) {
			Cursor cursor = db.query("newtable", new String[] { "name",
					"price_low", "picture" }, "barcode=?",
					new String[] { barcode }, null, null, null);

			
			cursor.moveToLast();
			content = cursor.getBlob(cursor.getColumnIndexOrThrow("picture"));
			priceint = cursor.getInt(cursor.getColumnIndexOrThrow("price_low"));
			nameString = cursor.getString(cursor.getColumnIndexOrThrow("name"));
			cursor.close();
			db.close();
			try {
				price.setText(priceint + "");
				nameTextView.setText(nameString);
				Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0,
						content.length);
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				Drawable drawable = bitmapDrawable;
				System.out.println("barcode" + barcode);
				drawImageView.setImageDrawable(drawable);

			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (any.equals("out")) {
			Cursor cursor = db.query("newtable", new String[] { "name",
					"price_high", "picture" }, "barcode=?",
					new String[] { barcode }, null, null, null);

			cursor.moveToLast();
			content = cursor.getBlob(cursor.getColumnIndexOrThrow("picture"));
			priceint = cursor
					.getInt(cursor.getColumnIndexOrThrow("price_high"));
			nameString = cursor.getString(cursor.getColumnIndexOrThrow("name"));
			cursor.close();
			db.close();
			try {
				price.setText(priceint + "");
				nameTextView.setText(nameString);
				Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0,
						content.length);
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				Drawable drawable = bitmapDrawable;
				System.out.println("barcode" + barcode);
				drawImageView.setImageDrawable(drawable);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		okButton.setOnClickListener(new OK());

	}

	class OK implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd    hh:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			SQLiteDatabase db = null;
			DBHelper dbHelper = null;
			dbHelper = new DBHelper(Out_in.this);
			db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("price", priceint);
			values.put("time", date);
			values.put("BarCode", barcode);
			values.put("name", nameString);
			values.put("number", number.getText().toString());
			System.out.println("数据已经存入"+any);
			
			if (any.equals("in")) {
				db.insert("intable", null, values);
				values.put("something", any);
				db.insert("out_intable", null, values);
			} else if (any.equals("out")) {
				db.insert("outtable", null, values);
				values.put("something", any);
				db.insert("out_intable", null, values);
			}
			db.close();

			Intent intent = new Intent();
			intent.putExtra("any", "in");
			intent.setClass(Out_in.this, CaptureActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void find() {
		nameTextView = (TextView) findViewById(R.id.name);
		out_inTextView=(TextView)findViewById(R.id.out_in);
		drawImageView = (ImageView) findViewById(R.id.draw);
		price = (EditText) findViewById(R.id.price);
		number = (EditText) findViewById(R.id.number);
		okButton = (Button) findViewById(R.id.ok);

	}
}
