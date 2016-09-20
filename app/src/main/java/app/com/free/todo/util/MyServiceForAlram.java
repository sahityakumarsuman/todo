package app.com.free.todo.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sahitya on 9/20/16.
 */
public class MyServiceForAlram extends Service {


    AlramForTask alarmPart = new AlramForTask();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Task Loaded","Service is started for alram");
        alarmPart.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarmPart.setAlarm(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
