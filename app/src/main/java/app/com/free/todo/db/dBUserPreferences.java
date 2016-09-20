package app.com.free.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.com.free.todo.util.Constants;


public class dBUserPreferences {
    private static final String DATABASE_TABLE = "gspreferences";
    public static final String DATE_FORMAT = "date_format";
    public static final String DATE_SEPARATOR = "date_separator";
    public static final String DISPLAY_COMPLETED = "display_completed_tasks";
    public static final String KEY_ROWID = "_id";
    public static final String REMINDER = "reminder";
    public static final String REMINDER_ALARM = "reminder_alarm";
    public static final String TIME_FORMAT = "time_format";
    private char DisplayCompletedTasks;
    private char Reminder;
    private char ReminderAlarm;
    private char TimeFormat;
    private char UserDateFormat;
    private char UserDateSeparator;
    private String[] fieldlist;
    private DbAdapter mDbAdapter;
    private Context mParentActivity;
    private SQLiteDatabase mPreferencesDb;

    public dBUserPreferences(Context ctx) {
        this.fieldlist = new String[]{KEY_ROWID, DATE_SEPARATOR, DATE_FORMAT, TIME_FORMAT, REMINDER, REMINDER_ALARM, DISPLAY_COMPLETED};
        this.mParentActivity = ctx;
        Open();
        Cursor cur = FetchRow(1);
        this.UserDateSeparator = cur.getString(cur.getColumnIndex(DATE_FORMAT)).charAt(0);
        this.UserDateFormat = cur.getString(cur.getColumnIndex(DATE_SEPARATOR)).charAt(0);
        this.TimeFormat = cur.getString(cur.getColumnIndex(TIME_FORMAT)).charAt(0);
        this.Reminder = cur.getString(cur.getColumnIndex(REMINDER)).charAt(0);
        this.ReminderAlarm = cur.getString(cur.getColumnIndex(REMINDER_ALARM)).charAt(0);
        this.DisplayCompletedTasks = cur.getString(cur.getColumnIndex(DISPLAY_COMPLETED)).charAt(0);
    }

    public boolean Open() {
        this.mDbAdapter = new DbAdapter(this.mParentActivity, Constants.DB_PATH, Constants.DATABASE_NAME, DATABASE_TABLE, 2);
        this.mDbAdapter.open();
        this.mPreferencesDb = this.mDbAdapter.mDb;
        return true;
    }

    public boolean Close() {
        this.mDbAdapter.close();
        return true;
    }

    public Cursor FetchRow(long rowId) {
        Cursor cur = this.mPreferencesDb.query(true, DATABASE_TABLE, this.fieldlist, "_id=1", null, null, null, null, null);
        if (cur != null) {
            cur.moveToFirst();
        }
        return cur;
    }

    private boolean updatePreferences(String field, char fieldValue) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(field, Character.toString(fieldValue));
        return this.mPreferencesDb.update(DATABASE_TABLE, updateValues, "_id= 1", null) > 0;
    }

    public char getUserDateSeparator() {
        return this.UserDateSeparator;
    }

    public void setUserDateSeparator(char userDateSeparator) {
        updatePreferences(DATE_SEPARATOR, userDateSeparator);
        this.UserDateSeparator = userDateSeparator;
    }

    public char getUserDateFormat() {
        return this.UserDateFormat;
    }

    public void setUserDateFormat(char userDateFormat) {
        updatePreferences(DATE_FORMAT, userDateFormat);
        this.UserDateFormat = userDateFormat;
    }

    public char getTimeFormat() {
        return this.TimeFormat;
    }

    public void setTimeFormat(char timeFormat) {
        updatePreferences(TIME_FORMAT, timeFormat);
        this.TimeFormat = timeFormat;
    }

    public char getReminder() {
        return this.Reminder;
    }

    public void setReminder(char reminder) {
        updatePreferences(REMINDER, reminder);
        this.Reminder = reminder;
    }

    public char getReminderAlarm() {
        return this.ReminderAlarm;
    }

    public void setReminderAlarm(char reminderAlarm) {
        updatePreferences(REMINDER_ALARM, reminderAlarm);
        this.ReminderAlarm = reminderAlarm;
    }

    public char getDisplayCompletedTasks() {
        return this.DisplayCompletedTasks;
    }

    public void setDisplayCompletedTasks(char displayCompletedTasks) {
        updatePreferences(DISPLAY_COMPLETED, displayCompletedTasks);
        this.DisplayCompletedTasks = displayCompletedTasks;
    }
}
