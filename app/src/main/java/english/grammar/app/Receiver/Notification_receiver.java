package english.grammar.app.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import english.grammar.app.Constants;
import english.grammar.app.Manager.Quotes_DBManager;
import english.grammar.app.MessageOfTheDay;
import english.grammar.app.Models.QuotesModel;
import english.grammar.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notification_receiver extends BroadcastReceiver {

    public static final String TASK_ID_TO_DISPLAY = "TASK_ID_TO_DISPLAY";
    public static final String ACTION_SET_TASK_DONE = "ACTION_SET_TASK_DONE";
    public static final String ACTION_POSTPONE_TASK = "ACTION_POSTPONE_TASK";
    public static final String PARAM_TASK_ID = "PARAM_TASK_ID";
    private static final int POSTPONE_MINUTES = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, MessageOfTheDay.class);
        repeating_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        List<QuotesModel> data = new ArrayList<>();
        List<QuotesModel> ids = new ArrayList<>();
        Quotes_DBManager dbManager = new Quotes_DBManager(context);
        dbManager.open();
        try {
            dbManager.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = dbManager.getQuote();
        ids = dbManager.getQuoteIDs();
        Collections.shuffle(data);
        Collections.shuffle(ids);

        displayNotification(context, "Today's Quote", data.get(Integer.parseInt(ids.get(5).getId())).getEng_quote());

    }
    public static void displayNotification(Context context, String contentTitle, String contentText) {

        //Create notification channel if running Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            createNotificationChannel(notificationManager, Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, Constants.NOTIFICATION_CHANNEL_DESCRIPTION);
        }

        //Intent for "DONE" button on BigView style
        Intent setTaskDoneIntent = new Intent(context, MessageOfTheDay.class);
        setTaskDoneIntent.setAction(ACTION_SET_TASK_DONE);
        setTaskDoneIntent.putExtra(PARAM_TASK_ID, -1);
        PendingIntent setTaskDonePendingIntent = PendingIntent.getService(context, -1, setTaskDoneIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for "POSTPONE" button on BigView style
        Intent postponeTaskIntent = new Intent(context, MessageOfTheDay.class);
        postponeTaskIntent.setAction(ACTION_POSTPONE_TASK);
        postponeTaskIntent.putExtra(PARAM_TASK_ID, -1);
        PendingIntent postponeTaskPendingIntent = PendingIntent.getService(context, -1, postponeTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for clicking notification
        Intent openTaskIntent = new Intent(context, MessageOfTheDay.class);
        openTaskIntent.putExtra(TASK_ID_TO_DISPLAY, -1);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack
        stackBuilder.addParentStack(MessageOfTheDay.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(openTaskIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent openTaskPendingIntent = stackBuilder.getPendingIntent(-1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logoalpha)
                .setColor(ContextCompat.getColor(context, R.color.white))
                .setVibrate(new long[] { 50, 50, 200, 50 })
                .setLights(ContextCompat.getColor(context, R.color.white), 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle(contentTitle)
                .setContentText(contentText)

                //Intent when clicking notification content
                .setContentIntent(openTaskPendingIntent)
                .setAutoCancel(true)

                //Make notification pop-up in post 5.0 devices
                .setPriority(Notification.PRIORITY_HIGH)

                //BigView
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText));

        // Get an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(-1, mBuilder.build());
    }

    //Needed for Android 8 Oreo +
    private static void createNotificationChannel(NotificationManager notificationManager, @NonNull String channelId, String channelName, String channelDescription) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }

}
