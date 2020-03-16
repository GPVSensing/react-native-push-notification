package com.dieam.reactnativepushnotification.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class WakeupService extends IntentService {

    private static final String LOG_TAG = WakeupService.class.getSimpleName();

    /**
     * Constructor comment
     */
    public WakeupService() {
        super("WakeupService");
        // Start redeliver intent, in case service is killed
        // it would be restarted due to this flag !
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, App.WAKEUP_CHANNEL_ID).build();
            startForeground(1, notification);
        }
    }

    /**
     * Wakeup user device on a background thread.
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent");

        if (intent != null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();

            if (isScreenOn == false) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "GPV:APP-LOCK");
                wl.acquire(5000);
            }
        }
    }
}
