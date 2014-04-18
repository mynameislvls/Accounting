package ls.accounting.barcode;

import ls.accounting.R;
import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CursorAdapter_record extends BaseAdapter {
	private static final String TAG = "CursorAdapter";
	private Context context;
	private Cursor cursor;
	private LayoutInflater inflater;
	private RelativeLayout pageLayout;
	private SQLiteDatabase db;

	public CursorAdapter_record(Context context,Cursor cursor) {
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
		pageLayout = (RelativeLayout) inflater.inflate(R.layout.item_words, null);

		TextView price=(TextView)pageLayout.findViewById(R.id.price);
		TextView numberTextView=(TextView)pageLayout.findViewById(R.id.number);
		TextView timeTextView=(TextView)pageLayout.findViewById(R.id.time);
		TextView name=(TextView) pageLayout.findViewById(R.id.name);
	
		System.out.println("cursor");
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("out_intable",
				new String[] { "barcode", "name","time","price","number","something" },null,null, null, null,
				null);
		cursor.moveToPosition(position);
		System.out.println("position" + position);
		//String price = cursor.getString(cursor.getColumnIndex("price"));
		String barcode1 = cursor.getString(cursor.getColumnIndex("barcode"));
		String name1 = cursor.getString(cursor.getColumnIndex("name"));
		String time1 = cursor.getString(cursor.getColumnIndex("time"));
		int number=cursor.getInt(cursor.getColumnIndexOrThrow("number"));
		int price1=cursor.getInt(cursor.getColumnIndexOrThrow("price"));
		String anyString =cursor.getString(cursor.getColumnIndexOrThrow("something"));
		name.setText(name1);
		timeTextView.setText(time1);
		if (anyString.equals("in")) {
			numberTextView.setText("Èë¿â"+number);
		}
		else if (anyString.equals("out")) {
			numberTextView.setText("³ö¿â"+number);
			
		}
		
		price.setText(price1+"Ôª");
		
		

		//textAddr.setText(price);
		name.setText(name1);
	
		

		cursor.close();

		

		return pageLayout;
	}
}