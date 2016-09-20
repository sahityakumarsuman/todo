package app.com.free.todo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class UserPreferencesdB extends Activity {
    private Spinner mDateFormat;
    private Spinner mDateSeparator;
    private ArrayAdapter<CharSequence> mDateSeparatorAdapter;
    private CheckBox mEnableReminder;
    private EditText mReminder;
    private EditText mTimeFormat;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_preferences);
        this.mDateFormat = (Spinner) findViewById(R.id.pref_date_format);
        this.mDateSeparator = (Spinner) findViewById(R.id.pref_date_separator);
        this.mDateSeparatorAdapter = ArrayAdapter.createFromResource(this, R.array.dateseparator, android.R.layout.simple_spinner_item);
        this.mDateSeparatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.mDateSeparator.setAdapter(this.mDateSeparatorAdapter);
    }
}
