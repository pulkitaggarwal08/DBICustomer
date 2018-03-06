package dbicustomer.com.agicon.dbi.dbicutomer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import dbicustomer.com.agicon.dbi.dbicutomer.models.AddCategoryItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddSubCategoryItem;

public class SQLController {

    private SQLiteDatabase sqLiteDatabase;
    private DataBaseHandler dataBaseHandler;
    private Context context;

    public SQLController(Context context) {
        this.context = context;
        dataBaseHandler = new DataBaseHandler(context);
    }

    public SQLController open() throws SQLException {
        sqLiteDatabase = dataBaseHandler.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHandler.close();
    }

    public void addNewItem(String new_item_name, String new_item_price, String new_item_quantity, String new_item_total_price,
                           String new_item_key_id , String total_price_included_tax) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_NAME, new_item_name);
        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_PRICE, new_item_price);
        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY, new_item_quantity);
        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, new_item_total_price);
        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID, new_item_key_id);
        contentValues.put(DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE_PLUS_TAX, total_price_included_tax);

        sqLiteDatabase.insert(DataBaseHandler.TABLE_KEYPAD, null, contentValues);
    }

    public AddNewItem getItem(String id) {
        AddNewItem contact = null;
        SQLiteDatabase db = dataBaseHandler.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHandler.TABLE_KEYPAD, new String[]{DataBaseHandler.COLUMN_NEW_ITEM_NAME,
                        DataBaseHandler.COLUMN_NEW_ITEM_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY,
                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID,
                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE_PLUS_TAX},
                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst() == true) {
                contact = new AddNewItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            } else {
                return contact;
            }
        }

        // return contact
        return contact;
    }

    public AddCategoryItem getCategoryItem(String id) {
        AddCategoryItem contact = null;
        SQLiteDatabase db = dataBaseHandler.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHandler.TABLE_KEYPAD, new String[]{DataBaseHandler.COLUMN_NEW_ITEM_NAME,
                        DataBaseHandler.COLUMN_NEW_ITEM_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY,
                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID},
                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst() == true) {
                contact = new AddCategoryItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
            } else {
                return contact;
            }
        }

        // return contact
        return contact;
    }

    public AddSubCategoryItem getSubCategoryItem(String id) {
        AddSubCategoryItem contact = null;
        SQLiteDatabase db = dataBaseHandler.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHandler.TABLE_KEYPAD, new String[]{DataBaseHandler.COLUMN_NEW_ITEM_NAME,
                        DataBaseHandler.COLUMN_NEW_ITEM_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY,
                        DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID},
                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.moveToFirst() == true) {
                contact = new AddSubCategoryItem(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
            } else {
                return contact;
            }
        }

        // return contact
        return contact;
    }

    public ArrayList<AddNewItem> getAllItems() {
        ArrayList<AddNewItem> list = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DataBaseHandler.TABLE_KEYPAD, null);

        if (cursor.moveToFirst()) {
            do {
                AddNewItem addNewItem = new AddNewItem();
                addNewItem.setNew_item_name(cursor.getString(0));
                addNewItem.setNew_item_price(cursor.getString(1));
                addNewItem.setNew_item_quantity(cursor.getString(2));
                addNewItem.setNew_item_total_price(cursor.getString(3));
                addNewItem.setNew_item_key_id(cursor.getString(4));
                addNewItem.setNew_item_total_price_included_tax(cursor.getString(5));

                list.add(addNewItem);

            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public int updateNewItem(String new_item_price, String new_item_quantity, String new_item_total_price, String new_item_key_id) {


        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.COLUMN_NEW_ITEM_QUANTITY, new_item_quantity);
        values.put(DataBaseHandler.COLUMN_NEW_ITEM_TOTAL_PRICE, new_item_total_price);

        // updating row
        return sqLiteDatabase.update(DataBaseHandler.TABLE_KEYPAD, values,
                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + " = ?", new String[]{new_item_key_id});
    }

    public void deleteNewItem(String new_item_key_id) {
        sqLiteDatabase.delete(DataBaseHandler.TABLE_KEYPAD,
                DataBaseHandler.COLUMN_NEW_ITEM_KEY_ID + "= ?", new String[]{new_item_key_id});
    }

    public void removeAll() {
        SQLiteDatabase db = dataBaseHandler.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(DataBaseHandler.TABLE_KEYPAD, null, null);
    }

}
