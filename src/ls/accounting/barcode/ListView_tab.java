package ls.accounting.barcode;

import javax.security.auth.PrivateCredentialPermission;

import ls.accounting.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class ListView_tab extends Activity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_tab);
		list = (ListView) findViewById(R.id.list_tab);
		Intent intent = getIntent();
		String barcodeString = intent.getExtras().getString("barcode");

		DBHelper dbHelper = new DBHelper(ListView_tab.this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("out_intable", new String[] { "barcode", "name",
				"time", "price", "number","something" }, "barcode=?",
				new String[] { barcodeString }, null, null, null);
		list.setAdapter(new CursorAdapter_record(this, cursor));
		cursor.close();
		db.close();

	}

}
