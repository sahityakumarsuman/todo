package app.com.free.todo.util;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import app.com.free.todo.ReceiveAlarm;
import app.com.free.todo.UserPreferenceValues;
import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;


public class OnDeviceBootComplete extends BroadcastReceiver {
    private AlarmManager am;
    private dateTime mDTTM;
    private String mReminderAlarm;
    private boolean mReminderEnabled;
    private dBTasks mTaskdb;
    private char mUserDateFormat;
    private char mUserDateSeparator;
    private char mUserTimeFormat;
    private SetReminder sr;
    private UserPreferenceValues up;

    public OnDeviceBootComplete() {
        this.mUserDateSeparator = '/';
        this.mUserDateFormat = dateTime.MonthFormat;
        this.mUserTimeFormat = dateTime.MonthFormat;
    }

    public void onReceive(Context context, Intent intent) {
        this.up = new UserPreferenceValues(context);
        this.mReminderEnabled = this.up.getReminder();
        this.mUserDateFormat = this.up.getUserDateFormat();
        this.mUserDateSeparator = this.up.getUserDateSeparator();
        this.mUserTimeFormat = this.up.getTimeFormat();
        this.mReminderAlarm = this.up.getReminderAlarm();
        this.mDTTM = new dateTime(this.mUserDateSeparator, this.mUserDateFormat, this.mUserTimeFormat);
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (this.mReminderEnabled) {
            this.mTaskdb = new dBTasks(context);
            this.mTaskdb.Open();
            this.mTaskdb.ClearFilterfields();
            this.mTaskdb.setDuedate(this.mDTTM.getSystemDate(Character.valueOf(dateTime.YearFormat)));
            this.mTaskdb.setDuetime(this.mDTTM.getSystemTime(Character.valueOf(' ')));
            this.mTaskdb.setStatus("N");
            Cursor cur = this.mTaskdb.FetchAllRows();
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                this.sr = new SetReminder(context.getApplicationContext(), ReceiveAlarm.class, this.am);
                this.sr.setmReminderEnabled(this.mReminderEnabled);
                this.sr.setmReminderAlarm(this.mReminderAlarm);
                this.sr.setmKey(dBUserPreferences.KEY_ROWID);
                this.sr.setmKeyValue(cur.getLong(cur.getColumnIndex(dBUserPreferences.KEY_ROWID)));
                this.sr.setmSubject(dBTasks.D_TITLE);
                this.sr.setmSubjectValue(cur.getString(cur.getColumnIndex(dBTasks.D_TITLE)));
                this.sr.setmDetail(dBTasks.D_DETAIL);
                this.sr.setmDetailValue(cur.getString(cur.getColumnIndex(dBTasks.D_DETAIL)));
                this.sr.setmDateFormat(dateTime.YearFormat);
                this.sr.setmDateSeparator(this.mUserDateSeparator);
                this.sr.setmAlarmDate(cur.getString(cur.getColumnIndex(dBTasks.D_DUEDATE)));
                this.sr.setmTimeFormat(dateTime.MonthFormat);
                this.sr.setmAlarmTime(cur.getString(cur.getColumnIndex(dBTasks.D_DUETIME)));
                if (this.mReminderEnabled) {
                    this.sr.Set();
                } else {
                    this.sr.Cancel();
                }
                cur.moveToNext();
            }
            cur.close();
        }
    }
}
