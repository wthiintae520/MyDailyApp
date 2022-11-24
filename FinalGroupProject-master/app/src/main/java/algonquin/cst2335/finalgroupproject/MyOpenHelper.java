
package algonquin.cst2335.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "CovidDatabase";
    public static final int version = 2;
    public static final String TABLE_NAME = "Searches";
    public static final String COL_ID = "_id";
   // public static final String COL_SEARCH = "Search";
   // public static final String COL_TIME_SENT = "TimeSent";
    public static final String COL_DATE = "Date";
    public static final String COL_CASE = "Cases";
    public static final String COL_FATALITIES = "Fatalities";
    public static final String COL_HOSPITALS = "Hospitalizations";
    public static final String COL_VACCINATIONS = "Vaccinations";
    public static final String COL_RECOVERIES = "Recoveries";

    public MyOpenHelper(Context context) { super(context, name, null, version); }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DATE + " TEXT,"
                + COL_CASE + " INTEGER,"
                + COL_FATALITIES + " INTEGER,"
                + COL_HOSPITALS + " INTEGER,"
                + COL_VACCINATIONS + " INTEGER,"
                + COL_RECOVERIES + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
