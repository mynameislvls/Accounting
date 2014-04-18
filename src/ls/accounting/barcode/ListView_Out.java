package ls.accounting.barcode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import ls.accounting.R;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListView_Out extends Activity {
	private SQLiteDatabase db = null;
	private ListView listView;
	private Button out_excelButton;
	private TextView any;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		DBHelper dbHelper = new DBHelper(ListView_Out.this);
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("outtable", new String[] { "barcode", "name",
				"price", "time" }, null, null, null, null, null);

		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new CursorAdapter_out(this, cursor));
		// setListAdapter(mAdapter);
		cursor.close();
		db.close();
		out_excelButton = (Button) findViewById(R.id.out_excel);
		out_excelButton.setOnClickListener(new Out_Excel());
		any=(TextView)findViewById(R.id.any);
		any.setText("出库记录");

	}

	class Out_Excel implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd    hh:mm:ss");
			String date = sDateFormat.format(new java.util.Date());

			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + date + ".xls");

			WritableWorkbook book = null;
			try {
				book = Workbook.createWorkbook(file);
				WritableSheet sheet = book.createSheet("first", 0);
				Label label = new Label(0, 0, "商品名");
				sheet.addCell(label);
				label = new Label(0, 1, "条形码");
				sheet.addCell(label);
				label = new Label(0, 2, "价格");
				sheet.addCell(label);
				int position = 0;
				DBHelper dbHelper = new DBHelper(ListView_Out.this);
				db = dbHelper.getReadableDatabase();
				Cursor cursor = db.query("outtable", new String[] { "barcode", "name",
						"price", "time" }, null, null, null,
						null, null);

				cursor.moveToPosition(position);

				while (position < cursor.getCount()) {
					cursor.moveToPosition(position);
					label = new Label(position + 1, 0, cursor.getString(cursor
							.getColumnIndexOrThrow("name")));
					sheet.addCell(label);
					jxl.write.Number number = new jxl.write.Number(
							position + 1, 1, cursor.getInt(cursor
									.getColumnIndexOrThrow("barcode")));
					sheet.addCell(number);
					jxl.write.Number number1 = new jxl.write.Number(
							position + 1, 2, cursor.getInt(cursor
									.getColumnIndexOrThrow("price")));
					sheet.addCell(number1);
					position = position + 1;

					System.out.println("while" + label);

				}
				book.write();
				book.close();

				cursor.close();
				db.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), "亲。Excel表到货了",
				     Toast.LENGTH_SHORT).show();
		}
	}

}
