package cn.rainss.smartNote.schedule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cn.rainss.smartNote.schedule.model.Schedule;
import cn.rainss.smartNote.schedule.utils.TimeUtil;

import java.util.List;


public class DBManager {
    private Context context;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    private static DBManager instance;

    public DBManager(Context context) {
        this.context = context;
        sqLiteOpenHelper = new ScheduleDBOpenHelper(context);
        dbReader = sqLiteOpenHelper.getReadableDatabase();
        dbWriter = sqLiteOpenHelper.getWritableDatabase();
    }

    //getInstance单例
    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    // 添加到数据库
    public void addToDB(String title, String content, String time, String priority, Long clockTime) {
        //  组装数据
        ContentValues cv = new ContentValues();
        cv.put(ScheduleDBOpenHelper.TITLE, title);
        cv.put(ScheduleDBOpenHelper.CONTENT, content);
        cv.put(ScheduleDBOpenHelper.TIME, time);
        cv.put(ScheduleDBOpenHelper.PRIORITY, priority);
        cv.put(ScheduleDBOpenHelper.CLOCKTIME, clockTime);
        dbWriter.insert(ScheduleDBOpenHelper.TABLE_NAME, null, cv);
    }

    //  读取数据(按照id顺序)
    public void readFromDBById(List<Schedule> scheduleList) {
        Cursor cursor = dbReader.query(ScheduleDBOpenHelper.TABLE_NAME, null, null,
                null, null, null, "_id");
        try {
            if (cursor.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule();
                    schedule.setId(cursor.getInt(cursor.getColumnIndex(ScheduleDBOpenHelper.ID)));
                    schedule.setTitle(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.TITLE)));
                    schedule.setContent(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.CONTENT)));
                    schedule.setTime(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.TIME)));
                    schedule.setPriority(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.PRIORITY)));
                    schedule.setClockTime(cursor.getLong(cursor.getColumnIndex(ScheduleDBOpenHelper.CLOCKTIME)));
//                     Log.d("TAG",note.getId()+"    title"+note.getTitle());
                    scheduleList.add(schedule);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  读取数据(按照clockTime顺序)
    public void readFromDBByClockTime(List<Schedule> scheduleList) {
        String currentMilisTime = TimeUtil.getCurrentMilisTime().toString();
        Cursor cursor = dbReader.query(ScheduleDBOpenHelper.TABLE_NAME, null, "clocKTime >= ?",
                new String[]{currentMilisTime}, null, null, "clocKTime");
        try {
            if (cursor.moveToFirst()) {
                do {
                    Schedule schedule = new Schedule();
                    schedule.setId(cursor.getInt(cursor.getColumnIndex(ScheduleDBOpenHelper.ID)));
                    schedule.setTitle(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.TITLE)));
                    schedule.setContent(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.CONTENT)));
                    schedule.setTime(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.TIME)));
                    schedule.setPriority(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.PRIORITY)));
                    schedule.setClockTime(cursor.getLong(cursor.getColumnIndex(ScheduleDBOpenHelper.CLOCKTIME)));
                    Log.d("TAG clocktime", schedule.getId() + "    clockTime" + schedule.getClockTime());
                    scheduleList.add(schedule);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //  更新数据
    public void updateNote(int noteID, String title, String content, String time, String priority, Long clockTime) {
        ContentValues cv = new ContentValues();
        cv.put(ScheduleDBOpenHelper.ID, noteID);
        cv.put(ScheduleDBOpenHelper.TITLE, title);
        cv.put(ScheduleDBOpenHelper.CONTENT, content);
        cv.put(ScheduleDBOpenHelper.TIME, time);
        cv.put(ScheduleDBOpenHelper.PRIORITY, priority);
        cv.put(ScheduleDBOpenHelper.CLOCKTIME, clockTime);
        dbWriter.update(ScheduleDBOpenHelper.TABLE_NAME, cv, "_id = ?", new String[]{noteID + ""});
    }

    //  删除数据
    public Boolean deleteNote(int noteID) {
        int delete = dbWriter.delete(ScheduleDBOpenHelper.TABLE_NAME, "_id = ?", new String[]{noteID + ""});
        return delete >= 1 ? true : false;
    }

    //删除数据库所有数据（通过升级版本实现）
    public void deleteAllNote(int newVersion) {
        sqLiteOpenHelper.onUpgrade(dbWriter, 3, newVersion);
    }


    // 根据id查询数据
    public Schedule readData(int noteID) {
        Cursor cursor = dbReader.rawQuery("SELECT * FROM schedule WHERE _id = ?", new String[]{noteID + ""});
        cursor.moveToFirst();
        Schedule schedule = new Schedule();
        schedule.setId(cursor.getInt(cursor.getColumnIndex(ScheduleDBOpenHelper.ID)));
        schedule.setPriority(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.PRIORITY)));
        schedule.setTitle(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.TITLE)));
        schedule.setContent(cursor.getString(cursor.getColumnIndex(ScheduleDBOpenHelper.CONTENT)));
        schedule.setClockTime(cursor.getLong(cursor.getColumnIndex(ScheduleDBOpenHelper.CLOCKTIME)));
        return schedule;
    }


}


