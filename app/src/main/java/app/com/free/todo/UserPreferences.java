package app.com.free.todo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPreferences extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferenceoptions);
    }

    private void saveToDb() {
    }
}
