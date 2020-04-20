package cn.rainss.smartNote.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteUtils extends SQLiteOpenHelper {

    private static final String sqliteFileName = "rains.db";

    private static final int sqliteVersion = 3;

    public SQLiteUtils(Context context) {
        super(context, sqliteFileName, null, sqliteVersion);
    }

    /**
     * 创建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建分类表
        db.execSQL("CREATE TABLE \"type\" (\n" +
                "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  \"name\" TEXT NOT NULL,\n" +
                "  \"create_time\" timestamp NOT NULL\n" +
                ")");
    }

    /**
     * 数据库版本升级时调用的方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
