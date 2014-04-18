package ls.accounting.barcode;

import java.util.List;

import ls.accounting.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

class cursorAdapter extends BaseAdapter implements View.OnClickListener {
	private static final String TAG = "CursorAdapter";
	private Context context;
	private Cursor cursor;

	private RelativeLayout pageLayout;
	private SQLiteDatabase db;
	private List<Integer> colors;
	private int screenWidth;

	public cursorAdapter(Context context, Cursor cursor, int screenwidth) {
		super();
		this.context = context;
		this.cursor = cursor;
		this.screenWidth = screenwidth;
		// inflater = LayoutInflater.from(context);
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_words, parent, false);
			holder = new ViewHolder();
			holder.hSView = (HorizontalScrollView) convertView
					.findViewById(R.id.hsv);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.numberTextView = (TextView) convertView
					.findViewById(R.id.number);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.time);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.name);
			holder.deleteButton = (Button) convertView
					.findViewById(R.id.delete_button);
			holder.deleteButton.setTag(position);
			holder.content = (RelativeLayout) convertView
					.findViewById(R.id.information_view);
			holder.action = (LinearLayout) convertView
					.findViewById(R.id.delete);
			 LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.content.getLayoutParams();
			lp.width=screenWidth;
			holder.content.setLayoutParams(lp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_UP:
					ViewHolder viewHolder = (ViewHolder) arg0.getTag();
					int scrollX = viewHolder.hSView.getScrollX();
					int actionW = viewHolder.action.getWidth();
					if (scrollX < actionW / 2) {
						viewHolder.hSView.smoothScrollTo(0, 0);
					} else {
						viewHolder.hSView.smoothScrollTo(actionW, 0);
					}

					return true;
				}
				return false;
			}
		});
		if (holder.hSView.getScrollX() != 0) {
			holder.hSView.scrollTo(0, 0);

		}
		// pageLayout = (RelativeLayout) inflater.inflate(R.layout.item_words,
		// null);
		//
		//
		//
		// System.out.println("cursor");
		DBHelper dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("intable", new String[] { "barcode", "name",
				"time", "price", "number" }, null, null, null, null, null);
		cursor.moveToPosition(position);
		System.out.println("position" + position);
		// String price = cursor.getString(cursor.getColumnIndex("price"));
		String barcode1 = cursor.getString(cursor.getColumnIndex("barcode"));
		String name1 = cursor.getString(cursor.getColumnIndex("name"));
		String time1 = cursor.getString(cursor.getColumnIndex("time"));
		int number = cursor.getInt(cursor.getColumnIndexOrThrow("number"));
		int price1 = cursor.getInt(cursor.getColumnIndexOrThrow("price"));

		holder.nameTextView.setText(name1);
		holder.timeTextView.setText(time1);
		holder.numberTextView.setText("Èë¿â" + number);
		holder.price.setText(price1 + "Ôª");
		holder.deleteButton.setOnClickListener(new Delete());

		// textAddr.setText(price);

		cursor.close();
		db.close();

		return convertView;
	}

	class Delete implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}

	class ViewHolder {
		public HorizontalScrollView hSView;
		public RelativeLayout content;
		public LinearLayout action;
		public Button deleteButton;
		public TextView nameTextView;

		public TextView timeTextView;
		public TextView numberTextView;
		public TextView price;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
