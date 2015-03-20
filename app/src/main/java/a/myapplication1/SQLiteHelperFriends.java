package a.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicole on 3/18/2015.
 */
public class SQLiteHelperFriends extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "friends.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FRIENDS = "friends";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";


    //Database CREATE TABLE statement
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_FRIENDS +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT  NOT NULL, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_EMAIL + " TEXT);";
            //Assume unique friend names

    private static final String QUERY_FRIENDS_ALL = "SELECT * FROM " + TABLE_FRIENDS + ";";
    private static final String QUERY_FRIENDS_NAMES = "SELECT " + COLUMN_NAME + " FROM " + TABLE_FRIENDS + ";";

    public SQLiteHelperFriends(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }

    public boolean saveFriend (String name, String phone, String email)
    {
        Cursor cursor = getFriend(name);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);

        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put(COLUMN_NAME, name);
            result = db.insert(TABLE_FRIENDS, null, contentValues);
        } else { // Record exists
            result = db.update(TABLE_FRIENDS, contentValues, COLUMN_NAME + "=?", new String[] { name });
        }
//
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getFriend(String name){

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM "+ TABLE_FRIENDS + " WHERE name=?";

        return db.rawQuery(sql, new String[] { name } );
    }

    public Cursor getAllFriends(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(QUERY_FRIENDS_ALL, new String[]{});
    }

}
