package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.component.InputDialog;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.InputDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener, InputDialogListener, TransportListener, AlertDialogListener.OneButton {
    private TextView etNip;
    private TextView etName;
    private TextView etPassword;
    private Button btnGanti;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        etNip = (TextView) findViewById(R.id.etNip);
        etName = (TextView) findViewById(R.id.etName);
        etPassword = (TextView) findViewById(R.id.etPassword);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        btnGanti = (Button) findViewById(R.id.btnGanti);
        btnGanti.setOnClickListener(this);

        etNip.setText(Global.nip);
        etName.setText(Global.name);
    }

    @Override
    public void onClick(View v) {
        if (v == btnGanti) {
            InputDialog inputDialog = new InputDialog(this)
                    .setListener(this)
                    .setContent("Masukan password baru anda")
                    .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .setTextButtonRight("Simpan");

            inputDialog.show();
        } else if (v == back) {
            onBackPressed();
        }
    }

    @Override
    public void onClickButtonLeft(InputDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onClickButtonRight(InputDialog dialog) {
        try {
            dialog.dismiss();

            JSONObject object = new JSONObject();
            object.put("id", Global.id);
            object.put("password", dialog.getEditContent());

            new Transporter()
                    .context(this)
                    .listener(this)
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/user/updatePassword")
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new Body().json(object))
                    .put()
                    .execute();

        } catch (JSONException e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            AlertDialogWithOneButton alertDialogWithOneButton = new AlertDialogWithOneButton(this).setListener(this);
            if (Integer.parseInt(json.get("code").toString()) == 200) {
                alertDialogWithOneButton.setContent("Perubahan password berhasil").show();
            } else {
                alertDialogWithOneButton.setContent("Perubahan password gagal. Silahkan ulangi.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickButton(Dialog dialog) {
        dialog.dismiss();
    }
}
