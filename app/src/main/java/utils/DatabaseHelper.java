package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import models.SkillModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "skill_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SkillModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+SkillModel.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertSkill(final String skillName, final String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SkillModel.COLUMN_ID,ID);
        contentValues.put(SkillModel.COLUMN_DATA,skillName);
        long id = db.insert(SkillModel.TABLE_NAME,null,contentValues);
        db.close();
        return id;
    }

    public SkillModel getSkills(long id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SkillModel.TABLE_NAME,new String[]{SkillModel.COLUMN_ID,SkillModel.COLUMN_DATA},SkillModel.COLUMN_ID+"=?",new String[]{SkillModel.COLUMN_ID},null,null,null,null);
        if (cursor!=null)
            cursor.moveToFirst();
        assert cursor != null;
        SkillModel skillModel = new SkillModel(
                cursor.getString(cursor.getColumnIndex(SkillModel.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SkillModel.COLUMN_DATA)));
        cursor.close();
        return skillModel;
    }

    public int getSkillCount()
    {
        String selectQuery = "SELECT * FROM "+SkillModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteSKill()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(SkillModel.TABLE_NAME,SkillModel.COLUMN_ID+"=?",new String[]{SkillModel.COLUMN_ID});
        database.close();
    }
}
