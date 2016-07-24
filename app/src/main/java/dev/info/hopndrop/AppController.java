package dev.info.hopndrop;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private static String api_id;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.startInit(this)
                .setAutoPromptLocation(true)
                //.setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();
        OneSignal.enableVibrate(true);
        OneSignal.enableSound(true);
        OneSignal.enableNotificationsWhenActive(true);
        OneSignal.enableInAppAlertNotification(true);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
         * a new one comes in while the app is running.
         * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
         *
         * @param message        The message string the user seen/should see in the Android status bar.
         * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
         * @param isActive       Was the app in the foreground when the notification was received.
         */
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            Intent intent = new Intent(getApplicationContext(),
            MainActivity.class);
            startActivity(intent);
        }
    }





}