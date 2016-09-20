package app.com.free.todo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import app.com.free.todo.db.DbAdapter;
import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;
import app.com.free.todo.util.Constants;

public class TasksContentProvider extends ContentProvider {
    private static HashMap<String, String> mTasksProjectionMap;
    private static UriMatcher mUriMatcher;
    private Cursor mTaskCursor;
    private dBTasks mTaskdb;

    public boolean onCreate() {
        this.mTaskdb = new dBTasks(getContext());
        this.mTaskdb.Open();
        mUriMatcher = new UriMatcher(-1);
        mUriMatcher.addURI(Constants.AUTHORITY, DbAdapter.dBNameTasks, Constants.uriTasks);
        mTasksProjectionMap = new HashMap();
        mTasksProjectionMap.put(dBUserPreferences.KEY_ROWID, dBUserPreferences.KEY_ROWID);
        mTasksProjectionMap.put(dBTasks.D_TITLE, dBTasks.D_TITLE);
        mTasksProjectionMap.put(dBTasks.D_DETAIL, dBTasks.D_DETAIL);
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (mUriMatcher.match(uri)) {
            case Constants.uriTasks /*190*/:
                qb.setTables(DbAdapter.dBNameTasks);
                qb.setProjectionMap(mTasksProjectionMap);
                this.mTaskCursor = qb.query(this.mTaskdb.mTasksDb, projection, selection, selectionArgs, null, null, sortOrder);
                this.mTaskCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return this.mTaskCursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case Constants.uriTasks /*190*/:
                return null;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
