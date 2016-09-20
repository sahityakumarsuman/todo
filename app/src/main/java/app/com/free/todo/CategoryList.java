package app.com.free.todo;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import app.com.free.todo.db.dBCategory;
import app.com.free.todo.util.Constants;
import app.com.free.todo.util.categorySimpleCursorAdapter;
import app.com.free.todo.util.notifyUser;


public class CategoryList extends ListActivity {
    private Button mAdd;
    private categorySimpleCursorAdapter mCategoryAdapter;
    private Cursor mCategoryCursor;
    private dBCategory mCategorydb;
    private String mOldValue;

    /* renamed from: com.crazelle.app.todo.free.CategoryList.1 */
    class C00001 implements OnClickListener {
        C00001() {
        }

        public void onClick(View v) {
            CategoryList.this.editCategory(true, 0);
        }
    }

    /* renamed from: com.crazelle.app.todo.free.CategoryList.2 */
    class C00012 implements DialogInterface.OnClickListener {
        private final /* synthetic */ long val$frowID;
        private final /* synthetic */ EditText val$input;
        private final /* synthetic */ boolean val$newrow;

        C00012(EditText editText, boolean z, long j) {
            this.val$input = editText;
            this.val$newrow = z;
            this.val$frowID = j;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            String value = this.val$input.getText().toString();
            if (this.val$newrow) {
                CategoryList.this.mCategorydb.create(value, "0");
                new notifyUser(CategoryList.this.getApplicationContext()).Notify(CategoryList.this.getString(R.string.saved));
            } else {
                CategoryList.this.mCategorydb.update(this.val$frowID, value);
                CategoryList.this.mCategorydb.updateTaskTable(CategoryList.this.mOldValue, value);
                new notifyUser(CategoryList.this.getApplicationContext()).Notify(CategoryList.this.getString(R.string.saved));
            }
            CategoryList.this.reDisplayList();
        }
    }

    /* renamed from: com.crazelle.app.todo.free.CategoryList.3 */
    class C00023 implements DialogInterface.OnClickListener {
        C00023() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            new notifyUser(CategoryList.this.getApplicationContext()).Notify(CategoryList.this.getString(R.string.discarded));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorylist);
        this.mCategorydb = new dBCategory(this);
        this.mCategorydb.Open();
        this.mAdd = (Button) findViewById(R.id.add_category);
        populateCategoryList();
        this.mAdd.setOnClickListener(new C00001());
        registerForContextMenu(getListView());
    }

    public void populateCategoryList() {
        this.mCategoryCursor = this.mCategorydb.FetchAllRows(false, dBCategory.D_CODE);
        startManagingCursor(this.mCategoryCursor);
        this.mCategoryAdapter = new categorySimpleCursorAdapter(this, R.layout.category_row, this.mCategoryCursor, new String[]{dBCategory.D_DESCRIPTION}, new int[]{R.id.category_row_edit});
        setListAdapter(this.mCategoryAdapter);
    }

    public void editCategory(boolean isNew, long rowID) {
        boolean newrow = isNew;
        long frowID = rowID;
        EditText input = new EditText(this);
        input.setSingleLine();
        if (!isNew) {
            Cursor cur = this.mCategorydb.FetchRow(rowID);
            input.setText(cur.getString(cur.getColumnIndex(dBCategory.D_CODE)));
            this.mOldValue = cur.getString(cur.getColumnIndex(dBCategory.D_CODE));
        }
        Builder alert = new Builder(this);
        alert.setMessage(R.string.category);
        alert.setView(input);
        alert.setPositiveButton(R.string.save, new C00012(input, newrow, frowID));
        alert.setNegativeButton(R.string.cancel, new C00023());
        alert.show();
    }

    public void reDisplayList() {
        stopManagingCursor(this.mCategoryCursor);
        this.mCategoryCursor.close();
        this.mCategoryCursor = this.mCategorydb.FetchAllRows(false, dBCategory.D_CODE);
        startManagingCursor(this.mCategoryCursor);
        this.mCategoryAdapter.changeCursor(this.mCategoryCursor);
    }

    private boolean checkTasksCategory(long categoryId) {
        Cursor cur = this.mCategorydb.FetchRow(categoryId);
        return this.mCategorydb.checkTaskTable(cur.getString(cur.getColumnIndex(dBCategory.D_CODE)));
    }

    public void CloseResources() {
        this.mCategoryCursor.close();
        this.mCategorydb.Close();
        finish();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.menu_select_action);
        menu.add(0, 2, 0,R.string.menu_edit);
        menu.add(0, 3, 1,R.string.menu_delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.EDIT_ID /*2*/:
                editCategory(false, ((AdapterContextMenuInfo) item.getMenuInfo()).id);
                break;
            case Constants.DELETE_ID /*3*/:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                if (checkTasksCategory(info.id)) {
                    new notifyUser(getApplicationContext()).Notify(getString(R.string.messagecategoryinuse));
                } else {
                    this.mCategorydb.delete(info.id);
                    reDisplayList();
                    new notifyUser(getApplicationContext()).Notify(getString(R.string.deleted));
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        editCategory(false, id);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 1, 0, R.string.menu_add);
        menu.add(0, 13, 1, R.string.menu_return);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Constants.ADD_ID /*1*/:
                editCategory(true, 0);
                return true;
            case Constants.RETURN_ID /*13*/:
                CloseResources();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        CloseResources();
    }
}
