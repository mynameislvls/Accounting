package ls.accounting.barcode;

import ls.accounting.R;
import ls.accounting.barcode.NewGoods.barcode;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class Start_Main extends TabActivity {
	private TabHost mTabHost;
	private ListView list;
	private SQLiteDatabase db = null;
private TextView nameTextView;
String barcode;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addTab();
		nameTextView=(TextView)findViewById(R.id.name);
		DBHelper dbHelper = new DBHelper(Start_Main.this);
		db = dbHelper.getReadableDatabase();
		Cursor cursor2 = db.query("newtable", new String[] { "name"}, "barcode=?",
				new String[] { barcode }, null, null, null);
		if (cursor2.moveToLast()) {
			String nameString=cursor2.getString(cursor2.getColumnIndexOrThrow("name"));
			nameTextView.setText(nameString);
			
		}
	}
private void addTab(){
	Intent getIntent=getIntent();
	barcode=getIntent.getExtras().getString("barcode");
	
	TabHost tabHost =getTabHost();
	TabHost.TabSpec removetabspec=tabHost.newTabSpec("list");
	Intent intent =new Intent();
	intent.setClass(Start_Main.this, ListView_tab.class);
	intent.putExtra("barcode", barcode);
	removetabspec.setContent(intent);
	Resources resources=getResources();
	removetabspec.setIndicator("库存信息");
	tabHost.addTab(removetabspec);
	
	TabHost.TabSpec tabSpec=tabHost.newTabSpec("information");
	Intent intent1=new Intent();
	intent1.putExtra("barcode", barcode);
	intent1.setClass(Start_Main.this, Main_information.class);
	tabSpec.setContent(intent1);
	tabSpec.setIndicator("商品信息");
	tabHost.addTab(tabSpec);
	 
	TabWidget tabWidget = tabHost.getTabWidget();
	 for (int i =0; i < tabWidget.getChildCount(); i++) {  
    tabWidget.getChildAt(i).getLayoutParams().height = 65;  
    tabWidget.getChildAt(i).getLayoutParams().width = 65;
    
   
   
}
}
}
