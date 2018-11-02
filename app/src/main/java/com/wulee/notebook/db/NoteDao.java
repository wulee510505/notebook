package com.wulee.notebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wulee.notebook.bean.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：笔记处理
 */

public class NoteDao {
    private MyOpenHelper helper;

    public NoteDao(Context context) {
        helper = new MyOpenHelper(context);
    }

    /**
     * 查询所有笔记
     */
    public List<Note> queryNotesAll() {
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        Note note ;
        String sql ;
        Cursor cursor = null;
        try {
            sql = "select * from db_note " ;
            cursor = db.rawQuery(sql, null);
            //cursor = db.query("note", null, null, null, null, null, "n_id desc");
            while (cursor.moveToNext()) {
                //循环获得展品信息
                note = new Note();
                note.setId(cursor.getString(cursor.getColumnIndex("n_id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("n_title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("n_content")));
                note.setType(cursor.getInt(cursor.getColumnIndex("n_type")));
                note.setBgColor(cursor.getString(cursor.getColumnIndex("n_bg_color")));
                note.setIsEncrypt(cursor.getInt(cursor.getColumnIndex("n_encrypt")));
                note.setUpdatedAt(cursor.getString(cursor.getColumnIndex("n_update_time")));
                noteList.add(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return noteList;
    }

    /**
     * 插入笔记
     */
    public long insertNote(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into db_note(n_id,n_title,n_content," +
                "n_type,n_bg_color,n_encrypt,n_create_time,n_update_time) " +
                "values(?,?,?,?,?,?,?,?)";

        long ret = 0;
        //sql = "insert into ex_user(eu_login_name,eu_create_time,eu_update_time) values(?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        try {
            stat.bindString(1, note.getObjectId());
            stat.bindString(2, note.getTitle());
            stat.bindString(3, note.getContent());
            stat.bindLong(4, note.getType());
            stat.bindString(5, note.getBgColor());
            stat.bindLong(6, note.getIsEncrypt());
            stat.bindString(7, note.getCreatedAt());
            stat.bindString(8, note.getUpdatedAt());
            ret = stat.executeInsert();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }

    /**
     * 更新笔记
     * @param note
     */
    public void updateNote(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("n_title", note.getTitle());
        values.put("n_content", note.getContent());
        values.put("n_type", note.getType());
        values.put("n_bg_color", note.getBgColor());
        values.put("n_encrypt", note.getIsEncrypt());
        values.put("n_create_time", note.getCreatedAt());
        values.put("m_update_time", note.getUpdatedAt());
        db.update("db_note", values, "n_id=?", new String[]{note.getId()+""});
        db.close();
    }

    /**
     * 删除笔记
     */
    public int deleteNote(String noteId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            ret = db.delete("db_note", "n_id=?", new String[]{noteId + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

    /**
     * 批量删除笔记
     *
     * @param mNotes
     */
    public int deleteNote(List<Note> mNotes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            if (mNotes != null && mNotes.size() > 0) {
                db.beginTransaction();//开始事务
                try {
                    for (Note note : mNotes) {
                        ret += db.delete("db_note", "n_id=?", new String[]{note.getId() + ""});
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

    /**
     * 删除所有笔记
     */
    public  void deleteAllNote() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "delete from db_note";
        try {
            db.beginTransaction();//开始事务
            try {
                db.execSQL(sql);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
