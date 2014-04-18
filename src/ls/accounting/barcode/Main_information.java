package ls.accounting.barcode;

import java.text.BreakIterator;

import ls.accounting.R;
import ls.accounting.barcode.NewGoods.barcode;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Main_information extends Activity {
	private ImageView pictureImageView;
	private TextView nameTextView;
	private TextView inventoryTextView;
	private TextView lowpriceTextView;
	private TextView highpriceTextView;
	private TextView barcodeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_information);
		find();
		Intent intent = getIntent();
		String barcodeString = intent.getExtras().getString("barcode");
		DBHelper dbHelper = new DBHelper(Main_information.this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("newtable", new String[] { "name",
				"price_low", "price_high",

				"picture" }, "barcode=?", new String[] { barcodeString }, null,
				null, null);
		cursor.moveToLast();
		nameTextView.setText(cursor.getString(cursor
				.getColumnIndexOrThrow("name")));
		lowpriceTextView.setText("进价:"
				+ cursor.getInt(cursor.getColumnIndexOrThrow("price_low"))
				+ "元");
		highpriceTextView.setText("售价"
				+ cursor.getInt(cursor.getColumnIndexOrThrow("price_high"))
				+ "元");
		byte[] content = cursor
				.getBlob(cursor.getColumnIndexOrThrow("picture"));
		Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0,
				content.length);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		Drawable drawable = bitmapDrawable;

		pictureImageView.setImageDrawable(drawable);
		cursor.close();
		Cursor cursor2 = db.query("out_intable", new String[] { "number",
				"something" }, "barcode=?", new String[] { barcodeString },
				null, null, null);

		int position = 0;
		int number = 0;
		
			while (position <= cursor2.getCount()) {
				if (cursor2.moveToPosition(position)) {
					String any; 
					any= cursor2.getString(cursor2
							.getColumnIndexOrThrow("something"));
					System.out.println("any" + any);
					int n = cursor2.getInt(cursor2
							.getColumnIndexOrThrow("number"));

					if (any.equals("in")) {
						number += n;

					} else if (any.equals("out")) {
						number -= n;
					}

				}
				position += 1;

			}
		
		cursor2.close();
		db.close();
		inventoryTextView.setText("当前库存:" + number);
		barcodeTextView.setText("条形码:" + barcodeString);
	}

	private void find() {
		pictureImageView = (ImageView) findViewById(R.id.picture);
		nameTextView = (TextView) findViewById(R.id.name);
		inventoryTextView = (TextView) findViewById(R.id.inventory);
		lowpriceTextView = (TextView) findViewById(R.id.low_price);
		highpriceTextView = (TextView) findViewById(R.id.high_price);
		barcodeTextView = (TextView) findViewById(R.id.barcode);
	}
}
