package ip.signature.com.signatureapps.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import net.cachapa.expandablelayout.ExpandableLayout;

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

public class SampleActivity extends AppCompatActivity implements View.OnClickListener, TransportListener {
    private SignaturePad signaturePad1;
    private SignaturePad signaturePad2;
    private SignaturePad signaturePad3;
    private SignaturePad signaturePad4;
    private SignaturePad signaturePad5;
    private SignaturePad signaturePad6;
    private SignaturePad signaturePad7;
    private SignaturePad signaturePad8;
    private SignaturePad signaturePad9;
    private SignaturePad signaturePad10;

    private ExpandableLayout expand1;
    private ExpandableLayout expand2;
    private ExpandableLayout expand3;
    private ExpandableLayout expand4;
    private ExpandableLayout expand5;
    private ExpandableLayout expand6;
    private ExpandableLayout expand7;
    private ExpandableLayout expand8;
    private ExpandableLayout expand9;
    private ExpandableLayout expand10;

    private LinearLayout toggle1;
    private LinearLayout toggle2;
    private LinearLayout toggle3;
    private LinearLayout toggle4;
    private LinearLayout toggle5;
    private LinearLayout toggle6;
    private LinearLayout toggle7;
    private LinearLayout toggle8;
    private LinearLayout toggle9;
    private LinearLayout toggle10;

    private Button btnSave1;
    private Button btnSave2;
    private Button btnSave3;
    private Button btnSave4;
    private Button btnSave5;
    private Button btnSave6;
    private Button btnSave7;
    private Button btnSave8;
    private Button btnSave9;
    private Button btnSave10;

    private Button btnClear;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        signaturePad1 = (SignaturePad) findViewById(R.id.signature_pad1);
        signaturePad2 = (SignaturePad) findViewById(R.id.signature_pad2);
        signaturePad3 = (SignaturePad) findViewById(R.id.signature_pad3);
        signaturePad4 = (SignaturePad) findViewById(R.id.signature_pad4);
        signaturePad5 = (SignaturePad) findViewById(R.id.signature_pad5);
        signaturePad6 = (SignaturePad) findViewById(R.id.signature_pad6);
        signaturePad7 = (SignaturePad) findViewById(R.id.signature_pad7);
        signaturePad8 = (SignaturePad) findViewById(R.id.signature_pad8);
        signaturePad9 = (SignaturePad) findViewById(R.id.signature_pad9);
        signaturePad10 = (SignaturePad) findViewById(R.id.signature_pad10);

        expand1 = (ExpandableLayout) findViewById(R.id.expand1);
        expand2 = (ExpandableLayout) findViewById(R.id.expand2);
        expand3 = (ExpandableLayout) findViewById(R.id.expand3);
        expand4 = (ExpandableLayout) findViewById(R.id.expand4);
        expand5 = (ExpandableLayout) findViewById(R.id.expand5);
        expand6 = (ExpandableLayout) findViewById(R.id.expand6);
        expand7 = (ExpandableLayout) findViewById(R.id.expand7);
        expand8 = (ExpandableLayout) findViewById(R.id.expand8);
        expand9 = (ExpandableLayout) findViewById(R.id.expand9);
        expand10 = (ExpandableLayout) findViewById(R.id.expand10);

        toggle1 = (LinearLayout) findViewById(R.id.toggle1);
        toggle2 = (LinearLayout) findViewById(R.id.toggle2);
        toggle3 = (LinearLayout) findViewById(R.id.toggle3);
        toggle4 = (LinearLayout) findViewById(R.id.toggle4);
        toggle5 = (LinearLayout) findViewById(R.id.toggle5);
        toggle6 = (LinearLayout) findViewById(R.id.toggle6);
        toggle7 = (LinearLayout) findViewById(R.id.toggle7);
        toggle8 = (LinearLayout) findViewById(R.id.toggle8);
        toggle9 = (LinearLayout) findViewById(R.id.toggle9);
        toggle10 = (LinearLayout) findViewById(R.id.toggle10);

        btnSave1 = (Button) findViewById(R.id.btn_save1);
        btnSave2 = (Button) findViewById(R.id.btn_save2);
        btnSave3 = (Button) findViewById(R.id.btn_save3);
        btnSave4 = (Button) findViewById(R.id.btn_save4);
        btnSave5 = (Button) findViewById(R.id.btn_save5);
        btnSave6 = (Button) findViewById(R.id.btn_save6);
        btnSave7 = (Button) findViewById(R.id.btn_save7);
        btnSave8 = (Button) findViewById(R.id.btn_save8);
        btnSave9 = (Button) findViewById(R.id.btn_save9);
        btnSave10 = (Button) findViewById(R.id.btn_save10);

        btnClear = (Button) findViewById(R.id.btn_clear);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            btnClear.setOnClickListener(this);

            toggle1.setOnClickListener(this);
            toggle2.setOnClickListener(this);
            toggle3.setOnClickListener(this);
            toggle4.setOnClickListener(this);
            toggle5.setOnClickListener(this);
            toggle6.setOnClickListener(this);
            toggle7.setOnClickListener(this);
            toggle8.setOnClickListener(this);
            toggle9.setOnClickListener(this);
            toggle10.setOnClickListener(this);

            btnSave1.setOnClickListener(this);
            btnSave2.setOnClickListener(this);
            btnSave3.setOnClickListener(this);
            btnSave4.setOnClickListener(this);
            btnSave5.setOnClickListener(this);
            btnSave6.setOnClickListener(this);
            btnSave7.setOnClickListener(this);
            btnSave8.setOnClickListener(this);
            btnSave9.setOnClickListener(this);
            btnSave10.setOnClickListener(this);

            this.bundle = bundle;
        }
    }

    private String createSignature(SignaturePad signaturePad) {
        int[][] signatureExtraction = Grayscale.toGray(signaturePad.getSignatureBitmap(), false);
        signatureExtraction = Thresholding.execute(signatureExtraction, signaturePad.getSignatureBitmap(), false);
        signatureExtraction = Negasi.toBiner(signatureExtraction, false);
        Bitmap result = SignatureExtraction.getExtraction(signatureExtraction, false);

        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(result);
        int[][] signatureNormalization = new Normalisasi().resize(images, 1, 50, 50).getBitmap(0, true);

        return new ConvertArray().twoDimensionToOneDimension(signatureNormalization).asString();
//        return null;
    }

    private void sendSample(String biner) {
        try {
            int id = bundle.getInt("id", 0);
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("biner", biner);

            new Transporter()
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
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave1) {
            String bitmap = createSignature(signaturePad1);
//            sendSample(bitmap);
        } else if (v == btnSave2) {
            String bitmap = createSignature(signaturePad2);
//            sendSample(bitmap);
        } else if (v == btnSave3) {
            String bitmap = createSignature(signaturePad3);
//            sendSample(bitmap);
        } else if (v == btnSave4) {
            String bitmap = createSignature(signaturePad4);
//            sendSample(bitmap);
        } else if (v == btnSave5) {
            String bitmap = createSignature(signaturePad5);
//            sendSample(bitmap);
        } else if (v == btnSave6) {
            String bitmap = createSignature(signaturePad6);
//            sendSample(bitmap);
        } else if (v == btnSave7) {
            String bitmap = createSignature(signaturePad7);
//            sendSample(bitmap);
        } else if (v == btnSave8) {
            String bitmap = createSignature(signaturePad8);
//            sendSample(bitmap);
        } else if (v == btnSave9) {
            String bitmap = createSignature(signaturePad9);
//            sendSample(bitmap);
        } else if (v == btnSave10) {
            String bitmap = createSignature(signaturePad10);
//            sendSample(bitmap);
        } else if (v == btnClear) {
            signaturePad1.clear();
            signaturePad2.clear();
            signaturePad3.clear();
            signaturePad4.clear();
            signaturePad5.clear();
            signaturePad6.clear();
            signaturePad7.clear();
            signaturePad8.clear();
            signaturePad9.clear();
            signaturePad10.clear();
        } else if (v == toggle1) {
            if (expand1.isExpanded()) {
                expand1.collapse();
            } else {
                expand1.expand();
            }
        } else if (v == toggle2) {
            if (expand2.isExpanded()) {
                expand2.collapse();
            } else {
                expand2.expand();
            }
        } else if (v == toggle3) {
            if (expand3.isExpanded()) {
                expand3.collapse();
            } else {
                expand3.expand();
            }
        } else if (v == toggle4) {
            if (expand4.isExpanded()) {
                expand4.collapse();
            } else {
                expand4.expand();
            }
        } else if (v == toggle5) {
            if (expand5.isExpanded()) {
                expand5.collapse();
            } else {
                expand5.expand();
            }
        } else if (v == toggle6) {
            if (expand6.isExpanded()) {
                expand6.collapse();
            } else {
                expand6.expand();
            }
        } else if (v == toggle7) {
            if (expand7.isExpanded()) {
                expand7.collapse();
            } else {
                expand7.expand();
            }
        } else if (v == toggle8) {
            if (expand8.isExpanded()) {
                expand8.collapse();
            } else {
                expand8.expand();
            }
        } else if (v == toggle9) {
            if (expand9.isExpanded()) {
                expand9.collapse();
            } else {
                expand9.expand();
            }
        } else if (v == toggle10) {
            if (expand10.isExpanded()) {
                expand10.collapse();
            } else {
                expand10.expand();
            }
        }
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra("nip", bundle.getString("nip", ""));
                intent.putExtra("name", bundle.getString("name", ""));
                intent.putExtra("id", bundle.getInt("id", 0));

                startActivity(intent);
            } else {
                Toast.makeText(this, "Authorization Not Valid", Toast.LENGTH_SHORT).show();
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
