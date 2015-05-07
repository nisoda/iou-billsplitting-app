package a.myapplication1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Naoki on 5/4/2015.
 */
public class SQLiteHelperIsParticipant extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "is_participant.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_IS_PARTICIPANT = "is_participant";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BILL_NAME = "bill_name";
    public static final String COLUMN_PARTICIPANT_NAME = "participant_name";
    public static final String COLUMN_AMT_OWED = "amt_owed";


    //Database CREATE TABLE statement
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_IS_PARTICIPANT +
            " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BILL_NAME + " TEXT  NOT NULL, " +
            COLUMN_PARTICIPANT_NAME + " TEXT  NOT NULL, " +
            COLUMN_AMT_OWED + " TEXT);";

    public SQLiteHelperIsParticipant(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IS_PARTICIPANT);
        onCreate(db);
    }

    public boolean saveParticipant (String billName, String participantName, String amtOwed)
    {
        Cursor cursor = getParticipantIn(billName, participantName);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BILL_NAME, billName);
        contentValues.put(COLUMN_PARTICIPANT_NAME, participantName);
        contentValues.put(COLUMN_AMT_OWED, amtOwed);

        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put(COLUMN_BILL_NAME, billName);
            result = db.insert(TABLE_IS_PARTICIPANT, null, contentValues);
        } else { // Record exists
            result = db.update(TABLE_IS_PARTICIPANT, contentValues, COLUMN_BILL_NAME + "=? and " +
                    COLUMN_PARTICIPANT_NAME + "=?", new String[] { billName, participantName });
        }
//
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getParticipantIn(String billName, String participantName){

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM "+ TABLE_IS_PARTICIPANT + " WHERE participant_name=?";

        return db.rawQuery(sql, new String[] { participantName } );
    }

//    public Cursor getAllFriends(){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        return db.rawQuery(QUERY_FRIENDS_ALL, new String[]{});
//    }

}
