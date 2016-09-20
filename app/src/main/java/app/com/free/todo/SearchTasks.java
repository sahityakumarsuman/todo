package app.com.free.todo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;
import app.com.free.todo.util.Constants;
import app.com.free.todo.util.notifyUser;
import app.com.free.todo.util.taskSimpleCursorAdapter;


public class SearchTasks extends Activity {
    private String mQueryString;
    private Button mSearch;
    private TextView mSearchText;
    private taskSimpleCursorAdapter mTaskAdapter;
    private Cursor mTaskCursor;
    private ListView mTaskSearchList;
    private dBTasks mTaskdb;

    /* renamed from: com.crazelle.app.todo.free.SearchTasks.1 */
    class C00131 implements OnClickListener {
        C00131() {
        }

        public void onClick(View v) {
            SearchTasks.this.onSearchRequested();
        }
    }

    /* renamed from: com.crazelle.app.todo.free.SearchTasks.2 */
    class C00142 implements OnItemClickListener {
        C00142() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent i = new Intent();
            i.setClass(SearchTasks.this.getBaseContext(), EditTask.class);
            i.putExtra(dBUserPreferences.KEY_ROWID, id);
            SearchTasks.this.startActivityForResult(i, 2);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_search);
        this.mTaskdb = new dBTasks(this);
        this.mTaskdb.Open();
        this.mTaskSearchList = (ListView) findViewById(R.id.lvSearchResults);
        this.mSearchText = (TextView) findViewById(R.id.tvsearchtext);
        this.mSearch = (Button) findViewById(R.id.search_tasks);
        this.mSearch.setOnClickListener(new C00131());
        this.mTaskSearchList.setOnItemClickListener(new C00142());
        onNewIntent(getIntent());
    }

    public void searchTasks(String searchString) {
        this.mTaskCursor = this.mTaskdb.searchTasks(searchString);
        startManagingCursor(this.mTaskCursor);
        this.mTaskAdapter = new taskSimpleCursorAdapter(this, R.layout.task_row, this.mTaskCursor, new String[]{dBTasks.D_TITLE, dBTasks.D_PRIORITY, dBTasks.D_DUEDATE}, new int[]{R.id.task_row_edit, R.id.tv_priority_display_r, R.id.tv_enddate_display_r});
        if (this.mTaskCursor.getCount() < 1) {
            new notifyUser(getApplicationContext()).Notify(getString(R.string.messagenotasksfound));
        }
        this.mTaskSearchList.setAdapter(this.mTaskAdapter);
    }

    public void onNewIntent(Intent queryIntent) {
        if ("android.intent.action.SEARCH".equals(queryIntent.getAction())) {
            this.mQueryString = queryIntent.getStringExtra("query");
            searchTasks(this.mQueryString);
        }
        this.mSearchText.setText(this.mQueryString);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 5, 1, R.string.menu_search);
        menu.add(0, 6, 1, R.string.menu_return);
        menu.add(0, 4, 2, R.string.menu_help);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Constants.SEARCH_ID /*5*/:
                onSearchRequested();
                return true;
            case Constants.EXIT_ID /*6*/:
                finish();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.EDIT_ID /*2*/:
                finish();
            default:
        }
    }
}
