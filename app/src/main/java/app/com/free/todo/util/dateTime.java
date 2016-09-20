package app.com.free.todo.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.com.free.todo.db.dBUserPreferences;

public class dateTime {
    public static final String AM = "AM";
    public static final char DayFormat = 'D';
    public static final char MilitaryTime = 'M';
    public static final char MonthFormat = 'M';
    public static final String PM = "PM";
    public static final char StandardTime = 'S';
    public static final char YearFormat = 'Y';
    public final Calendar cal;
    private int mAMposition;
    private int mDateLength;
    public String mDeviceDateFormat;
    public String mDeviceDateSeparator;
    public String mDeviceTimeFormat;
    private int mFirstDelimiter;
    private int mPMposition;
    private int mSecondDelimiter;
    private int mTimeLength;

    public dateTime() {
        this.cal = Calendar.getInstance();
        this.mDeviceDateFormat = dBUserPreferences.DATE_FORMAT;
        this.mDeviceTimeFormat = "time_12_24";
        this.mDeviceDateSeparator = "/";
    }

    public dateTime(char dateseparator, char dateformat, char timeformat) {
        this.cal = Calendar.getInstance();
        this.mDeviceDateFormat = Character.toString(dateformat);
        this.mDeviceTimeFormat = Character.toString(timeformat);
        this.mDeviceDateSeparator = Character.toString(dateseparator);
    }

    public String getSystemDateTime(Character pDateFormat, Character pTimeFormat) {
        return getSystemDate(pDateFormat) + " " + getSystemTime(pTimeFormat);
    }

    public String getSystemDate(Character pDateFormat) {
        String dtFormat;
        switch (pDateFormat.charValue()) {
            case 'D':
                dtFormat = "dd/MM/yyyy";
                break;
            case 'M':
                dtFormat = "MM/dd/yyyy";
                break;
            case 'Y':
                dtFormat = "yyyy/MM/dd";
                break;
            default:
                dtFormat = "yyyy/MM/dd";
                break;
        }
        return new SimpleDateFormat(dtFormat).format(new Date());
    }

    public String getSystemTime(Character pTimeFormat) {
        String tmFormat;
        switch (pTimeFormat.charValue()) {
            case 'M':
                tmFormat = "HH:mm:ss z";
                break;
            case 'S':
                tmFormat = "hh:mm:ss a,z";
                break;
            default:
                tmFormat = "HH:mm:ss";
                break;
        }
        return new SimpleDateFormat(tmFormat).format(new Date());
    }

    public String convertDate(String pdate, Character fromFormat, Character toFormat) {
        String retDate = pdate;
        this.mDateLength = 0;
        this.mFirstDelimiter = 0;
        this.mSecondDelimiter = 0;
        if (pdate != null) {
            setDateDelimiters(pdate);
        }
        if (this.mDateLength == 0) {
            return retDate;
        }
        String dd;
        String mm;
        String yy;
        switch (fromFormat.charValue()) {
            case 'D':
                dd = pdate.substring(0, this.mFirstDelimiter).trim();
                mm = pdate.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim();
                yy = pdate.substring(this.mSecondDelimiter + 1, this.mDateLength).trim();
                switch (toFormat.charValue()) {
                    case 'M':
                        return new StringBuilder(String.valueOf(mm)).append(this.mDeviceDateSeparator).append(dd).append(this.mDeviceDateSeparator).append(yy).toString();
                    case 'Y':
                        return new StringBuilder(String.valueOf(yy)).append(this.mDeviceDateSeparator).append(mm).append(this.mDeviceDateSeparator).append(dd).toString();
                    default:
                        return retDate;
                }
            case 'M':
                mm = pdate.substring(0, this.mFirstDelimiter).trim();
                dd = pdate.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim();
                yy = pdate.substring(this.mSecondDelimiter + 1, this.mDateLength).trim();
                switch (toFormat.charValue()) {
                    case 'D':
                        return new StringBuilder(String.valueOf(dd)).append(this.mDeviceDateSeparator).append(mm).append(this.mDeviceDateSeparator).append(yy).toString();
                    case 'Y':
                        return new StringBuilder(String.valueOf(yy)).append(this.mDeviceDateSeparator).append(mm).append(this.mDeviceDateSeparator).append(dd).toString();
                    default:
                        return retDate;
                }
            case 'Y':
                yy = pdate.substring(0, this.mFirstDelimiter).trim();
                mm = pdate.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim();
                dd = pdate.substring(this.mSecondDelimiter + 1, this.mDateLength).trim();
                switch (toFormat.charValue()) {
                    case 'D':
                        return new StringBuilder(String.valueOf(dd)).append(this.mDeviceDateSeparator).append(mm).append(this.mDeviceDateSeparator).append(yy).toString();
                    case 'M':
                        return new StringBuilder(String.valueOf(mm)).append(this.mDeviceDateSeparator).append(dd).append(this.mDeviceDateSeparator).append(yy).toString();
                    default:
                        return retDate;
                }
            default:
                return pdate.substring(0, 10);
        }
    }

    public String convertTime(String ptime, Character fromFormat, Character toFormat) {
        this.mFirstDelimiter = 0;
        this.mAMposition = 0;
        this.mPMposition = 0;
        if (ptime != null) {
            setTimeDelimiters(ptime);
        }
        if (fromFormat == toFormat) {
            return ptime;
        }
        if (ptime == null) {
            return null;
        }
        String hh = ptime.substring(0, this.mFirstDelimiter).trim();
        String newhh;
        switch (fromFormat.charValue()) {
            case 'M':
                if (Integer.parseInt(hh) < 12) {
                    return hh.trim() + ":" + ptime.substring(this.mFirstDelimiter + 1, ptime.length()).trim() + " " + AM;
                }
                newhh = hh;
                if (Integer.parseInt(hh) > 12) {
                    newhh = Integer.toString(Integer.parseInt(hh) - 12).trim();
                }
                if (newhh.length() == 1) {
                    newhh = "0".concat(newhh);
                }
                return new StringBuilder(String.valueOf(newhh)).append(":").append(ptime.substring(this.mFirstDelimiter + 1, ptime.length()).trim()).append(" ").append(PM).toString();
            case 'S':
                if (this.mPMposition == -1) {
                    return hh.trim() + ":" + ptime.substring(this.mFirstDelimiter + 1, this.mAMposition).trim();
                }
                newhh = hh.trim();
                if (Integer.parseInt(hh) != 12) {
                    newhh = Integer.toString(Integer.parseInt(hh) + 12).trim();
                }
                return new StringBuilder(String.valueOf(newhh)).append(":").append(ptime.substring(this.mFirstDelimiter + 1, this.mPMposition).trim()).toString();
            default:
                return null;
        }
    }

    public int getYear(char format, String date) {
        setDateDelimiters(date);
        switch (format) {
            case 'D':
            case 'M':
                return Integer.parseInt(date.substring(this.mSecondDelimiter + 1, this.mDateLength).trim());
            case 'Y':
                return Integer.parseInt(date.substring(0, this.mFirstDelimiter).trim());
            default:
                return 0;
        }
    }

    public int getMonth(char format, String date) {
        setDateDelimiters(date);
        switch (format) {
            case 'D':
                return Integer.parseInt(date.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim());
            case 'M':
                return Integer.parseInt(date.substring(0, this.mFirstDelimiter).trim());
            case 'Y':
                return Integer.parseInt(date.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim());
            default:
                return 0;
        }
    }

    public int getDay(char format, String date) {
        setDateDelimiters(date);
        switch (format) {
            case 'D':
                return Integer.parseInt(date.substring(0, this.mFirstDelimiter).trim());
            case 'M':
                return Integer.parseInt(date.substring(this.mFirstDelimiter + 1, this.mSecondDelimiter).trim());
            case 'Y':
                return Integer.parseInt(date.substring(this.mSecondDelimiter + 1, this.mDateLength).trim());
            default:
                return 0;
        }
    }

    public int getHour(char format, String time) {
        setTimeDelimiters(time);
        int hh = Integer.parseInt(time.substring(0, this.mFirstDelimiter).trim());
        if (format != StandardTime || this.mPMposition == -1 || hh == 12) {
            return hh;
        }
        return hh + 12;
    }

    public int getHour(String time) {
        setTimeDelimiters(time);
        return Integer.parseInt(time.substring(0, this.mFirstDelimiter).trim());
    }

    public int getMinute(char format, String time) {
        setTimeDelimiters(time);
        if (format == StandardTime) {
            return Integer.parseInt(time.substring(this.mFirstDelimiter + 1, this.mFirstDelimiter + 3).trim());
        }
        return Integer.parseInt(time.substring(this.mFirstDelimiter + 1, this.mTimeLength).trim());
    }

    private void setTimeDelimiters(String time) {
        this.mTimeLength = time.length();
        this.mFirstDelimiter = time.indexOf(":", 0);
        this.mAMposition = time.indexOf(AM, 0);
        this.mPMposition = time.indexOf(PM, 0);
    }

    private void setDateDelimiters(String date) {
        this.mDateLength = date.length();
        this.mFirstDelimiter = date.indexOf(this.mDeviceDateSeparator, 0);
        this.mSecondDelimiter = date.indexOf(this.mDeviceDateSeparator, this.mFirstDelimiter + 1);
        if (this.mFirstDelimiter == -1) {
            this.mFirstDelimiter = date.indexOf("-", 0);
            this.mSecondDelimiter = date.indexOf("-", this.mFirstDelimiter + 1);
        }
        if (this.mFirstDelimiter == -1) {
            this.mFirstDelimiter = date.indexOf("/", 0);
            this.mSecondDelimiter = date.indexOf("/", this.mFirstDelimiter + 1);
        }
        if (this.mFirstDelimiter == -1) {
            this.mFirstDelimiter = date.indexOf(".", 0);
            this.mSecondDelimiter = date.indexOf(".", this.mFirstDelimiter + 1);
        }
    }
}
