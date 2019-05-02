package ip.signature.com.signatureapps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.listener.TransportListener;

public class EndAbsenActivity extends AppCompatActivity implements View.OnClickListener, TransportListener {
    private Button btnBatal;
    private Button btnSave;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_absen);

        btnBatal = (Button) findViewById(R.id.btn_batal);
        btnSave = (Button) findViewById(R.id.btn_save);
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            btnBatal.setOnClickListener(this);
            btnSave.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBatal) {
            onBackPressed();
        } else if (v == btnSave) {

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
