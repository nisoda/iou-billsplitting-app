package a.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nicole on 3/19/2015.
 */
public class SQLiteHelperBills extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bills.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BILLS = "bills";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BILL_NAME = "bill_name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TIP = "tip";
    public static final String COLUMN_TAX = "tax";
    public static final String COLUMN_TOTAL = "total";
    public static final String COLUMN_PARTICIPANTS = "participants";
    public static final String COLUMN_AMT_PER = "amt_per";


    //Database CREATE TABLE statement
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_BILLS +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BILL_NAME + " TEXT  NOT NULL, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_AMOUNT + " REAL, " +
            COLUMN_TIP + " REAL, " +
            COLUMN_TAX + " REAL, " +
            COLUMN_TOTAL + " REAL, " +
            COLUMN_PARTICIPANTS + " TEXT, " +
            COLUMN_AMT_PER + " REAL );";
    //Assume unique friend names

    private static final String QUERY_BILLS_ALL = "SELECT * FROM " + TABLE_BILLS + ";";
    private static final String QUERY_BILLS_NAMES_DATES = "SELECT " + COLUMN_BILL_NAME +
            ", " + COLUMN_DATE + " FROM " + TABLE_BILLS + ";";

    public SQLiteHelperBills(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public boolean saveBill(String billName, String date, double amount, double tip, double tax, double total, String participants, double amtPer)
    {
        Cursor cursor = getBill(billName, date);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BILL_NAME, billName);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_AMOUNT, amount);
        contentValues.put(COLUMN_TIP, tip);
        contentValues.put(COLUMN_TAX, tax);
        contentValues.put(COLUMN_TOTAL, total);
        contentValues.put(COLUMN_PARTICIPANTS, participants);
        contentValues.put(COLUMN_AMT_PER, amtPer);

        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put(COLUMN_BILL_NAME, billName);
            contentValues.put(COLUMN_DATE, date);
            result = db.insert(TABLE_BILLS, null, contentValues);
        } else { // Record exists
            result = db.update(TABLE_BILLS, contentValues, COLUMN_BILL_NAME + "=? AND " +
                    COLUMN_DATE + "=?" , new String[] { billName, date });
        }
//
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getBill(String billName, String date){

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_BILLS + " WHERE bill_Name=? AND date=?";

        return db.rawQuery(sql, new String[] { billName, date } );
    }

    public Cursor getAllBills(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(QUERY_BILLS_ALL, new String[]{});
    }

}