package app.com.free.todo.util;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class categorySimpleCursorAdapter extends SimpleCursorAdapter {
    public Context mContext;
    public Cursor mCursor;
    public String[] mFromDbField;
    public int mLayout;
    public int[] mToLayoutId;

    public categorySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mContext = context;
        this.mLayout = layout;
        this.mCursor = c;
        this.mFromDbField = from;
        this.mToLayoutId = to;
    }

    public int getRowPos(String fieldName, String promptValue) {
        int colIndex = this.mCursor.getColumnIndex(fieldName);
        this.mCursor.moveToFirst();
        while (!this.mCursor.isAfterLast()) {
            if (this.mCursor.getString(colIndex).equals(promptValue)) {
                return this.mCursor.getPosition();
            }
            this.mCursor.moveToNext();
        }
        return 0;
    }
}
