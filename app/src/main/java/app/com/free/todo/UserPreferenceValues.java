package app.com.free.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferenceValues {
    private boolean DisplayCompletedTasks;
    private boolean Reminder;
    private String ReminderAlarm;
    private char TimeFormat;
    private char UserDateFormat;
    private char UserDateSeparator;

    public char getUserDateSeparator() {
        return this.UserDateSeparator;
    }

    public char getUserDateFormat() {
        return this.UserDateFormat;
    }

    public char getTimeFormat() {
        return this.TimeFormat;
    }

    public boolean getReminder() {
        return this.Reminder;
    }

    public String getReminderAlarm() {
        return this.ReminderAlarm;
    }

    public boolean getDisplayCompletedTasks() {
        return this.DisplayCompletedTasks;
    }

    public UserPreferenceValues(Context ctx) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.UserDateSeparator = sharedPrefs.getString("dateseparator", "/").charAt(0);
        this.UserDateFormat = sharedPrefs.getString("dateformat", "M").charAt(0);
        this.TimeFormat = sharedPrefs.getString("timeformat", "M").charAt(0);
        this.Reminder = sharedPrefs.getBoolean("enablereminder", false);
        this.ReminderAlarm = sharedPrefs.getString("reminderalarm", "Default ringtone");
        this.DisplayCompletedTasks = sharedPrefs.getBoolean("displaycompletedtasks", true);
    }
}
