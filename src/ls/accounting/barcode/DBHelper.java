/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ls.accounting.barcode;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;

/**
 * @author Sean Owen
 */
final class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "accounting";

	DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql_in = "CREATE TABLE intable(_id integer primary key autoincrement,barcode integer ,name char[20],price integer,time char[20],number integer)";
		// 输出创建数据库的日志信息

		// execSQL函数用于执行SQL语句
		db.execSQL(sql_in);
		String sql_out = "CREATE TABLE outtable(_id integer primary key autoincrement,barcode integer ,name char[20],price integer,time char[20],number integer)";
		// 输出创建数据库的日志信息

		// execSQL函数用于执行SQL语句
		db.execSQL(sql_out);
		String sql_inventory = "CREATE TABLE inventory(_id integer primary key autoincrement,barcode integer ,name char[20],price integer,time char[20],number integer)";
		// 输出创建数据库的日志信息

		// execSQL函数用于执行SQL语句
		db.execSQL(sql_inventory);
		String sql_out_in = "CREATE TABLE out_intable(_id integer primary key autoincrement," +
				"barcode integer ,name char[20],price integer,time char[20]," +
				"number integer,something char[20])";
		// 输出创建数据库的日志信息

		// execSQL函数用于执行SQL语句
		db.execSQL(sql_out_in);
		String sql_new = "CREATE TABLE newtable(_id integer primary key autoincrement,"
				+ "picture blob,barcode integer ,name char[20],"
				+ "price_high integer,price_low integer,time char[20])";
		// 输出创建数据库的日志信息

		// execSQL函数用于执行SQL语句
		db.execSQL(sql_new);
		System.out.println("创建数据库完成");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

	}

}
