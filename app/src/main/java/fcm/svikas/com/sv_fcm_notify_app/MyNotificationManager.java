package fcm.svikas.com.sv_fcm_notify_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


public class MyNotificationManager {

    private Context ctx;

    public static final int NOTIFICATION_ID = 234;
    public static final String CHANNEL_ID = "Weather";
    public MyNotificationManager (Context ctx) {
        this.ctx = ctx;
    }

    public void showNotification(String from, String notificationText, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID);
        Notification mNotification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notificationText)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .build();

        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, mNotification);
    }
}
