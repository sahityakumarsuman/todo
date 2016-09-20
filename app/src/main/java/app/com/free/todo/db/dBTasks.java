package app.com.free.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.com.free.todo.util.Constants;
import app.com.free.todo.util.dateTime;

public class dBTasks {
    private static final String DATABASE_TABLE = "gstasks";
    public static final String D_CATEGORY = "category";
    public static final String D_CREATED = "datetime_created";
    public static final String D_DETAIL = "detail";
    public static final String D_DUEDATE = "duedate";
    public static final String D_DUETIME = "duetime";
    public static final String D_HIDDEN = "hidden";
    public static final String D_MODIFIED = "datetime_modified";
    public static final String D_PRIORITY = "priority";
    public static final String D_PRIORITY_CODE = "priority_code";
    public static final String D_STARTDATE = "startdate";
    public static final String D_STARTTIME = "starttime";
    public static final String D_STATUS = "status";
    public static final String D_TITLE = "title";
    public static final String D_TYPE = "type";
    public static final String KEY_ROWID = "_id";
    public static final String TASK_TAG = "tag";
    private String _id;
    private String category;
    private String datetime_created;
    private String datetime_modified;
    private String detail;
    private String duedate;
    private String duetime;
    private String[] fieldlist;
    private String hidden;
    private Cursor mCategoryCursor;
    private dBCategory mCategorydb;
    private DbAdapter mDbAdapter;
    private boolean mDisplayCompletedTasks;
    private Context mParentActivity;
    public SQLiteDatabase mTasksDb;
    public Cursor mTasksRows;
    private ContentValues mValues;
    private String priority;
    private String priority_code;
    private String startdate;
    private String starttime;
    private String status;
    private String title;
    private String type;
    private String notTage;

    public dBTasks(Context ctx) {
        this.fieldlist = new String[]{KEY_ROWID, D_TYPE, D_CATEGORY, D_PRIORITY, D_PRIORITY_CODE, D_STATUS, D_TITLE, D_DETAIL, D_DUEDATE, D_DUETIME, D_STARTDATE, D_STARTTIME, D_HIDDEN, D_CREATED, D_MODIFIED, TASK_TAG};
        this.mParentActivity = ctx;
        ResetUpdateBuffer();
    }

    public boolean Open() {
        this.mDbAdapter = new DbAdapter(this.mParentActivity, Constants.DB_PATH, Constants.DATABASE_NAME, DATABASE_TABLE, 2);
        this.mDbAdapter.open();
        this.mTasksDb = this.mDbAdapter.mDb;
        this.mCategorydb = new dBCategory(this.mParentActivity);
        this.mCategorydb.Open();
        return true;
    }

    public boolean Close() {
        this.mDbAdapter.close();
        this.mCategorydb.Close();
        return true;
    }

    public Cursor FetchAllRows() {
        String whereStatus;
        String str;
        String whereDueDate = null;
        String whereDueTime = null;
        if (this.status != null) {
            whereStatus = " (status = '" + this.status + "')";
        } else {
            whereStatus = "";
        }
        if (this.duedate != null) {
            whereDueDate = "duedate > '" + this.duedate + "' ";
            if (this.duetime != null) {
                whereDueTime = " (duedate = '" + this.duedate + "' and " + D_DUETIME + " >= '" + this.duetime + "' and " + D_DUETIME + " != null " + " ) ";
            }
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(whereStatus));
        if (whereDueDate == null) {
            str = "";
        } else {
            StringBuilder append = new StringBuilder(" and (").append(whereDueDate);
            if (whereDueTime == null) {
                str = "";
            } else {
                str = " or (" + whereDueTime + ")";
            }
            str = append.append(str).append(")").toString();
        }
        this.mTasksRows = this.mTasksDb.query(DATABASE_TABLE, this.fieldlist, stringBuilder.append(str).toString(), null, null, null, null);
        return this.mTasksRows;
    }

    public Cursor FetchAllRows(String pFilter, String pOrderby) {
        String where;
        String orderby = pOrderby;
        if (this.mDisplayCompletedTasks) {
            where = null;
        } else {
            where = "status = 'N' ";
        }
        if (!(pFilter == null || pFilter.equals("1"))) {
            this.mCategoryCursor = this.mCategorydb.FetchRow(Long.valueOf(pFilter).longValue());
            String filter = this.mCategoryCursor.getString(this.mCategoryCursor.getColumnIndex(dBCategory.D_CODE)).replace("'", "''");
            if (where != null) {
                where = new StringBuilder(String.valueOf(where)).append(" and ").append(D_CATEGORY).append(" = '").append(filter).append("'").toString();
            } else {
                where = "category = '" + filter + "'";
            }
        }
        this.mTasksRows = this.mTasksDb.query(DATABASE_TABLE, this.fieldlist, where, null, null, null, orderby);
        return this.mTasksRows;
    }

    public Cursor FetchRow(long rowId) {
        Cursor cur = this.mTasksDb.query(true, DATABASE_TABLE, this.fieldlist, "_id=" + rowId, null, null, null, null, null);
        if (cur != null) {
            cur.moveToFirst();
        }
        return cur;
    }

    public Cursor searchTasks(String searchString) {
        String criteriaTemp = searchString.replace("'", "_");
        return this.mTasksDb.query(DATABASE_TABLE, this.fieldlist, "title like '%" + criteriaTemp + "%' or " + D_DETAIL + " like '%" + criteriaTemp + "%' or " + D_STATUS + " like '%" + criteriaTemp + "%' or " + D_CATEGORY + " like '%" + criteriaTemp + "%'", null, null, null, null);
    }

    public boolean delete(long rowId) {
        return this.mTasksDb.delete(DATABASE_TABLE, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public long create() {
        this.mValues.put(D_CREATED, new dateTime().getSystemDateTime(Character.valueOf(dateTime.YearFormat), Character.valueOf(dateTime.MonthFormat)));
        return this.mTasksDb.insert(DATABASE_TABLE, null, this.mValues);
    }

    public boolean update() {
        this.mValues.put(D_MODIFIED, new dateTime().getSystemDateTime(Character.valueOf(dateTime.YearFormat), Character.valueOf(dateTime.MonthFormat)));
        return this.mTasksDb.update(DATABASE_TABLE, this.mValues, new StringBuilder("_id=").append(get_id()).toString(), null) > 0;
    }

    public boolean updateStatus(long rowId, String status) {
        ContentValues values = new ContentValues();
        values.put(D_STATUS, status);
        values.put(D_MODIFIED, new dateTime().getSystemDateTime(Character.valueOf(dateTime.YearFormat), Character.valueOf(dateTime.MonthFormat)));
        return this.mTasksDb.update(DATABASE_TABLE, values, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public void ResetUpdateBuffer() {
        this.mValues = new ContentValues();
        ClearFilterfields();
    }

    public void setmDisplayCompletedTasks(boolean mDisplayCompletedTasks) {
        this.mDisplayCompletedTasks = mDisplayCompletedTasks;
    }

    public void ClearFilterfields() {
        this.status = null;
        this.duedate = null;
        this.duetime = null;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
        this.mValues.put(KEY_ROWID, _id);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.mValues.put(D_STATUS, status);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
        this.mValues.put(D_TYPE, type);
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
        this.mValues.put(D_CATEGORY, category);
    }

    public String getPriorityCode() {
        return this.priority_code;
    }

    public void setPriorityCode(String priority_code) {
        this.priority_code = priority_code;
        this.mValues.put(D_PRIORITY_CODE, priority_code);
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
        this.mValues.put(D_PRIORITY, priority);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.mValues.put(D_TITLE, title);
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        this.mValues.put(D_DETAIL, detail);
    }

    public String getStartdate() {
        return this.startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
        this.mValues.put(D_STARTDATE, startdate);
    }

    public String getNoteTage() {
        return this.notTage;
    }

    public void setTag(String _noteTage) {
        this.notTage = _noteTage;
        this.mValues.put(TASK_TAG, _noteTage);

    }

    public String getStarttime() {
        return this.starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
        this.mValues.put(D_STARTTIME, starttime);
    }

    public String getHidden() {
        return this.hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
        this.mValues.put(D_HIDDEN, hidden);
    }

    public String getDatetime_created() {
        return this.datetime_created;
    }

    public void setDatetime_created(String datetime_created) {
        this.datetime_created = datetime_created;
        this.mValues.put(D_CREATED, datetime_created);
    }

    public String getDatetime_modified() {
        return this.datetime_modified;
    }

    public void setDatetime_modified(String datetime_modified) {
        this.datetime_modified = datetime_modified;
        this.mValues.put(D_MODIFIED, datetime_modified);
    }

    public String getDuedate() {
        return this.duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
        this.mValues.put(D_DUEDATE, duedate);
    }

    public String getDuetime() {
        return this.duetime;
    }

    public void setDuetime(String duetime) {
        this.duetime = duetime;
        this.mValues.put(D_DUETIME, duetime);
    }


    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = mDbAdapter.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
