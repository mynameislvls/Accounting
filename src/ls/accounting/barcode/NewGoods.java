package ls.accounting.barcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLData;

import jxl.biff.ByteArray;
import ls.accounting.R;
import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes.Margins;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewGoods extends Activity {
	private ImageView drawImageView;
	private Button getbarcodeButton;
	private Button newGood;
	private EditText barcodeEditText;
	private EditText nameEditText;
	private EditText lowpricdeEditText;
	private EditText highpriceEditText;

	String barcode;
	String nameString;
	byte[] content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgoods);
		find();
		setpicture();
		getbarcodeButton.setOnClickListener(new barcode());
		newGood.setOnClickListener(new Sure());

	}

	private void setpicture() {
		try {
			Intent intent = getIntent();
			barcode = intent.getExtras().getString("barcode");
			content = intent.getExtras().getByteArray("picture");
			nameString = intent.getExtras().getString("name");
			Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0,
					content.length);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
			Drawable drawable = bitmapDrawable;
			System.out.println("barcode" + barcode);
			drawImageView.setImageDrawable(drawable);
			nameEditText.setText(nameString);
			barcodeEditText.setText(barcode);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class Sure implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nameString = nameEditText.getText().toString();
			if (nameString != null && barcode != null && content != null
					&& lowpricdeEditText.getText().toString() != null
					&& highpriceEditText.getText().toString()!=null) {
				SQLiteDatabase db = null;
				DBHelper dbHelper = new DBHelper(NewGoods.this);
				db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("barcode", barcode);
				values.put("picture", content);
				values.put("name", nameString);
				values.put("price_low", lowpricdeEditText.getText().toString());
				values.put("price_high", highpriceEditText.getText().toString());
				db.insert("newtable", null, values);
				db.close();
				Intent intent = new Intent();
				intent.setClass(NewGoods.this, Start.class);
				startActivity(intent);
				finish();

			}
			else {
				Toast.makeText(getApplicationContext(), "请将所需的内容填写完整！",
					     Toast.LENGTH_SHORT).show();		
			}
			

		}

	}

	class barcode implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			nameString = nameEditText.getText().toString();
			Intent intent = new Intent();
			intent.putExtra("content", content);
			intent.putExtra("name", nameString);
			intent.putExtra("any", "new");
			intent.setClass(NewGoods.this, CaptureActivity.class);
			startActivity(intent);
			finish();

		}

	}

	private void find() {
		drawImageView = (ImageView) findViewById(R.id.draw);
		lowpricdeEditText = (EditText) findViewById(R.id.low_price);
		highpriceEditText = (EditText) findViewById(R.id.high_price);
		getbarcodeButton = (Button) findViewById(R.id.get_barcode);
		newGood = (Button) findViewById(R.id.newGood);
		barcodeEditText = (EditText) findViewById(R.id.barcode);
		nameEditText = (EditText) findViewById(R.id.name);

	}

}
