package ip.signature.com.signatureapps.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.util.ArrayList;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Grayscale;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Negasi;
import ip.signature.com.signatureapps.imageprocessing.Preprocessing.Thresholding;
import ip.signature.com.signatureapps.imageprocessing.Resize.Normalisasi;
import ip.signature.com.signatureapps.imageprocessing.Segmentation.SignatureExtraction;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.util.ConvertArray;

public class TestActivity extends AppCompatActivity implements View.OnClickListener, TransportListener {
    private SignaturePad signaturePad;
    private Button btnClear;
    private Button btnSave;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSave = (Button) findViewById(R.id.btn_save);
        back = (ImageView) findViewById(R.id.back);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(TestActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
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
    }

    private void createSignature() {
        try {
            int[][] signatureExtraction = Grayscale.toGray(signaturePad.getSignatureBitmap(), false);
            signatureExtraction = Thresholding.execute(signatureExtraction, signaturePad.getSignatureBitmap(), false);
            signatureExtraction = Negasi.toBiner(signatureExtraction, false);
            Bitmap result = SignatureExtraction.getExtraction(signatureExtraction, false);

            ArrayList<Bitmap> images = new ArrayList<>();
            images.add(result);
            int[][] signatureNormalization = new Normalisasi().resize(images, 1, 50, 50).getBitmap(0, false);
            String featureExtraction = new ConvertArray().twoDimensionToOneDimension(signatureNormalization).asString();

            System.out.println("NIP : " + Global.nip + "\tID : " + Global.id);

            JSONObject body = new JSONObject();
            body.put("nip", Global.nip);
            body.put("biner", featureExtraction);

            new Transporter()
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
            createSignature();
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
                if (classification.equals("true")) {
                    Toast.makeText(this, "TANDA TANGAN COCOK", Toast.LENGTH_SHORT).show();
                } else if (classification.equals("false")) {
                    Toast.makeText(this, "TANDA TANGAN TIDAK COCOK", Toast.LENGTH_SHORT).show();
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
