package aniweeb.com;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 02/03/2023.
 */
public class ApplicationClass extends Application {
    private static final String ONESIGNAL_APP_ID = "b9a49050-1552-4001-a0a6-5cfca9d83131";

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: Add OneSignal initialization here

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
    }
}
