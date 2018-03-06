package dbicustomer.com.agicon.dbi.dbicutomer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_DBI_SHOPKEEPER = "dbiCustomer";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_KEYPAD = "keypad";
    public static final String COLUMN_NEW_ITEM_NAME = "newItemName";
    public static final String COLUMN_NEW_ITEM_QUANTITY = "newItemQuantity";
    public static final String COLUMN_NEW_ITEM_PRICE = "newItemPrice";
    public static final String COLUMN_NEW_ITEM_TOTAL_PRICE = "newItemTtalPrice";
    public static final String COLUMN_NEW_ITEM_KEY_ID = "newItemKeyId";
    public static final String COLUMN_NEW_ITEM_TOTAL_PRICE_PLUS_TAX = "newItemTtalPricePlusTax";

    private static final String CREATE_TABLE_DBI = "CREATE TABLE " + TABLE_KEYPAD + "(" +
            COLUMN_NEW_ITEM_NAME + " TEXT," +
            COLUMN_NEW_ITEM_PRICE + " TEXT," +
            COLUMN_NEW_ITEM_QUANTITY + " TEXT," +
            COLUMN_NEW_ITEM_TOTAL_PRICE + " TEXT," +
            COLUMN_NEW_ITEM_KEY_ID + " TEXT," +
            COLUMN_NEW_ITEM_TOTAL_PRICE_PLUS_TAX + " TEXT);";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_DBI_SHOPKEEPER, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DBI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEYPAD);
        onCreate(db);
    }

}
