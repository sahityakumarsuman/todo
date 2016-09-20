package app.com.free.todo.util;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OnSpinnerSelected implements OnItemSelectedListener {
    public int mPos;
    public String mValue;
    public Cursor pCursorSelected;

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        this.mPos = pos;
        this.pCursorSelected = (Cursor) parent.getItemAtPosition(pos);
        this.mValue = this.pCursorSelected.getString(1);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        this.mPos = 0;
        this.mValue = null;
        this.pCursorSelected = null;
    }
}
