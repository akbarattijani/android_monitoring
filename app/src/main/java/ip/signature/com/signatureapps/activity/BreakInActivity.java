package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
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
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.util.GPSUtil;

public class BreakInActivity extends AppCompatActivity implements View.OnClickListener, TransportListener, AlertDialogListener.OneButton {
    private ImageView back;
    private Button btnSave;
    private Button btnBatal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_in);

        back = (ImageView) findViewById(R.id.back);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnBatal = (Button) findViewById(R.id.btn_batal);

        back.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBatal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == back || v == btnBatal) {
            onBackPressed();
        } else if (v == btnSave) {
            try {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                JSONObject params = new JSONObject();
                params.put("id_user", Global.id);
                params.put("break_end_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
                params.put("longitude", GPSUtil.getLongitude());
                params.put("latitude", GPSUtil.getLatitude());

                new Transporter()
                        .context(this)
                        .listener(this)
                        .url("https://monitoring-api.herokuapp.com")
                        .route("/api/v1/attendance/breakIn")
                        .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                        .body(new Body().json(params))
                        .put()
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClickButton(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                String res = json.getString("result");

                if (res.equals("true")) {
                    Toast.makeText(this, "Berhasil perbarui status selesai istirahat", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    AlertDialogWithOneButton dialog = new AlertDialogWithOneButton(this)
                            .setListener(this)
                            .setContent(res);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
    }
}
