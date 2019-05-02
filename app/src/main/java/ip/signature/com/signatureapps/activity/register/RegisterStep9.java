package ip.signature.com.signatureapps.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.util.ArrayList;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Grayscale;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Negasi;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Thresholding;
import ip.signature.com.signatureapps.imageprocessing.Resize.Normalisasi;
import ip.signature.com.signatureapps.imageprocessing.Segmentation.SignatureExtraction;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.util.ConvertArray;

public class RegisterStep9 extends AppCompatActivity implements View.OnClickListener, TransportListener {
    private SignaturePad signaturePad;
    private Button rlNext;
    private Button rlClear;
    private Bundle bundle;

    private final int RC_SEND_SIGNATURE = 0;
    private final int RC_UPDATE_STEP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step);

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        rlNext = (Button) findViewById(R.id.btn_next);
        rlClear = (Button) findViewById(R.id.btn_clear);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                rlNext.setEnabled(true);
                rlClear.setEnabled(true);
            }

            @Override
            public void onClear() {
                rlNext.setEnabled(false);
                rlClear.setEnabled(false);
            }
        });

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            rlNext.setOnClickListener(this);
            rlClear.setOnClickListener(this);

            this.bundle = bundle;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlNext) {
            try {
                int id = bundle.getInt("id", 0);
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("biner", createSignature(signaturePad));

                new Transporter()
                        .id(RC_SEND_SIGNATURE)
                        .context(this)
                        .listener(this)
                        .url("https://monitoring-api.herokuapp.com")
                        .route("/api/v1/sample/insert")
                        .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                        .body(new Body().json(object))
                        .post()
                        .execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v == rlClear) {
            signaturePad.clear();
        }
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (id == RC_SEND_SIGNATURE) {
                if (Integer.parseInt(json.get("code").toString()) == 200) {
                    JSONObject object = new JSONObject();
                    object.put("id", bundle.getInt("id", 0));
                    object.put("step", 10);

                    new Transporter()
                            .id(RC_UPDATE_STEP)
                            .context(this)
                            .listener(this)
                            .url("https://monitoring-api.herokuapp.com")
                            .route("/api/v1/user/updateStep")
                            .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                            .body(new Body().json(object))
                            .put()
                            .execute();
                } else {
                    Toast.makeText(this, "Authorization Not Valid", Toast.LENGTH_SHORT).show();
                }
            } else if (id == RC_UPDATE_STEP) {
                if (Integer.parseInt(json.get("code").toString()) == 200) {
                    Intent intent = new Intent(this, RegisterStepFinal.class);
                    intent.putExtra("bundle", bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Authorization Not Valid", Toast.LENGTH_SHORT).show();
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

    private String createSignature(SignaturePad signaturePad) {
        int[][] signatureExtraction = Grayscale.toGray(signaturePad.getSignatureBitmap(), false);
        signatureExtraction = Thresholding.execute(signatureExtraction, signaturePad.getSignatureBitmap(), false);
        signatureExtraction = Negasi.toBiner(signatureExtraction, false);
        Bitmap result = SignatureExtraction.getExtraction(signatureExtraction, false);

        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(result);
        int[][] signatureNormalization = new Normalisasi().resize(images, 1, 50, 50).getBitmap(0, false);

        return new ConvertArray().twoDimensionToOneDimension(signatureNormalization).asString();
    }
}
