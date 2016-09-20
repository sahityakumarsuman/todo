package app.com.free.todo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import app.com.free.todo.db.dBTasks;
import app.com.free.todo.db.dBUserPreferences;
import app.com.free.todo.util.notifyUser;


public class ReceiveAlarm extends BroadcastReceiver {
    private NotificationManager nm;

    Context _mCotext;

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {

        this._mCotext = context;
        Bundle bundle = intent.getExtras();
        Long rowid = Long.valueOf(bundle.getLong(dBUserPreferences.KEY_ROWID));
        String title = bundle.getString(dBTasks.D_TITLE);
        String detail = bundle.getString(dBTasks.D_DETAIL);
        try {
            this.nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent launchActivity = new Intent(context, EditTask.class);
            launchActivity.putExtra(dBUserPreferences.KEY_ROWID, rowid);
            launchActivity.setAction(rowid.toString());
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);

//
//            Notification.Builder builder = new Notification.Builder(_mCotext);
//
//            builder.setAutoCancel(false);
//            builder.setTicker("this is ticker text");
//            builder.setContentTitle(title);
//            builder.setContentText("You have a new message");
//            builder.setSmallIcon(R.drawable.mytasksnotification);
//            builder.setContentIntent(contentIntent);
//            builder.setOngoing(true);
//            builder.setNumber(100);
//            builder.build();
//
//
//
//            Notification notif = new Notification(R.drawable.mytasksnotification, title, System.currentTimeMillis());
//            notif.setLatestEventInfo(context, title, detail, contentIntent);
//            notif.sound = (Uri) intent.getParcelableExtra("ALARM");
//            notif.flags = 16;
//            this.nm.notify(rowid.intValue(), notif);
        } catch (Exception e) {
            new notifyUser(context).Notify("error");
        }
    }
}
