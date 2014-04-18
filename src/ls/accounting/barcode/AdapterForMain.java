package ls.accounting.barcode;

import ls.accounting.R;
import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterForMain extends BaseAdapter {
	private static final String TAG = "CursorAdapter";
	private Context context;
	private Cursor cursor;
	private LayoutInflater inflater;
	private RelativeLayout pageLayout;
	private SQLiteDatabase db;

	public AdapterForMain(Context context, Cursor cursor) {
		super();
		this.context = context;
		this.cursor = cursor;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		Log.d(TAG, "Count :" + cursor.getCount());
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "Item :" + position);
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d(TAG, "ItemId :" + position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		pageLayout = (RelativeLayout) inflater.inflate(R.layout.listview_item,
				null);
		TextView textAddr = (TextView) pageLayout.findViewById(R.id.name);
		TextView price_lowTextView = (TextView) pageLayout
				.findViewById(R.id.price);

		TextView numberTextView = (TextView) pageLayout
				.findViewById(R.id.number);
		System.out.println("cursor");
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("newtable", new String[] { "picture", "name",
				"price_low", "price_high", "time", "barcode" }, null, null,
				null, null, null);
		cursor.moveToPosition(position);
		String barcodeString = cursor.getString(cursor
				.getColumnIndexOrThrow("barcode"));
		String name = cursor.getString(cursor.getColumnIndex("name"));
		textAddr.setText(name);
		int price_low = cursor
				.getInt(cursor.getColumnIndexOrThrow("price_low"));
		int price_high = cursor.getInt(cursor
				.getColumnIndexOrThrow("price_high"));
		String timeString = cursor.getString(cursor
				.getColumnIndexOrThrow("time"));
		price_lowTextView.setText(price_low + "Ôª" + "/" + price_high + "Ôª");

		ImageView image = (ImageView) pageLayout.findViewById(R.id.draw);

		byte[] b = cursor.getBlob(cursor.getColumnIndex("picture"));

		System.out.println(b + "@@@pictureijijoiji@@@");
		if (b != null) {

			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			image.setImageBitmap(bitmap);
		}

		cursor.close();
		Cursor cursor2 = db.query("out_intable", new String[] { "number",
				"time","something"}, "barcode=?",
				new String[] { barcodeString }, null, null, null);

		int position1 = 0;
		int number = 0;
try {
	while (position1 <= cursor2.getCount()) {
		if (cursor2.moveToPosition(position1)) {
			String any ;
			any= cursor2.getString(cursor2
					.getColumnIndexOrThrow("something"));

			int n = cursor2.getInt(cursor2.getColumnIndexOrThrow("number"));

			if (any.equals("in")) {
				number += n;

			} else if (any.equals("out")) {
				number -= n;
			}

		}
		position1 += 1;
		

	}
} catch (Exception e) {
	// TODO: handle exception
}
		

		numberTextView.setText("¿â´æ" + number);
		cursor2.close();
		db.close();
		return pageLayout;
	}
}