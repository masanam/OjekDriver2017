package driverside.ojekkeren.com.ojekkerendriverside;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andi on 4/29/2016.
 */
public class DBDriverAccount  extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ojek";

    // Contacts table name
    private static final String TABLE_MEMBER = "DriversAccount";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "drivername";
    private static final String DRIVER_NIK = "drivernik";
    private static final String KEY_PH_NO = "phonenum";
    private static final String IS_LOGGED = "isLogged";

    public DBDriverAccount(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void reCreateDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEMBER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_NAME + " TEXT," +
                DRIVER_NIK + " TEXT," +
                KEY_PH_NO + " TEXT," +
                IS_LOGGED + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEMBER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_NAME + " TEXT," +
                DRIVER_NIK + " TEXT," +
                KEY_PH_NO + " TEXT," +
                IS_LOGGED + " TEXT)";
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);

        // Create tables again
        onCreate(db);
    }

    // Getting All Contacts
    public POJODrivers getCurrentMemberDetails() {

        String query = "select * from DriversAccount where id=0";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        POJODrivers member = new POJODrivers();
        if (cursor != null && cursor.moveToFirst()){
            member.setId(cursor.getInt(0));
            member.setDrivername(cursor.getString(1));
            member.setDrivernik(cursor.getString(2));
            member.setPhonenum(cursor.getString(3));
            member.setIsLogged(cursor.getString(4));
        }
        // return contact list
        return member;
    }

    public boolean isLoggedInOrNot(){
        return false;
    }

    // Adding new memberbaru
    void startUpMemberTable(POJODrivers memberAkun) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, memberAkun.getIsLogged());
        values.put(KEY_PH_NO, memberAkun.getPhonenum());
        values.put(KEY_ID, memberAkun.getId());
        values.put(IS_LOGGED, memberAkun.getIsLogged());

        // Inserting Row
        db.insert(TABLE_MEMBER, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public void setLoginFlag(POJODrivers akun) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.execSQL("update DriversAccount set isLogged=1,drivernik="+akun.getDrivernik()+",drivername='"+akun.getDrivername()+"'");
//        return db.update(TABLE_MEMBER, values, KEY_ID + " = 0",new String[] { String.valueOf("0")});
    }

    // Updating single contact
    public int setLogout() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DRIVER_NIK, "0");
        values.put(IS_LOGGED, "0");
        return db.update(TABLE_MEMBER, values, KEY_ID + " = ?",new String[] { String.valueOf("0")});
    }

    // Updating single contact
    public int setReset(POJODrivers akun) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DRIVER_NIK, "0");
        values.put(IS_LOGGED, "0");
        return db.update(TABLE_MEMBER, values, DRIVER_NIK + " = ?",new String[] { String.valueOf(akun.getId())});
    }


}
