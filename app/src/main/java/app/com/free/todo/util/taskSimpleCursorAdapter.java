package app.com.free.todo.util;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import app.com.free.todo.R;
import app.com.free.todo.UserPreferenceValues;
import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;


public class taskSimpleCursorAdapter extends SimpleCursorAdapter {
    public Context mContext;
    public Cursor mCursor;
    public String[] mFromDbField;
    public int mLayout;
    private dBTasks mTaskdb;
    public int[] mToLayoutId;
    private char mUserDateFormat;
    private char mUserDateSeparator;
    private char mUserTimeFormat;

    /* renamed from: com.crazelle.util.taskSimpleCursorAdapter.1 */
    class C00171 implements OnClickListener {
        private final /* synthetic */ Cursor val$cr;
        private final /* synthetic */ int val$rowid;
        private final /* synthetic */ String val$s;

        C00171(int i, String str, Cursor cursor) {
            this.val$rowid = i;
            this.val$s = str;
            this.val$cr = cursor;
        }

        public void onClick(View v) {
            taskSimpleCursorAdapter.this.mTaskdb = new dBTasks(taskSimpleCursorAdapter.this.mContext);
            taskSimpleCursorAdapter.this.mTaskdb.Open();
            taskSimpleCursorAdapter.this.mTaskdb.set_id(Integer.toString(this.val$rowid));
            if (this.val$s.equals("Y")) {
                ((Button) v).setBackgroundResource(R.drawable.checkbox_off);
                taskSimpleCursorAdapter.this.mTaskdb.setStatus("N");
            } else {
                ((Button) v).setBackgroundResource(R.drawable.checkbox_on);
                taskSimpleCursorAdapter.this.mTaskdb.setStatus("Y");
            }
            taskSimpleCursorAdapter.this.mTaskdb.update();
            taskSimpleCursorAdapter.this.mTaskdb.Close();
            this.val$cr.requery();
        }
    }

    public taskSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mContext = context;
        this.mLayout = layout;
        this.mCursor = c;
        this.mFromDbField = from;
        this.mToLayoutId = to;
        this.mUserDateFormat = dateTime.MonthFormat;
    }

    public void bindView(View v, Context context, Cursor c) {
        super.bindView(v, context, c);
        this.mUserDateFormat = new UserPreferenceValues(context).getUserDateFormat();
        this.mUserDateSeparator = new UserPreferenceValues(context).getUserDateSeparator();
        this.mUserTimeFormat = new UserPreferenceValues(context).getTimeFormat();
        TextView vTask = (TextView) v.findViewById(R.id.task_row_edit);
        TextView vField1 = (TextView) v.findViewById(R.id.tv_priority_display_r);
        TextView vField1_p = (TextView) v.findViewById(R.id.tv_priority_prompt_r);
        TextView vField2 = (TextView) v.findViewById(R.id.tv_enddate_display_r);
        TextView vField2_p = (TextView) v.findViewById(R.id.tv_enddate_prompt_r);
        Button vStatus = (Button) v.findViewById(R.id.checkBox_status);
        Cursor cr = c;
        String s = c.getString(c.getColumnIndex(dBTasks.D_STATUS));
        int rowid = Integer.parseInt(c.getString(c.getColumnIndex(dBUserPreferences.KEY_ROWID)));
        if (vField1 != null) {
            vField1.setText(context.getResources().getStringArray(R.array.priorityarray)[Integer.valueOf(c.getString(c.getColumnIndex(dBTasks.D_PRIORITY_CODE))).intValue()]);
        }
        if (vTask != null) {
            if (s == null || !s.equals("Y")) {
                vTask.setPaintFlags(vTask.getPaintFlags() & -17);
                vField1.setPaintFlags(vTask.getPaintFlags() & -17);
                vField2.setPaintFlags(vTask.getPaintFlags() & -17);
            } else {
                vTask.setPaintFlags(vTask.getPaintFlags() | 16);
                vField1.setPaintFlags(vTask.getPaintFlags() | 16);
                vField2.setPaintFlags(vTask.getPaintFlags() | 16);
            }
        }
        if (vStatus != null) {
            if (s == null || !s.equals("Y")) {
                vStatus.setBackgroundResource(R.drawable.checkbox_off);
            } else {
                vStatus.setBackgroundResource(R.drawable.checkbox_on);
            }
            vStatus.setOnClickListener(new C00171(rowid, s, cr));
        }
        if (vField2 != null) {
            vField2.setText(new dateTime(this.mUserDateSeparator, this.mUserDateFormat, this.mUserTimeFormat).convertDate(c.getString(c.getColumnIndex(dBTasks.D_DUEDATE)), Character.valueOf(dateTime.YearFormat), Character.valueOf(this.mUserDateFormat)));
        }
        if (vField2_p != null) {
            if (c.getString(c.getColumnIndex(dBTasks.D_DUEDATE)) == null) {
                vField2_p.setVisibility(View.VISIBLE);
            } else {
                vField2_p.setVisibility(View.INVISIBLE);
            }
        }
    }
}
