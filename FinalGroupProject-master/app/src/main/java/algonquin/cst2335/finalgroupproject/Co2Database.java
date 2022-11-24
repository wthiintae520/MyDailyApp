package algonquin.cst2335.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Co2Database extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";//filename
    public static final int version = 1;//data columns
    public static final String TABLE_NAME = "History";//data columns
    public static final String columnCarBrand = "CarBrand";
    public static final String columnCarType = "CarType";
    public static final String columnDistance = "Distance";
    public static final String columnCarbon_g = "Carbon_g";
    public static final String columnCarbon_lb = "Carbon_lb";
    public static final String columnCarbon_kg = "Carbon_kg";

    public Co2Database(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//create database file in case none exits
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                columnCarBrand + " TEXT," +
                columnCarType + " TEXT," +
                columnDistance + " TEXT," +
                columnCarbon_g + " TEXT," +
                columnCarbon_lb + " TEXT," +
                columnCarbon_kg + " TEXT" +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {//rebuild the table if add or remove columns
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);//reuse onCreate() to create the new database
    }
}