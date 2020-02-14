package com.aboukalil.phoneinsider1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Test.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    SQLiteDatabase db;

    private static final String DATABASE_PATH = "/data/data/com.aboukalil.phoneinsider/databases/";
    private final String USER_TABLE = "User";
    private final String REPORT_TABLE = "Report";

    //Report Table Columns names
    private static final String ID = "id";
    private static final String CONTACT = "contact";
    private static final String DATE = "date";
    private static final String TASK = "task";

    public Context getContext() {
        return context;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        createDb();
        db = openDatabase();
        close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    public void createDb(){
        boolean dbExist = checkDbExist();
        if(!dbExist){
            this.getReadableDatabase();
            copyDatabase();
        }
    }
    private boolean checkDbExist(){
        SQLiteDatabase sqLiteDatabase = null;

        try{
            String path = DATABASE_PATH + DATABASE_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception ex){
        }

        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
            return true;
        }

        return false;
    }

    private void copyDatabase(){
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            String outFileName = DATABASE_PATH + DATABASE_NAME;

            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] b = new byte[1024];
            int length;

            while ((length = inputStream.read(b)) > 0){
                outputStream.write(b, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDatabase openDatabase(){
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public boolean checkUserExist(String username, String password){
        String[] columns = {"username"};
        db = openDatabase();
        String selection = "username=? and password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean signUp(String userName,String password)
    {
        SQLiteDatabase db = openDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put("username", userName);
        newValues.put("password",password);
        long result =db.insert(USER_TABLE,null,newValues);
        close();
        if (result== -1)
            return false;
        else
            return true;
    }

    public boolean CountUser()
    {
        String query = "SELECT  * FROM " + USER_TABLE;
        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public int changePassword(String username, String old_password ,String new_password)
    {
        SQLiteDatabase dataBase = openDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("password",new_password);
        int result= dataBase.update(USER_TABLE, newValues, "username ='"+username+"' AND "+"password ='"+old_password+"'", null);
        close();
        return (int)result;
    }

    public String getPassword()
    {
        SQLiteDatabase db = openDatabase();
        String query = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()<1) // UserName Not Exist
        {
            close();
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("password"));
        cursor.close();
        close();
        return password;
    }

    public String getUsername()
    {
        SQLiteDatabase db = openDatabase();
        String query = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()<1) // UserName Not Exist
        {
            close();
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String username= cursor.getString(cursor.getColumnIndex("username"));
        cursor.close();
        close();
        return username;
    }


    public void addReportNode(String date , String task ,String contact)
    {
        try
        {
            SQLiteDatabase db = openDatabase();
            ContentValues newValues = new ContentValues();
            newValues.put(DATE, date);
            newValues.put(CONTACT,contact);
            newValues.put(TASK,task);

            db.insert(REPORT_TABLE, null, newValues);
            close();
        }
        catch(Exception ex) {}
    }

    public ArrayList<ReportNode> getAllNotes() {
        SQLiteDatabase db = this.openDatabase();
        ArrayList<ReportNode> listItems = new ArrayList<ReportNode>();
        String query = "SELECT  * FROM " + REPORT_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            do {
                ReportNode note = new ReportNode();
                note.setDate(cursor.getString(cursor.getColumnIndex(ID)));
                note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                note.setContact(cursor.getString(cursor.getColumnIndex(CONTACT))) ;
                note.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                listItems.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return listItems;
    }


    public void deleteNode(int id) {
        SQLiteDatabase db = this.openDatabase();
        db.delete(REPORT_TABLE,"id = ? " , new String[]{Integer.toString(id)});
        close();
    }


}
