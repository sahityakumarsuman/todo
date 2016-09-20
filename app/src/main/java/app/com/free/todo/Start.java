package app.com.free.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import app.com.free.todo.db.dBCategory;
import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;
import app.com.free.todo.util.Constants;
import app.com.free.todo.util.MyServiceForAlram;
import app.com.free.todo.util.OnSpinnerSelected;
import app.com.free.todo.util.SetReminder;
import app.com.free.todo.util.categorySimpleCursorAdapter;
import app.com.free.todo.util.dateTime;
import app.com.free.todo.util.notifyUser;
import app.com.free.todo.util.taskSimpleCursorAdapter;

public class Start extends ListActivity {
    //    private AdView adView;
    private AlarmManager am;
    private String mActiveFilter;
    private String mActiveSort;
    private final String mAdMob_id;
    private Button mAdd;
    private Spinner mCategory;
    private categorySimpleCursorAdapter mCategoryAdapter;
    private Cursor mCategoryCursor;
    private dBCategory mCategorydb;
    private dateTime mDTTM;
    private Locale mLocale;
    private String mReminderAlarm;
    private boolean mReminderEnabled;
    private Button mSearch;
    private Spinner mSort;
    private ArrayAdapter<CharSequence> mSortAdapter;
    private taskSimpleCursorAdapter mTaskAdapter;
    private Cursor mTaskCursor;
    private dBTasks mTaskdb;
    private char mUserDateFormat;
    private char mUserDateSeparator;
    private boolean mUserDisplayCompletedTasks;
    private char mUserTimeFormat;
    private Intent myIntent;
    private UserPreferenceValues up;

    /* renamed from: com.crazelle.app.todo.free.Start.1 */
    class C00151 implements OnClickListener {
        C00151() {
        }

        public void onClick(View v) {
            Start.this.editTask(true, 0);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.Start.2 */
    class C00162 implements OnClickListener {
        C00162() {
        }

        public void onClick(View v) {
            Start.this.onSearchRequested();
        }
    }

    /* renamed from: com.crazelle.app.todo.free.Start.3 */
    class C01053 extends OnSpinnerSelected {
        C01053() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Start.this.mActiveFilter = Long.toString(id);
            int h = ((Cursor) parent.getItemAtPosition(pos)).getPosition();
            Start.this.reDisplayList();
        }
    }

    /* renamed from: com.crazelle.app.todo.free.Start.4 */
    class C01064 extends OnSpinnerSelected {
        C01064() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            Start.this.deriveSortDBField(pos);
            Start.this.reDisplayList();
        }
    }

    public Start() {
        this.mUserDateSeparator = '/';
        this.mUserDateFormat = dateTime.MonthFormat;
        this.mUserTimeFormat = dateTime.MonthFormat;
        this.mAdMob_id = "a14f25b98d58f6d";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist);
        this.mLocale = Locale.getDefault();


//        this.adView = new AdView((Activity) this, AdSize.SMART_BANNER, "a14f25b98d58f6d");
//        ((LinearLayout) findViewById(R.id.primaryLayout)).addView(this.adView);
//        AdRequest request = new AdRequest();
//        request.addKeyword("computer");
//        request.addKeyword("jewelry");
//        request.addKeyword("car");
//        request.addKeyword("sale");
//        request.addKeyword("thanksgiving");
//        request.addKeyword("black friday");
//        request.addKeyword("christmas");
//        request.addKeyword("columbus day");
//        request.addKeyword("indepencence day");
//        request.addKeyword("Martin Luther King Day");
//        request.addKeyword("Valentine's Day");
//        request.addKeyword("Presidents' Day");
//        request.addKeyword("St. Patrick's Day");
//        request.addKeyword("Easter");
//        request.addKeyword("Cinco de Mayo");
//        request.addKeyword("Mothers' Day");
//        request.addKeyword("Memorial Day");
//        request.addKeyword("D-Day");
//        request.addKeyword("Labor Day");
//        request.addKeyword("Rosh Hashana");
//        request.addKeyword("Yom Kippur");
//        request.addKeyword("Halloween");
//        request.addKeyword("Diwali / Deepavali");
//        request.addKeyword("Chanukah / Hanukkah");
//        this.adView.setGravity(48);
//        this.adView.loadAd(request);
        this.am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        this.myIntent = new Intent(this, ReceiveAlarm.class);
        this.up = new UserPreferenceValues(this);
        this.mUserDisplayCompletedTasks = this.up.getDisplayCompletedTasks();
        this.mUserDateFormat = this.up.getUserDateFormat();
        this.mUserDateSeparator = this.up.getUserDateSeparator();
        this.mUserTimeFormat = this.up.getTimeFormat();
        this.mDTTM = new dateTime(this.mUserDateSeparator, this.mUserDateFormat, this.mUserTimeFormat);
        this.mActiveFilter = null;
        this.mActiveSort = null;
        this.mTaskdb = new dBTasks(this);
        this.mTaskdb.Open();
        this.mTaskdb.setmDisplayCompletedTasks(this.mUserDisplayCompletedTasks);

        this.mCategorydb = new dBCategory(this);
        this.mCategorydb.Open();
        this.mCategory = (Spinner) findViewById(R.id.category_filter);
        this.mSort = (Spinner) findViewById(R.id.sort_tasks);
        this.mAdd = (Button) findViewById(R.id.add_todo);
        this.mSearch = (Button) findViewById(R.id.search_tasks);
        this.mAdd.setOnClickListener(new C00151());
        this.mSearch.setOnClickListener(new C00162());
        this.mCategorydb.update(1, getString(R.string.category_all));
        populateTaskList();
        populateCategoryFilter();
        populateSortoptions();
        this.mCategory.setOnItemSelectedListener(new C01053());
        this.mSort.setOnItemSelectedListener(new C01064());
        registerForContextMenu(getListView());

        startService(new Intent(this, MyServiceForAlram.class));

    }

    private void populateCategoryFilter() {
        this.mCategoryCursor = this.mCategorydb.FetchAllRows(true, dBUserPreferences.KEY_ROWID);
        this.mCategoryAdapter = new categorySimpleCursorAdapter(this, 17367048, this.mCategoryCursor, new String[]{dBCategory.D_CODE}, new int[]{16908308});
        this.mCategoryAdapter.setDropDownViewResource(17367049);
        this.mCategory.setAdapter(this.mCategoryAdapter);
    }

    private void populateSortoptions() {
        this.mSortAdapter = ArrayAdapter.createFromResource(this, R.array.sortoptions, android.R.layout.simple_spinner_item);
        this.mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.mSort.setAdapter(this.mSortAdapter);
    }

    private void populateTaskList() {
        this.mTaskCursor = this.mTaskdb.FetchAllRows(this.mActiveFilter, this.mActiveSort);
        startManagingCursor(this.mTaskCursor);
        this.mTaskAdapter = new taskSimpleCursorAdapter(this, R.layout.task_row, this.mTaskCursor, new String[]{dBTasks.D_TITLE, dBTasks.D_PRIORITY, dBTasks.D_DUEDATE, dBTasks.TASK_TAG}, new int[]{R.id.task_row_edit, R.id.tv_priority_display_r, R.id.tv_enddate_display_r, R.id.tv_tagTask_display_r});
        setListAdapter(this.mTaskAdapter);
    }

    private void deriveSortDBField(int selection) {
        switch (selection) {
            case 0:
                this.mActiveSort = dBTasks.D_TITLE;
            case Constants.ADD_ID /*1*/:
                this.mActiveSort = "duedate, duetime";
            case Constants.EDIT_ID /*2*/:
                this.mActiveSort = "startdate, starttime";
            case Constants.DELETE_ID /*3*/:
                this.mActiveSort = "priority_code DESC";
            default:
        }
    }

    public void reDisplayList() {
        stopManagingCursor(this.mTaskCursor);
        this.mTaskCursor.close();
        this.mTaskCursor = this.mTaskdb.FetchAllRows(this.mActiveFilter, this.mActiveSort);
        startManagingCursor(this.mTaskCursor);
        this.mTaskAdapter.changeCursor(this.mTaskCursor);
    }

    private void editTask(boolean isNew, long rowID) {
        Intent i = new Intent(this, EditTask.class);
        if (isNew) {
            startActivityForResult(i, 1);
            return;
        }
        i.putExtra(dBUserPreferences.KEY_ROWID, rowID);
        startActivityForResult(i, 2);
    }

    public void CloseResources() {
        if (this.mTaskCursor != null) {
            this.mTaskCursor.close();
        }
        this.mTaskdb.Close();
        if (this.mCategoryCursor != null) {
            this.mCategoryCursor.close();
        }
        this.mCategorydb.Close();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 7, 1, R.string.menu_get_task_list);
        menu.add(1, 1, 0, R.string.menu_add);
        menu.add(1, 5, 2, R.string.menu_search);
        menu.add(0, 6, 1, R.string.menu_exit);
        menu.add(0, 14, 2, R.string.menu_personalization);
        menu.add(0, 4, 3, R.string.menu_help);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Constants.ADD_ID /*1*/:
                editTask(true, 0);
                return true;
            case Constants.HELP_ID /*4*/:
                startActivityForResult(new Intent(this, HelpMain.class), 4);
                return true;
            case Constants.SEARCH_ID /*5*/:
                onSearchRequested();
                return true;
            case Constants.EXIT_ID /*6*/:
                CloseResources();
                return true;
            case Constants.CATEGORY_ID /*7*/:
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("market://details?id=com.crazelle.app.todo"));
                startActivity(i);
                return true;
            case Constants.PERSONALIZATION_ID /*14*/:
                startActivityForResult(new Intent(this, UserPreferences.class), 14);
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.CATEGORY_ID /*7*/:
                reDisplayList();
                populateCategoryFilter();
            case Constants.PERSONALIZATION_ID /*14*/:
                this.up = new UserPreferenceValues(this);
                this.mReminderEnabled = this.up.getReminder();
                this.mReminderAlarm = this.up.getReminderAlarm();
                this.mUserDateSeparator = this.up.getUserDateSeparator();
                this.mUserDisplayCompletedTasks = this.up.getDisplayCompletedTasks();
                this.mTaskdb.ClearFilterfields();
                this.mTaskdb.setDuedate(this.mDTTM.getSystemDate(Character.valueOf(dateTime.YearFormat)));
                this.mTaskdb.setDuetime(this.mDTTM.getSystemTime(Character.valueOf(' ')));
                this.mTaskdb.setStatus("N");
                Cursor cur = this.mTaskdb.FetchAllRows();
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    SetReminder sr = new SetReminder(getApplicationContext(), ReceiveAlarm.class, this.am);
                    sr.setmReminderEnabled(this.mReminderEnabled);
                    sr.setmReminderAlarm(this.mReminderAlarm);
                    sr.setmKey(dBUserPreferences.KEY_ROWID);
                    sr.setmKeyValue(cur.getLong(cur.getColumnIndex(dBUserPreferences.KEY_ROWID)));
                    sr.setmSubject(dBTasks.D_TITLE);
                    sr.setmSubjectValue(cur.getString(cur.getColumnIndex(dBTasks.D_TITLE)));
                    sr.setmDetail(dBTasks.D_DETAIL);
                    sr.setmDetailValue(cur.getString(cur.getColumnIndex(dBTasks.D_DETAIL)));
                    sr.setmDateFormat(dateTime.YearFormat);
                    sr.setmDateSeparator(this.mUserDateSeparator);
                    sr.setmAlarmDate(cur.getString(cur.getColumnIndex(dBTasks.D_DUEDATE)));
                    sr.setmTimeFormat(dateTime.MonthFormat);
                    sr.setmAlarmTime(cur.getString(cur.getColumnIndex(dBTasks.D_DUETIME)));
                    if (this.mReminderEnabled) {
                        sr.Set();
                    } else {
                        sr.Cancel();
                    }
                    cur.moveToNext();
                }
                cur.close();
                this.mTaskdb.setmDisplayCompletedTasks(this.mUserDisplayCompletedTasks);
                reDisplayList();
            default:
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.menu_select_action);
        menu.add(0, 2, 0, R.string.menu_edit);
        menu.add(0, 3, 1, R.string.menu_delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.EDIT_ID /*2*/:
                editTask(false, ((AdapterContextMenuInfo) item.getMenuInfo()).id);
                return true;
            case Constants.DELETE_ID /*3*/:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                this.mTaskdb.delete(info.id);
                reDisplayList();
                new notifyUser(getApplicationContext()).Notify(getString(R.string.deleted));
                SetReminder sr = new SetReminder(getApplicationContext(), ReceiveAlarm.class, this.am);
                sr.setmReminderEnabled(this.mReminderEnabled);
                sr.setmReminderAlarm(this.up.getReminderAlarm());
                sr.setmKey(dBUserPreferences.KEY_ROWID);
                sr.setmKeyValue(info.id);
                sr.setmSubject(dBTasks.D_TITLE);
                sr.Cancel();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EditTask.class);
        i.putExtra(dBUserPreferences.KEY_ROWID, id);
        startActivityForResult(i, 2);
    }

    public void onBackPressed() {
        super.onBackPressed();
        CloseResources();
    }

    public void Start() {
    }

    public void OnResume() {
        super.onResume();
    }

    public void OnPause() {
        super.onPause();
    }
}
