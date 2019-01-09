package com.example.ilya4.koshpushover.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.ilya4.koshpushover.MainActivity;
import com.example.ilya4.koshpushover.R;
import com.example.ilya4.koshpushover.pojo.MessageToSend;
import com.example.ilya4.koshpushover.retrofit.ApiClient;
import com.example.ilya4.koshpushover.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver
{

    private ApiInterface apiInterface;
    private static final String TAG = ".AlarmReceiver";
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(TAG, "onReceive Method");

        Intent alarmIntent = new Intent(context, MainActivity.class);
        final String token = intent.getStringExtra("token");
        final String userKey = intent.getStringExtra("userKey");
        final String message = intent.getStringExtra("message");



        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);

        Log.e(TAG, "5");
        final PendingIntent contentIntent =  PendingIntent.getActivity(context, 0, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Log.e(TAG, "6");

        Call<MessageToSend> postCall = apiInterface.sendMessage(token, userKey, message);
        postCall.enqueue(new Callback<MessageToSend>() {
            @Override
            public void onResponse(Call<MessageToSend> call, Response<MessageToSend> response) {
                Log.v(TAG, "response success" + response.toString() );
                makeNotif(1, context, contentIntent, message, "Push отправлено");

            }

            @Override
            public void onFailure(Call<MessageToSend> call, Throwable t) {
                makeNotif(1, context, contentIntent, message, "Push не отправлено, проверьте Token и UserKey");
                Log.e(TAG, "response failure" + t.getMessage());
            }

        });


        Log.e(TAG, "7");
    }

    private void makeNotif(int id, Context context, PendingIntent intent, String message, String title){
        Log.e(TAG, "notify this message: " +  message);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context.getApplicationContext());

        Log.e(TAG, "1");
       builder
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            String CHANNEL_ID = "ch01";
            Log.e(TAG, "8");
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"ls", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
        Log.e(TAG, "2");

        Log.e(TAG, "3");

        notificationManager.notify(id, notification);
        Log.e(TAG, "4");
    }
}
