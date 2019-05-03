package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.component.AlertDialogWithTwoButton;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.service.AttendanceService;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;
import ip.signature.com.signatureapps.util.GPSUtil;

public class EndAttendanceActivity extends AppCompatActivity implements
        View.OnClickListener,
        TransportListener,
        AlertDialogListener.TwoButton,
        AlertDialogListener.OneButton {

    private Button btnBatal;
    private Button btnSave;
    private ImageView back;

    private AlertDialogWithTwoButton dialog;

    private final int RC_CHECK = 0;
    private final int RC_END_ATTENDANCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_attendance);

        btnBatal = (Button) findViewById(R.id.btn_batal);
        btnSave = (Button) findViewById(R.id.btn_save);
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            btnBatal.setOnClickListener(this);
            btnSave.setOnClickListener(this);

            dialog = new AlertDialogWithTwoButton(this)
                    .setListener(this)
                    .setTextButtonLeft("Batal")
                    .setTextButtonRight("Selesai")
                    .setContent("Anda akan mengakhiri absensi kerja hari ini. Anda yakin..?");

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            new Transporter()
                    .id(RC_CHECK)
                    .context(this)
                    .listener(this)
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/attendance/checkend/" + Global.id)
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                    .gets()
                    .execute();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBatal) {
            onBackPressed();
        } else if (v == btnSave) {
            dialog.show();
        } else if (v == back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                String classification = json.getString("result");

                if (id == RC_CHECK) {
                    if (classification.equals("false")) {
                        AlertDialogWithOneButton dialog = new AlertDialogWithOneButton(this)
                                .setListener(this)
                                .setTextButton("OK")
                                .setContent("Anda belum melakukan absen untuk hari ini atau telah melakukan absen pulang.");

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                } else if (id == RC_END_ATTENDANCE) {
                    if (classification.equals("true")) {
                        Toast.makeText(this, "Anda telah melakukan absen pulang.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), AttendanceService.class);
                        intent.addCategory("ATTENDANCE");
                        stopService(intent);

                        AttendanceService.stop();
                        onBackPressed();
                    } else if (classification.equals("false")) {
                        Toast.makeText(this, "Gagal absen pulang. Silahkan ulangi.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onClickButtonLeft(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onClickButtonRight(Dialog dialog) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            JSONObject params = new JSONObject();
            params.put("id", Global.id);
            params.put("end_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
            params.put("longitude", GPSUtil.getLongitude());
            params.put("latitude", GPSUtil.getLatitude());

            new Transporter()
                    .id(RC_END_ATTENDANCE)
                    .context(this)
                    .listener(this)
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/attendance/update")
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new Body().json(params))
                    .put()
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickButton(Dialog dialog) {
        dialog.dismiss();
        onBackPressed();
    }
}
