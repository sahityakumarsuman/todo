package app.com.free.todo.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.Date;

public class SetReminder {
    private static final String ALARM = "ALARM";
    private String mAlarmDate;
    private AlarmManager mAlarmManager;
    private String mAlarmTime;
    private Class mClass;
    private Context mContext;
    private dateTime mDTTM;
    private char mDateFormat;
    private char mDateSeparator;
    private int mDay;
    private String mDetail;
    private String mDetailValue;
    private int mHour;
    private Intent mIntent;
    private String mKey;
    private Long mKeyValue;
    private int mMinute;
    private int mMonth;
    private PendingIntent mPendingIntent;
    private String mReminderAlarm;
    private Date mReminderDate;
    private boolean mReminderEnabled;
    private String mSubject;
    private String mSubjectValue;
    private char mTimeFormat;
    private int mYear;

    public SetReminder(Context context, Class receiverclass, AlarmManager alarmmanager) {
        this.mContext = context;
        this.mClass = receiverclass;
        this.mAlarmManager = alarmmanager;
        this.mIntent = new Intent(this.mContext, this.mClass);
    }

    public boolean Set() {
        if (!this.mReminderEnabled) {
            return false;
        }
        CreateObject();
        this.mAlarmManager.set(0, this.mReminderDate.getTime(), this.mPendingIntent);
        return true;
    }

    public boolean Cancel() {
        CreateObject();
        this.mAlarmManager.cancel(this.mPendingIntent);
        return true;
    }

    private void CreateObject() {
        this.mDTTM = new dateTime(this.mDateSeparator, this.mDateFormat, this.mTimeFormat);
        this.mIntent.putExtra(ALARM, Uri.parse(this.mReminderAlarm));
        this.mIntent.putExtra(this.mKey, this.mKeyValue);
        this.mIntent.putExtra(this.mSubject, this.mSubjectValue);
        this.mIntent.putExtra(this.mDetail, this.mDetailValue);
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, this.mKeyValue.intValue(), this.mIntent, Intent.FILL_IN_ACTION);
        if (this.mAlarmDate != null) {
            this.mYear = this.mDTTM.getYear(this.mDateFormat, this.mAlarmDate);
            this.mMonth = this.mDTTM.getMonth(this.mDateFormat, this.mAlarmDate);
            this.mDay = this.mDTTM.getDay(this.mDateFormat, this.mAlarmDate);
            if (this.mAlarmTime != null) {
                this.mHour = this.mDTTM.getHour(this.mTimeFormat, this.mAlarmTime);
                this.mMinute = this.mDTTM.getMinute(this.mTimeFormat, this.mAlarmTime);
            } else {
                this.mHour = 0;
                this.mMinute = 0;
            }
        }
        this.mReminderDate = null;
        this.mReminderDate = new Date(this.mYear - 1900, this.mMonth - 1, this.mDay, this.mHour, this.mMinute);
    }

    public void setmReminderEnabled(boolean mReminderEnabled) {
        this.mReminderEnabled = mReminderEnabled;
    }

    public void setmReminderAlarm(String mReminderAlarm) {
        this.mReminderAlarm = mReminderAlarm;
    }

    public void setmAlarmDate(String mAlarmDate) {
        this.mAlarmDate = mAlarmDate;
    }

    public void setmDateFormat(char mDateFormat) {
        this.mDateFormat = mDateFormat;
    }

    public void setmDateSeparator(char mDateSeparator) {
        this.mDateSeparator = mDateSeparator;
    }

    public void setmAlarmTime(String mAlarmTime) {
        this.mAlarmTime = mAlarmTime;
    }

    public void setmTimeFormat(char mTimeFormat) {
        this.mTimeFormat = mTimeFormat;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public void setmKeyValue(long mKeyValue) {
        this.mKeyValue = Long.valueOf(mKeyValue);
    }

    public void setmDetail(String mDetail) {
        this.mDetail = mDetail;
    }

    public void setmDetailValue(String mDetailValue) {
        this.mDetailValue = mDetailValue;
    }

    public void setmSubject(String mSubject) {
        this.mSubject = mSubject;
    }

    public void setmSubjectValue(String mSubjectValue) {
        this.mSubjectValue = mSubjectValue;
    }
}
