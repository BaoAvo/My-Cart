package hanu.a2_1901040030.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class ProductDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "ProductDBHelper";
    private static final String DB_NAME = "products.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "products";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String THUMBNAIL = "thumbnail";
    private static final String UNIT_PRICE = "unitPrice";
    private static final String QUANTITY = "quantity";
;    public ProductDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG,"Create table");
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL, " +
                THUMBNAIL + " TEXT NOT NULL," +
                UNIT_PRICE + " INTEGER NOT NULL," +
                QUANTITY + " INTEGER)" ;
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
