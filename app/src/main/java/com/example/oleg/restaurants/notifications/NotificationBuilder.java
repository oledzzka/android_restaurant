package com.example.oleg.restaurants.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;

import com.example.oleg.restaurants.R;
import com.example.oleg.restaurants.activities.MainActivity;

import java.util.List;

/**
 * Created by oleg on 25.12.17.
 */

public class NotificationBuilder {
    public static final String NOTIFICATION = "notification";
    public static final String RESTAURANT_ID = "restaurant_id";
    private static final String CHANEL_ID = "my_chanel_id";
    private static final String CHANEL_NAME = "Restaurant chanel";
    private static final String GROUP_ID = "my_group_id";
    private static final String GROUP_NAME = "You asked";


    public static void createNotificationRemindMeLater(Context context, Class clazz,
                                                               long when, String  restaurantName, int restaurantId) {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, clazz);
        intent.putExtra(NOTIFICATION, true);
        intent.putExtra(RESTAURANT_ID, restaurantId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class)
                .addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource( resources,R.drawable.background_splash))
                .setSmallIcon(R.drawable.item_menu)
                .setTicker(resources.getString(R.string.time_for_food))
                .setAutoCancel(true)
                .setWhen(when)
                .setContentTitle(resources.getString(R.string.you_asked_notify))
                .setContentText(resources.getString(R.string.you_want_eating_in) + " " +restaurantName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChanelId(context, CHANEL_ID, CHANEL_NAME, GROUP_ID, GROUP_NAME));
        }

        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        defaults |= Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);

        Notification nc = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(1, nc);
        }
    }

    public static void createNotificationVisit(Context context, Class clazz) {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, clazz);
        intent.putExtra(NOTIFICATION, true);
        PendingIntent pendingIntent =PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource( resources,R.drawable.background_splash))
                .setSmallIcon(R.drawable.item_menu)
                .setTicker(resources.getString(R.string.time_for_food))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(resources.getString(R.string.go_here))
                .setContentText(resources.getString(R.string.you_long_no_visit));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChanelId(context, CHANEL_ID, CHANEL_NAME, GROUP_ID, GROUP_NAME));
        }

        int defaults = 0;
        defaults |= Notification.DEFAULT_VIBRATE;
        defaults |= Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);

        Notification nc = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(2, nc);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static String getChanelId(Context context, String channelId, String chanelName, String groupId, String groupName) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            List<NotificationChannel> channels = nm.getNotificationChannels();
            for (NotificationChannel channel: channels) {
                if (channel.getId().equals(channelId)) return channel.getId();
            }

            String group = getNotificationChanelGroup(context, groupId, groupName);
            if (group != null) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, chanelName,
                        NotificationManager.IMPORTANCE_LOW);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.enableVibration(true);
                notificationChannel.setGroup(group);
                notificationChannel.setVibrationPattern(new long[]{400, 400});
                nm.createNotificationChannel(notificationChannel);
                return channelId;
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static String getNotificationChanelGroup(Context context, String groupId, String name) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            List<NotificationChannelGroup> channels = nm.getNotificationChannelGroups();
            for (NotificationChannelGroup group: channels) {
                if (group.getId().equals(groupId)) return group.getId();
            }
            nm.createNotificationChannelGroup(new NotificationChannelGroup(groupId, name));
            return groupId;
        }
        return null;
    }
}
