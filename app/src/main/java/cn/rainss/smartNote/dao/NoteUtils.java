package cn.rainss.smartNote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.rainss.smartNote.adapter.entity.Note;
import cn.rainss.smartNote.utils.TimeUtils;

/**
 * 笔记操作类
 */
public class NoteUtils extends SQLiteOpenHelper {

    private static final String sqliteFileName = "rains.db";

    private static final int sqliteVersion = 3;

    private SQLiteDatabase mDb;

    public NoteUtils(Context context) {
        super(context, sqliteFileName, null, sqliteVersion);
    }

    /**
     * 创建表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建笔记表
        db.execSQL("create table \"note\"(\"id\" integer not null primary key autoincrement,\"title\" text not null,\"content\" text not null,\"type_id\" integer not null,\"create_time\" timestamp not null,\"update_time\" timestamp not null)");
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

    /**
     * 添加笔记
     * @param note
     * @return
     */
    public Boolean insert(Note note){
        if(note != null){
            SQLiteDatabase writableDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            //设置标题
            cv.put("title",note.getTitle());
            //设置内容
            cv.put("content",note.getContent());
            //设置分类
            cv.put("type_id",note.getTypeId());
            //创建时间
            cv.put("create_time", TimeUtils.timeToString(note.getCreateTime()));
            //更新时间
            cv.put("update_time",TimeUtils.timeToString(note.getUpdateTime()));
            writableDatabase.insert("note",null,cv);
            writableDatabase.close();
            return true;
        }else{
            return false;
        }
    }

    /**
     * 笔记更新
     * @param note
     * @return
     */
    public Boolean update(Note note){
        if(note != null){
            SQLiteDatabase writableDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            //设置标题
            cv.put("title",note.getTitle());
            //设置内容
            cv.put("content",note.getContent());
            //设置分类
            cv.put("type_id",note.getTypeId());
            //更新时间
            cv.put("update_time",TimeUtils.timeToString(note.getUpdateTime()));
            writableDatabase.update("note",cv,"id=?",new String[]{String.valueOf(note.getId())});
            writableDatabase.close();
            return true;
        }else{
            return false;
        }
    }

    /**
     * 删除笔记
     * @param id
     * @return
     */
    public Boolean delete(Long id){
        if(id > 0){
            SQLiteDatabase writableDatabase = this.getWritableDatabase();
            writableDatabase.delete("note","id=?",new String[]{String.valueOf(id)});
            return true;
        }else{
            return false;
        }
    }

}
