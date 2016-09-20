package app.com.free.todo.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


import android.support.v4.app.NotificationCompat;


import java.util.Calendar;

import app.com.free.todo.R;
import app.com.free.todo.Start;
import app.com.free.todo.db.dBTasks;

/**
 * Created by sahitya on 9/20/16.
 */
public class AlramForTask extends BroadcastReceiver {

    private dBTasks mTaskdb;

    private Context _context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this._context = context;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();


        this.mTaskdb = new dBTasks(context);
        this.mTaskdb.Open();
        int totalCountNoPresent = this.mTaskdb.getTaskCount();


        Log.d("Toat entry in table", " " + totalCountNoPresent);


        showNotification(totalCountNoPresent);


        wl.release();


    }


    public void setAlarm(Context context) {


        Log.d("Aralm set", "Alram is set now ");

        Intent intent = new Intent(context, AlramForTask.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                60 * 60 * 1000, pendingIntent);
    }


    public void showNotification(int totalCountNoPresent) {
        PendingIntent pi = PendingIntent.getActivity(_context, 0, new Intent(_context, Start.class), 0);

        Notification notification = new NotificationCompat.Builder(_context)
                .setTicker("Alert for you")
                .setSmallIcon(R.drawable.mytasks)
                .setContentTitle("TodNote Alert")
                .setContentText("You have total " + totalCountNoPresent + " task to complete")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}
