package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Grayscale;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Negasi;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Thresholding;
import ip.signature.com.signatureapps.imageprocessing.Resize.Normalisasi;
import ip.signature.com.signatureapps.imageprocessing.Segmentation.SignatureExtraction;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.ScheduleListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.service.AttendanceService;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;
import ip.signature.com.signatureapps.util.ConvertArray;
import ip.signature.com.signatureapps.util.GPSUtil;
import ip.signature.com.signatureapps.util.ScheduleUtil;
import ip.signature.com.signatureapps.util.TimeConverter;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener, TransportListener, AlertDialogListener.OneButton {
    private SignaturePad signaturePad;
    private Button btnClear;
    private Button btnSave;
    private ImageView back;

    private final int RC_CHECK = 0;
    private final int RC_SIGNATURE_CHECK = 1;
    private final int RC_ATTENDANCE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSave = (Button) findViewById(R.id.btn_save);
        back = (ImageView) findViewById(R.id.back);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(AttendanceActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                btnSave.setEnabled(true);
                btnClear.setEnabled(true);
            }

            @Override
            public void onClear() {
                btnClear.setEnabled(false);
                btnSave.setEnabled(false);
            }
        });

        btnClear.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        back.setOnClickListener(this);

        new Transporter()
                .id(RC_CHECK)
                .context(this)
                .listener(this)
                .url("https://monitoring-api.herokuapp.com")
                .route("/api/v1/attendance/check/" + Global.id)
                .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                .gets()
                .execute();
    }

    private void createSignature() {
        try {
            int[][] signatureExtraction = Grayscale.toGray(signaturePad.getSignatureBitmap(), false);
            signatureExtraction = Thresholding.execute(signatureExtraction, signaturePad.getSignatureBitmap(), false);
            signatureExtraction = Negasi.toBiner(signatureExtraction, false);
            Bitmap result = SignatureExtraction.getExtraction(signatureExtraction, false);

            ArrayList<Bitmap> images = new ArrayList<>();
            images.add(result);
            int[][] signatureNormalization = new Normalisasi().resize(images, 1, 100, 100).getBitmap(0, false);
            String featureExtraction = new ConvertArray().twoDimensionToOneDimension(signatureNormalization).asString();

            System.out.println("NIP : " + Global.nip + "\tID : " + Global.id);

            JSONObject body = new JSONObject();
            body.put("nip", Global.nip);
            body.put("biner", featureExtraction);

            new Transporter()
                    .id(RC_SIGNATURE_CHECK)
                    .context(this)
                    .listener(this)
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/classification/search")
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new Body().json(body))
                    .post()
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnClear) {
            signaturePad.clear();
        } else if (v == btnSave) {
            if (GPSUtil.forceGps(this)) {
                createSignature();
            }
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
                                .setContent("Anda telah melakukan absen untuk hari ini");

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                } else if (id == RC_SIGNATURE_CHECK) {
                    if (classification.equals("true")) {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        JSONObject params = new JSONObject();
                        params.put("id", Global.id);
                        params.put("start_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
                        params.put("longitude", GPSUtil.getLongitude());
                        params.put("latitude", GPSUtil.getLatitude());

                        new Transporter()
                                .id(RC_ATTENDANCE)
                                .context(this)
                                .listener(this)
                                .url("https://monitoring-api.herokuapp.com")
                                .route("/api/v1/attendance/insert")
                                .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                                .body(new Body().json(params))
                                .post()
                                .execute();
                    } else if (classification.equals("false")) {
                        Toast.makeText(this, "Tanda tangan tidak cocok. Silahkan ulangi.", Toast.LENGTH_SHORT).show();
                    }
                } else if (id == RC_ATTENDANCE) {
                    if (classification.equals("true")) {
                        Toast.makeText(this, "Absen Berhasil", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), AttendanceService.class);
                        intent.addCategory("ATTENDANCE");
                        startService(intent);

                        onBackPressed();
                    } else if (classification.equals("false")) {
                        Toast.makeText(this, "Absen Gagal. Silahkan Ulangi.", Toast.LENGTH_SHORT).show();
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
    public void onClickButton(Dialog dialog) {
        dialog.dismiss();
        onBackPressed();
    }
}
