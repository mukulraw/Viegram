package com.relinns.viegram.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "viegram.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 11;
    private static final String TABLE_LEARNING = "user_data";
    private static final String CREATE_TABLE_LEARNING = "CREATE TABLE user_data (id integer primary key autoincrement, content_type text,score text,value1 text,value2 text,value3 text,date text)";

    private final Context mContext;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 11);
         DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        //DB_PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        this.mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_LEARNING);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase();
            close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean checkDataBase() {
        return new File(DB_PATH + DB_NAME).exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = this.mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        while (true) {
            int mLength = mInput.read(mBuffer);
            if (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength);
            } else {
                mOutput.flush();
                mOutput.close();
                mInput.close();
                return;
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS CREATE TABLE learning (id integer primary key autoincrement, content_type text,score text,value1 text,value2 text,value3 text,date text)");
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public void insertValues(UserData data) {
        ContentValues values = new ContentValues();
        values.put("content_type", learn.getContentType());
        values.put("score", learn.getScore());
        values.put("value1", learn.getValue1());
        values.put("value2", learn.getValue2());
        values.put("value3", learn.getValue3());
        values.put("date", learn.getDate());
        getWritableDatabase().insert(TABLE_LEARNING, null, values);
    }

    public UserData getSingleValues(String contentType) {
        String selectQuery = "SELECT  * FROM learning where content_type=?";
        Cursor cursor = getWritableDatabase().rawQuery(selectQuery, new String[] { contentType });
        Learning learn = new Learning();

        if (cursor.moveToFirst()) {
            learn.setId(cursor.getString(0));
            learn.setContentType(cursor.getString(1));
            learn.setScore(cursor.getString(2));
            learn.setValue1(cursor.getString(3));
            learn.setValue2(cursor.getString(4));
            learn.setValue3(cursor.getString(5));
            learn.setDate(cursor.getString(6));
        }
        cursor.close();

        return learn;
    }

    public String getScore(String contentType) {
        String selectQuery = "SELECT  * FROM learning where content_type=?";
        Cursor c = getWritableDatabase().rawQuery(selectQuery, new String[] { contentType });
        String score="";
        if(c != null) {
            while (c.moveToNext()) {
                score = c.getString(c.getColumnIndex("score"));

            }
        }
        return score;
    }

    public void updateScore(String contentType,String score) {

        ContentValues newValues = new ContentValues();
        newValues.put("score", score);

        String[] args = new String[]{contentType};
        getWritableDatabase().update(TABLE_LEARNING, newValues, "content_type=? ", args);

    }

    public ArrayList<Learning> getAllValues() {
        String selectQuery = "SELECT  * FROM learning";
        Cursor cursor = getWritableDatabase().rawQuery(selectQuery,null);
        Learning learn = new Learning();
        ArrayList<Learning> learnings = new ArrayList<>();
        cursor.moveToFirst();
        do {
            learn.setId(cursor.getString(0));
            learn.setContentType(cursor.getString(1));
            learn.setScore(cursor.getString(2));
            learn.setValue1(cursor.getString(3));
            learn.setValue2(cursor.getString(4));
            learn.setValue3(cursor.getString(5));
            learn.setDate(cursor.getString(6));

            learnings.add(learn);
        }while (cursor.moveToNext());

        cursor.close();

        return learnings;
    }
*/


  /*  public void insert_exp_type_values(ExpenseTypeBean bean) {
        ContentValues values = new ContentValues();
        values.put("id", bean.getExpense_id());
        values.put("expense_name", bean.getExpense_name());
        getWritableDatabase().insert("expense_type_tb", null, values);
    }

    public void insertViewExpenseValues(ViewExpenseBean exp) {
        ContentValues values = new ContentValues();
        values.put("event_name", exp.getExpense_name());
        values.put("event_type", exp.getExpense_type());
        values.put("event_date", exp.getExpense_date());
        values.put("event_desc", exp.getEvent_desc());
        values.put("simple_date", exp.getSimple_date());
        values.put("amount", exp.getAmount());
        values.put("simple_desc", exp.getSimple_desc());
        values.put("image_url", exp.getImage_url());
        values.put("is_checked", "false");
        getWritableDatabase().insert(TABLE_cart, null, values);
    }

    public boolean deleteItem() {
        try {
            if (getWritableDatabase().delete(TABLE_cart, "is_checked=true", null) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void deleteAllRows() {
        getWritableDatabase().execSQL("delete from expense_type_tb");
    }
*/



    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LEARNING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
