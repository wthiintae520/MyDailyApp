package algonquin.cst2335.finalgroupproject;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OwlbotMyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "OwlbotDatabase";
    public static final int version = 1;
    public static final String table_name = "Search Results";
    public static final String col_search = "Message";
    public static final String col_time_sent = "TimeSent";

    public OwlbotMyOpenHelper(Context context) { super(context, name, null, version); }

    @Override
    public void onCreate(SQLiteDatabase odb) {
        odb.execSQL("CREATE TABLE " + table_name + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + col_search + " TEXT,"
        + col_time_sent + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase odb, int oldVersion, int newVersion) {
        odb.execSQL("drop table if exists " + table_name);
        onCreate(odb);
    }
}


