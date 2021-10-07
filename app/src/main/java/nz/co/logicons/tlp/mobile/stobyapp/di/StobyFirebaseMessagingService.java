package nz.co.logicons.tlp.mobile.stobyapp.di;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.MainActivity;
import nz.co.logicons.tlp.mobile.stobyapp.R;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiUserClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

/*
 * @author by Allen
 */
@AndroidEntryPoint
public class StobyFirebaseMessagingService extends FirebaseMessagingService {
    @Inject
    SharedPreferences.Editor editor;
    @Inject
    SharedPreferences sharedPreferences;

    public StobyFirebaseMessagingService(){
        Log.d(Constants.TAG, "StobyFirebaseMessagingService: " + this.hashCode());
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d(Constants.TAG, "onMessageReceived: " +retroApiUserClient2);
//        DaggerServiceComponent
        if (remoteMessage.getData().size() > 0) {
            Log.d(Constants.TAG, "Message data payload: " + remoteMessage.getData());
            showNotification(remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("message"));

        } else if (remoteMessage.getNotification() != null) {
            Log.d(Constants.TAG, "Message notifications payload: " + remoteMessage.getNotification());
            showNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "FCM onCreate: ");

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(Constants.TAG, "Refreshed token: " + token);

        editor.putString(PreferenceKeys.FCM_TOKEN, token);
        editor.apply();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        String fcmToken = sharedPreferences.getString(PreferenceKeys.FCM_TOKEN, "").toString();
        User user = new User(username, password, fcmToken);

        RetroApiUserClient retroApiUserClient = new RetroApiUserClient();
        retroApiUserClient.reinitRetroApi(sharedPreferences);
        retroApiUserClient.saveFcmToken(user);
    }

    private void showNotification(String title, String message) {
        String channelId = getString(R.string.default_notification_channel_id);
        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentText(message)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_24)
                .setContentIntent(pendingIntent).build();
        notificationManager.notify((new Random()).nextInt(), notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        NotificationChannel notificationChannel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
