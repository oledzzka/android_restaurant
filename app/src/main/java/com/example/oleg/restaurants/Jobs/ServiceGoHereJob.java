package com.example.oleg.restaurants.Jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.oleg.restaurants.activities.StartActivity;
import com.example.oleg.restaurants.database.VisitEntry;
import com.example.oleg.restaurants.notifications.NotificationBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by oleg on 25.12.17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ServiceGoHereJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                long last_visit = VisitEntry.getLastVisit(context);
                long now = System.currentTimeMillis();
                if (TimeUnit.MILLISECONDS.toDays(now - last_visit) < 7 ) {
                    NotificationBuilder.createNotificationVisit(context, StartActivity.class);
                }
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
