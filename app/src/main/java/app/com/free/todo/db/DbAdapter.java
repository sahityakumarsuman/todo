package app.com.free.todo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbAdapter extends SQLiteOpenHelper {

    private static final String TAG = "DbAdapter";
    public static final String dBNameCategory = "Category";
    public static final String dBNameLocale = "locale";
    public static final String dBNameTasks = "gstasks";
    public static final String dbNamePreferences = "gspreferences";
    private final String DATABASE_NAME;
    private final String DATABASE_TABLE;
    private final int DATABASE_VERSION;
    private final String DB_PATH;
    private final Context mCtx;
    public SQLiteDatabase mDb;

    public DbAdapter(Context ctx, String dbpath, String dbname, String dbtable, int version) {
        super(ctx, dbname, null, version);
        this.mCtx = ctx;
        this.DB_PATH = dbpath;
        this.DATABASE_NAME = dbname;
        this.DATABASE_TABLE = dbtable;
        this.DATABASE_VERSION = version;
    }

    private void defineDataBase(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Category (_id INTEGER PRIMARY KEY, Code TEXT, Description TEXT, Color TEXT, Filter TEXT, Seq NUMERIC, fortasks TEXT, fornotes TEXT)");
        db.execSQL("CREATE TABLE gstasks (_id INTEGER PRIMARY KEY, type TEXT, category TEXT, priority TEXT, priority_code TEXT, status TEXT, title TEXT, detail TEXT, duedate TEXT, duetime text, startdate TEXT, starttime text, hidden TEXT, datetime_created TEXT, datetime_modified TEXT, tag TEXT)");
        db.execSQL("CREATE TABLE gsnotes (_id INTEGER PRIMARY KEY, title TEXT, detail TEXT, seq NUMERIC, category TEXT, datetime_created TEXT, datetime_modified TEXT)");
        db.execSQL("CREATE TABLE gspreferences (_id INTEGER PRIMARY KEY, date_separator TEXT, date_format TEXT, time_format TEXT, reminder TEXT, reminder_alarm TEXT, display_completed_tasks TEXT)");
        db.execSQL("CREATE UNIQUE INDEX Category_p on Category(_id ASC)");
        db.execSQL("CREATE INDEX Category_1 on Category(Code ASC)");
        db.execSQL("CREATE INDEX Category_2 on Category(Description ASC)");
        db.execSQL("CREATE UNIQUE INDEX gsnotes_p ON gsnotes(_id ASC)");
        db.execSQL("CREATE INDEX gsnotes_1 ON gsnotes(title ASC)");
        db.execSQL("CREATE INDEX gsnotes_2 ON gsnotes(detail ASC)");
        db.execSQL("CREATE INDEX gsnotes_3 ON gsnotes(datetime_modified ASC)");
        db.execSQL("CREATE INDEX gstasks_1 on gstasks(priority ASC, title ASC, detail ASC)");
        db.execSQL("CREATE INDEX gstasks_2 on gstasks(type ASC, duedate ASC, duetime ASC, startdate ASC, starttime ASC)");
        db.execSQL("CREATE INDEX gstasks_3 on gstasks(status ASC, title ASC, detail ASC)");
        db.execSQL("CREATE INDEX gstasks_4 on gstasks(priority_code ASC, title ASC, detail ASC)");
        db.execSQL("CREATE UNIQUE INDEX gstasks_p ON gstasks(_id ASC)");
        db.execSQL("INSERT INTO Category VALUES(1,'All Categories','All Categories',NULL,'Y',1,'Y','Y')");
        db.execSQL("INSERT INTO Category VALUES(2,'Personal','Personal',NULL,'N',2,'Y','Y')");
        db.execSQL("INSERT INTO Category VALUES(3,'Business','Business',NULL,'N',3,'Y','Y')");
        db.execSQL("INSERT INTO Category VALUES(4,'Travel','Travel',NULL,'N',4,'Y','Y')");
        db.execSQL("INSERT INTO Category VALUES(5,'House','House',NULL,'N',5,'Y','Y')");
        db.execSQL("INSERT INTO gspreferences VALUES(1, '/', 'M', 'S', 'N', null, 'Y')");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.w(TAG, "Database " + this.DATABASE_NAME + " does not exist.");
        defineDataBase(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL("alter table gstasks add priority_code TEXT");
            db.execSQL("update gstasks set priority_code = '0' where priority ='Low'");
            db.execSQL("update gstasks set priority_code = '1' where priority ='Medium'");
            db.execSQL("update gstasks set priority_code = '2' where priority ='High'");
        }
    }

    public DbAdapter open() {
        this.mDb = getWritableDatabase();
        return this;
    }

    public void close() {
        super.close();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = this.mCtx.getAssets().open(this.DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(this.DB_PATH + this.DATABASE_NAME);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = myInput.read(buffer);
            if (length <= 0) {
                myOutput.flush();
                myOutput.close();
                myInput.close();
                return;
            }
            myOutput.write(buffer, 0, length);
        }
    }



}
