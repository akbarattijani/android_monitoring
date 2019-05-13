package ip.signature.com.signatureapps.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.fragment.MenuFragment;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.global.GlobalMaps;
import ip.signature.com.signatureapps.listener.ScheduleListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.util.GPSUtil;
import ip.signature.com.signatureapps.util.ScheduleUtil;
import ip.signature.com.signatureapps.util.TimeConverter;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;

public class AttendanceService extends Service implements ScheduleListener {
    private static AttendanceService instance;
    private static ScheduleUtil scheduleUtil;
    private boolean isSuccess = false;

    public void onCreate() {
        instance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            String CHANNEL_ID = "my_service";
            String CHANNEL_NAME = "My Background Service";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(instance)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.singature_icon)
                    .setPriority(PRIORITY_MIN).build();

            startForeground(101, notification);
        }

        scheduleUtil = new ScheduleUtil(this, 0).always(true);
        scheduleUtil.run(TimeConverter.convertToMinute(10));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static AttendanceService getInstance() {
        return instance;
    }

    public static void stop() {
        if (scheduleUtil != null) {
            scheduleUtil.end();
        }
    }

    @Override
    public boolean onRun(int requestCode) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            JSONObject object = new JSONObject();
            object.put("id", Global.id);
            object.put("longitude", GPSUtil.getLongitude());
            object.put("latitude", GPSUtil.getLatitude());
            object.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));

            new Transporter()
                    .context(this)
                    .listener(new TransportListener() {
                        @Override
                        public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
                            try {
                                String result = String.valueOf(body);
                                JSONObject json = new JSONObject(result);

                                if (Integer.parseInt(json.get("code").toString()) == 200) {
                                    String classification = json.getString("result");
                                    isSuccess = classification.equals("true");

                                    GlobalMaps.getMenuFragment().updateMaps();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                isSuccess = false;
                            }
                        }

                        @Override
                        public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {

                        }
                    })
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/attendance/track")
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new Body().json(object))
                    .post()
                    .silent(true)
                    .execute();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return isSuccess;
    }

    @Override
    public void onDone(int requestCode) {

    }

    @Override
    public void onFail(int requestCode) {

    }
}
