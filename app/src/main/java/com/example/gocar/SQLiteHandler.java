package com.example.gocar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_REVIEWS = "reviews";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_AGE = "age";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_REVIEW_ID = "reviewId";
    private static final String KEY_UID_REVIEW = "UniqueId";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_CAR_ID = "Carid";
    private static final String KEY_REVIEW = "UserReview";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE "
                + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT,"
                + KEY_AGE + " INTGER,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_PHONE + " INTGER,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_REVIEWS + "("
                + KEY_REVIEW_ID + " INTEGER PRIMARY KEY ," + KEY_UID_REVIEW + " TEXT," + KEY_USER_ID + " TEXT,"
                + KEY_CAR_ID + " INTEGER," + KEY_REVIEW + " TEXT" + ")";
        db.execSQL(CREATE_REVIEW_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String age, String country, String phone , String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_AGE, age); // age
        values.put(KEY_COUNTRY, country); // country
        values.put(KEY_PHONE, phone); // phone number
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public void addReview(String UniqueId , String userid , String Carid , String UserReview) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID_REVIEW , UniqueId);
        values.put(KEY_USER_ID , userid);
        values.put(KEY_CAR_ID , Carid);
        values.put(KEY_REVIEW , UserReview);
        long id = db.insert(TABLE_REVIEWS, null, values);
        db.close();
        Log.d(TAG, "New review has been inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("age", cursor.getString(4));
            user.put("country", cursor.getString(5));
            user.put("phonenumber", cursor.getString(6));
            user.put("created_at", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}