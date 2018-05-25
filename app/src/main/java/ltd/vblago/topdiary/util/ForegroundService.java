package ltd.vblago.topdiary.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.activity.MainActivity;
import ltd.vblago.topdiary.model.Entry;

import static ltd.vblago.topdiary.util.Channel.CHANNEL_ID;


public class ForegroundService extends Service {

    private static final int START = 1;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    int counter;
    ArrayList<Entry> list;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("HandlerLeak")
    private Handler h = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == START) {

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(list.get(counter).title)
                        .setContentText(list.get(counter).getTimeString())
                        .setSmallIcon(R.drawable.ic_add_alarm_white_24dp)
                        .setContentIntent(pendingIntent)
                        .build();
                startForeground(1, notification);

                counter++;
                Calendar calendar = Calendar.getInstance();
                long min = calendar.getTimeInMillis();
                if (counter <= list.size()){
                    h.sendEmptyMessageDelayed(START, list.get(counter-1).minStart*1000L*60L - min);
                }else {
                    stopForeground(true);
                    h.removeMessages(START);
                    stopSelf();
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        list = (ArrayList<Entry>) intent.getSerializableExtra("list");

        notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        counter = 0;

        Calendar calendar = Calendar.getInstance();
        long min = calendar.getTimeInMillis()/1000L/60L;
        ArrayList<Entry> updateList = new ArrayList<>();
        for (Entry entry:list){
            if (entry.type >= 3 && entry.minStart > min && entry.alert){
                updateList.add(entry);
            }
        }
        list.clear();
        list = updateList;

        Collections.sort(list, new EntryComparator());
        if (list.size() != 0){
            h.sendEmptyMessage(START);
        }else {
            stopForeground(true);
            stopSelf();
        }

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
