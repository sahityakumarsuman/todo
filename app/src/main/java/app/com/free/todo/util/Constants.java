package app.com.free.todo.util;

public final class Constants {
    public static final int ADD_ID = 1;
    public static final String AUTHORITY = "com.crazelle.app.providers.tasks";
    public static final int CATEGORY_ID = 7;
    public static final String DATABASE_NAME = "crazelle.db";
    public static final int DATABASE_VERSION = 2;
    public static String DB_PATH = null;
    public static final int DELETE_ID = 3;
    public static final int DUE_DATE_DIALOG_ID = 10;
    public static final int DUE_TIME_DIALOG_ID = 12;
    public static final char DayFormat = 'D';
    public static final int Detail_Request_Code = 2000;
    public static final int EDIT_ID = 2;
    public static final int EXIT_ID = 6;
    public static final int HELP_ID = 4;
    public static final char MilitaryTime = 'M';
    public static final char MonthFormat = 'M';
    public static final int PERSONALIZATION_ID = 14;
    public static final int RETURN_ID = 13;
    public static final int SAVE_ID = 8;
    public static final int SEARCH_ID = 5;
    public static final int START_DATE_DIALOG_ID = 9;
    public static final int START_TIME_DIALOG_ID = 11;
    public static final char StandardTime = 'S';
    public static final int TTS_Code = 3000;
    public static final int Title_Request_Code = 1000;
    public static final char YearFormat = 'Y';
    public static final int uriTasks = 190;

    static {
        DB_PATH = "/data/data/com.gss.app.todo/databases/";
    }
}
