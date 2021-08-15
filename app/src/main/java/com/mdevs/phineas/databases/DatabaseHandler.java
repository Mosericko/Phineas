package com.mdevs.phineas.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mdevs.phineas.classes.CartDetails;
import com.mdevs.phineas.classes.User;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Constants
    private static final String DATABASE_NAME = "Phineas";
    private static final int DATABASE_VERSION = 1;

    //Database tables
    private static final String USER_TABLE = "userProfile";
    private static final String CART_TABLE = "phineasCart";

    //table fields for userProfile
    private static final String USER_ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String GENDER = "gender";
    private static final String EMAIL_ADDRESS = "emailAddress";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String USER_TYPE = "userType";

    //table fields for adding to Cart
    public static final String PRIMARY_ID = "primary_id";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_TAG = "tag";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_QUANTITY = "quantity";


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String PRIMARY_USER_ID = "primary_id";
        String userProfileSQl = "CREATE TABLE " + USER_TABLE + "(" + PRIMARY_USER_ID + " INTEGER PRIMARY KEY, " +
                USER_ID + " INTEGER, " +
                FIRST_NAME + " TEXT, " +
                LAST_NAME + " TEXT, " +
                GENDER + " TEXT, " +
                EMAIL_ADDRESS + " VARCHAR, " +
                PHONE_NUMBER + " VARCHAR, " +
                USER_TYPE + " VARCHAR " + ");";

        db.execSQL(userProfileSQl);

        String cartSQL = "CREATE TABLE " + CART_TABLE + "(" + PRIMARY_ID + " INTEGER PRIMARY KEY, "
                + PRODUCT_ID + " INTEGER, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_PRICE + " VARCHAR, "
                + PRODUCT_IMAGE + " VARCHAR, "
                + PRODUCT_TAG + " VARCHAR, "
                + PRODUCT_QUANTITY + " INTEGER " + ");";

        db.execSQL(cartSQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if the tables already exist
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        onCreate(db);
    }


    //add user to database
    public void addUser(User user) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, String.valueOf(user.getId()));
        cv.put(FIRST_NAME, user.getFirstName());
        cv.put(LAST_NAME, user.getLastName());
        cv.put(GENDER, user.getGender());
        cv.put(EMAIL_ADDRESS, user.getEmail());
        cv.put(PHONE_NUMBER, user.getPhone());
        cv.put(USER_TYPE, user.getUserType());

        //insert row in myDb
        myDb.insert(USER_TABLE, null, cv);
        myDb.close();
    }

    //get User Details From myDb using their id
    public User getUser(String id) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        //Cursor userCursor= myDb.query(USER_TABLE,new String[]{FIRST_NAME,LAST_NAME,EMAIL_ADDRESS,PHONE_NUMBER},null,null,null,
        //null,null);

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_ID + " = '" + id + "'";
        Cursor userCursor = myDb.rawQuery(sql, null);

        if (userCursor != null)
            userCursor.moveToFirst();

        assert userCursor != null;
        User user = new User(
                userCursor.getString(userCursor.getColumnIndex(USER_ID)),
                userCursor.getString(userCursor.getColumnIndex(FIRST_NAME)),
                userCursor.getString(userCursor.getColumnIndex(LAST_NAME)),
                userCursor.getString(userCursor.getColumnIndex(GENDER)),
                userCursor.getString(userCursor.getColumnIndex(PHONE_NUMBER)),
                userCursor.getString(userCursor.getColumnIndex(EMAIL_ADDRESS)),
                userCursor.getString(userCursor.getColumnIndex(USER_TYPE))

        );
        userCursor.close();
        myDb.close();

        return user;

    }

    public void addToCart(CartDetails products) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRODUCT_ID, products.getId());
        cv.put(PRODUCT_NAME, products.getName());
        cv.put(PRODUCT_PRICE, products.getPrice());
        cv.put(PRODUCT_IMAGE, products.getImage());
        cv.put(PRODUCT_TAG, products.getTag());
        cv.put(PRODUCT_QUANTITY, products.getQuantity());

        myDb.insert(CART_TABLE, null, cv);
        myDb.close();
    }

    public ArrayList<CartDetails> getCartDetails() {
        ArrayList<CartDetails> cartList = new ArrayList<>();
        SQLiteDatabase myDb = this.getReadableDatabase();
        String sql = "SELECT * FROM " + CART_TABLE;

        Cursor cursor = myDb.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                CartDetails cartDetails = new CartDetails();
                cartDetails.setId(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                cartDetails.setName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                cartDetails.setPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)));
                cartDetails.setImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE)));
                cartDetails.setTag(cursor.getString(cursor.getColumnIndex(PRODUCT_TAG)));
                cartDetails.setQuantity(cursor.getString(cursor.getColumnIndex(PRODUCT_QUANTITY)));

                cartList.add(cartDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        myDb.close();

        return cartList;
    }

    public void deleteCartItems() {
        //this method will be called out once the user logs out of their account
        SQLiteDatabase myDb = this.getWritableDatabase();
        String sQL = "DELETE FROM " + CART_TABLE;
        myDb.execSQL(sQL);
        myDb.close();
    }


    public boolean checkIfRowExists(CartDetails cartDetails) {
        boolean exists;
        SQLiteDatabase myDb = this.getReadableDatabase();
        String checkQuery = "SELECT * FROM " + CART_TABLE + " WHERE id = " + cartDetails.getId();
        Cursor cursor = myDb.rawQuery(checkQuery, null);
        exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

}
