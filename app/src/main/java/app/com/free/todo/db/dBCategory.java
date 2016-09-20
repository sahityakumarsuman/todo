package app.com.free.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.com.free.todo.util.Constants;


public class dBCategory {
    private static final String DATABASE_TABLE = "Category";
    public static final String D_CODE = "Code";
    public static final String D_COLOR = "Color";
    public static final String D_DESCRIPTION = "Description";
    public static final String D_FILTER = "Filter";
    public static final String D_FORNOTES = "fornotes";
    public static final String D_FORTASKS = "fortasks";
    public static final String D_SEQ = "Seq";
    public static final String KEY_ROWID = "_id";
    private String[] fieldlist;
    private SQLiteDatabase mCategoryDb;
    public Cursor mCategoryRows;
    private DbAdapter mDbAdapter;
    private Context mParentActivity;

    public dBCategory(Context ctx) {
        this.fieldlist = new String[]{KEY_ROWID, D_CODE, D_DESCRIPTION, D_FILTER, D_COLOR, D_SEQ, D_FORTASKS, D_FORNOTES};
        this.mParentActivity = ctx;
    }

    public boolean Open() {
        this.mDbAdapter = new DbAdapter(this.mParentActivity, Constants.DB_PATH, Constants.DATABASE_NAME, DATABASE_TABLE, 2);
        this.mDbAdapter.open();
        this.mCategoryDb = this.mDbAdapter.mDb;
        return true;
    }

    public boolean Close() {
        this.mDbAdapter.close();
        return true;
    }

    public Cursor FetchAllRows(boolean pforFilter, String pOrderby) {
        String where;
        if (pforFilter) {
            where = null;
        } else {
            where = "Filter='N'";
        }
        this.mCategoryRows = this.mCategoryDb.query(DATABASE_TABLE, this.fieldlist, where, null, null, null, pOrderby);
        return this.mCategoryRows;
    }

    public Cursor FetchRow(long rowId) {
        Cursor cur = this.mCategoryDb.query(true, DATABASE_TABLE, this.fieldlist, "_id=" + rowId, null, null, null, null, null);
        if (cur != null) {
            cur.moveToFirst();
        }
        return cur;
    }

    public long create(String categoryDescr) {
        return create(categoryDescr, String.valueOf(Integer.parseInt(getMaxSeq()) + 10));
    }

    public long create(String categoryDescr, String categoryseq) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(D_CODE, categoryDescr);
        initialValues.put(D_DESCRIPTION, categoryDescr);
        initialValues.put(D_FILTER, "N");
        initialValues.put(D_COLOR, "");
        initialValues.put(D_SEQ, categoryseq);
        initialValues.put(D_FORTASKS, "Y");
        initialValues.put(D_FORNOTES, "Y");
        return this.mCategoryDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean update(long rowId, String categoryDescr) {
        return update(rowId, categoryDescr, "NA");
    }

    public boolean update(long rowId, String categoryDescr, String categoryseq) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(D_CODE, categoryDescr);
        updateValues.put(D_DESCRIPTION, categoryDescr);
        if (!categoryseq.equals("NA")) {
            updateValues.put(D_SEQ, categoryseq);
        }
        return this.mCategoryDb.update(DATABASE_TABLE, updateValues, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public boolean delete(long rowId) {
        return this.mCategoryDb.delete(DATABASE_TABLE, new StringBuilder("_id=").append(rowId).toString(), null) > 0;
    }

    public String getMaxSeq() {
        String maxseq = null;
        Cursor cur = FetchAllRows(true, null);
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            maxseq = cur.getString(cur.getColumnIndex(D_SEQ));
            if (Integer.parseInt(cur.getString(cur.getColumnIndex(D_SEQ))) > Integer.parseInt(maxseq)) {
                maxseq = cur.getString(cur.getColumnIndex(D_SEQ));
            }
            cur.moveToNext();
        }
        return maxseq;
    }

    public void execSQL(String sql) {
        this.mCategoryDb.execSQL(sql);
    }

    public void updateTaskTable(String originalCategoryValue, String newCategoryValue) {
        execSQL("update ".concat(DbAdapter.dBNameTasks).concat(" set category = '").concat(newCategoryValue).concat("' where category = '").concat(originalCategoryValue).concat("'"));
    }

    public boolean checkTaskTable(String categorycode) {
        if (this.mCategoryDb.query(DbAdapter.dBNameTasks, new String[]{dBTasks.D_TITLE, dBTasks.D_CATEGORY}, dBTasks.D_CATEGORY.concat(" = '").concat(categorycode).concat("'"), null, null, null, null).getCount() > 0) {
            return true;
        }
        return false;
    }
}
