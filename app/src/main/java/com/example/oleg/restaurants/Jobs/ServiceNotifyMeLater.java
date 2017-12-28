package com.example.oleg.restaurants.Jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;

import com.example.oleg.restaurants.activities.StartActivity;
import com.example.oleg.restaurants.database.VisitEntry;
import com.example.oleg.restaurants.notifications.NotificationBuilder;

/**
 * Created by oleg on 25.12.17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ServiceNotifyMeLater extends JobService {
    public static final String WHEN = "WHEN";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String RESTAURANT_NAME = "restaurant_name";


    @Override
    public boolean onStartJob(final JobParameters params) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                long last_visit = VisitEntry.getLastVisit(context);
                long now = System.currentTimeMillis();
                PersistableBundle bundle = params.getExtras();
                long when = bundle.getLong(WHEN);
                String name = bundle.getString(RESTAURANT_NAME);
                int id = bundle.getInt(RESTAURANT_ID);
                NotificationBuilder.createNotificationRemindMeLater(context, StartActivity.class, when, name, id);
            }
        });
        thread.start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
