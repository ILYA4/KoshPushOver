package com.example.ilya4.koshpushover;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilya4.koshpushover.io.WritingAndReading;
import com.example.ilya4.koshpushover.listeners.QrButtonOnClickListener;
import com.example.ilya4.koshpushover.pojo.MessageToSend;
import com.example.ilya4.koshpushover.receivers.AlarmReceiver;
import com.example.ilya4.koshpushover.retrofit.ApiClient;
import com.example.ilya4.koshpushover.retrofit.ApiInterface;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final static int RC_BARCODE_CAPTURE = 901;

    private ApiInterface apiInterface;
    private static final String TAG = ".MAINActivity";

    private EditText tokenApi;
    private EditText userKey;
    private EditText message;

    private TextView dateTime_tv;
    private CheckBox delayTimeCB;
    private Calendar delayedTime = Calendar.getInstance();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "initialize");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tokenApi = findViewById(R.id.tokenApi_et);
        userKey = findViewById(R.id.userKey_et);
        message = findViewById(R.id.message_et);
        dateTime_tv = findViewById(R.id.datetime_tv);
        delayTimeCB = findViewById(R.id.delayTime);
        file = new File(getFilesDir(), "history.dg");

        findViewById(R.id.qr_camera).setOnClickListener(new QrButtonOnClickListener(this));
        Log.v(TAG, "start retrofit");
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNotNullET()){
                    if (!delayTimeCB.isChecked()){
                        Log.v(TAG, "post push now");
                Call<MessageToSend> postCall = apiInterface.sendMessage(tokenApi.getText().toString(), userKey.getText().toString(),
                        message.getText().toString());
                postCall.enqueue(new Callback<MessageToSend>() {
                    @Override
                    public void onResponse(Call<MessageToSend> call, Response<MessageToSend> response) {
                        Log.v(TAG,  " push success: " + response.toString());
                        new WritingAndReading().writeToFile(file,tokenApi.getText().toString() + " -> " +
                                userKey.getText().toString() + ":\n at " +
                                new SimpleDateFormat("HH:mm ", Locale.getDefault()).format(delayedTime.getTime())
                                + ":\n" + message.getText().toString()+"\n");
                        Toast.makeText(getApplicationContext(), "Сообщение отправлено", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageToSend> call, Throwable t) {
                        Log.e(TAG,  "push failure: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), "Сообщение не отправлено," +
                                " проверьте Token и User Key", Toast.LENGTH_SHORT).show();
                    }
                });
                } else  addDelayPush(tokenApi.getText().toString(), userKey.getText().toString(), message.getText().toString());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Не все поля заполнены", Toast.LENGTH_SHORT);
                    toast.show();


            }
        }
    }
    );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkNotNullET (){
        return tokenApi.getText().length()!=0 && userKey.getText().length()!=0 && message.getText().length()!=0;
    }

    public void specifyDateTime(View view){
        new DateTimePickers(this, delayedTime, dateTime_tv).getDateTimeCalendar();
    }

    private void addDelayPush(String token, String userKey, String message){
        Log.v(TAG, "addDelayPush()");
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("token", token);
        intent.putExtra("userKey", userKey);
        intent.putExtra("message", message);
        Toast.makeText(getApplicationContext(), "Сообщение отправлено", Toast.LENGTH_SHORT).show();

        new WritingAndReading().writeToFile(file, token + " -> " +
                userKey + ":\n at " + new SimpleDateFormat("HH:mm ", Locale.getDefault()).format(delayedTime.getTime())+ ":\n" + message + "\n");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, delayedTime.getTimeInMillis(), pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data!=null){
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    message.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                }else {
                    Toast.makeText(this, R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            }else {
                Toast.makeText(this, R.string.barcode_error, Toast.LENGTH_SHORT).show();
            }
        }else super.onActivityResult(requestCode,resultCode,data);
    }
}
