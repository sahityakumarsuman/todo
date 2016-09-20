package app.com.free.todo.util;

import android.content.Context;
import android.widget.Toast;

public class notifyUser {
    private Context mAppContext;

    public notifyUser(Context appContext) {
        this.mAppContext = appContext;
    }

    public void Notify(int notificationId) {
        switch (notificationId) {
        }
        Notify(null);
    }

    public void Notify(String notificationText) {
        Toast toast = Toast.makeText(this.mAppContext, notificationText, Toast.LENGTH_SHORT);
        toast.setGravity(16, 0, 0);
        toast.show();
    }
}
