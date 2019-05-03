package ip.signature.com.signatureapps.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.transport.Body;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;
import ip.signature.com.signatureapps.activity.register.RegisterStep1;
import ip.signature.com.signatureapps.activity.register.RegisterStep2;
import ip.signature.com.signatureapps.activity.register.RegisterStep3;
import ip.signature.com.signatureapps.activity.register.RegisterStep4;
import ip.signature.com.signatureapps.activity.register.RegisterStep5;
import ip.signature.com.signatureapps.activity.register.RegisterStep6;
import ip.signature.com.signatureapps.activity.register.RegisterStep7;
import ip.signature.com.signatureapps.activity.register.RegisterStep8;
import ip.signature.com.signatureapps.activity.register.RegisterStep9;
import ip.signature.com.signatureapps.activity.register.RegisterStepFinal;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TransportListener, AlertDialogListener.OneButton {
    private EditText etNip;
    private EditText etName;
    private EditText etPassword;
    private RelativeLayout rlRegis;

    private final int RC_CHECK = 0;
    private final int RC_SUBMIT = 1;

    private AlertDialogWithOneButton dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNip = (EditText) findViewById(R.id.etNip);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        rlRegis = (RelativeLayout) findViewById(R.id.rlRegis);

        rlRegis.setOnClickListener(this);

        dialog = new AlertDialogWithOneButton(this)
                .setListener(this)
                .setTextButton("OK");

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == rlRegis) {
            if (etNip.getText().toString().trim().equals("")) {
                dialog.setContent("Nomor NIP tidak boleh kosong").show();
            } else if (etName.getText().toString().trim().equals("")) {
                dialog.setContent("Nama tidak boleh kosong").show();
            } else if (etPassword.getText().toString().trim().equals("")) {
                dialog.setContent("Password tidak boleh kosong").show();
            } else {
                new Transporter()
                        .id(RC_CHECK)
                        .context(this)
                        .listener(this)
                        .url("https://monitoring-api.herokuapp.com")
                        .route("/api/v1/user/check/" + etNip.getText().toString())
                        .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                        .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                        .gets()
                        .execute();
            }
        }
    }

    private void goToRegisterStep(int step, int id, String name, String nip) {
        Intent intent;
        switch (step) {
            case 1:
                intent = new Intent(this, RegisterStep1.class);
                break;
            case 2:
                intent = new Intent(this, RegisterStep2.class);
                break;
            case 3:
                intent = new Intent(this, RegisterStep3.class);
                break;
            case 4:
                intent = new Intent(this, RegisterStep4.class);
                break;
            case 5:
                intent = new Intent(this, RegisterStep5.class);
                break;
            case 6:
                intent = new Intent(this, RegisterStep6.class);
                break;
            case 7:
                intent = new Intent(this, RegisterStep7.class);
                break;
            case 8:
                intent = new Intent(this, RegisterStep8.class);
                break;
            case 9:
                intent = new Intent(this, RegisterStep9.class);
                break;
            case 10:
                intent = new Intent(this, RegisterStepFinal.class);
                break;
            default:
                intent = new Intent(this, LoginActivity.class);
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putString("nip", nip);

        intent.putExtra("bundle", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Global.reset();
        super.onBackPressed();
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (id == RC_CHECK) {
                if (Integer.parseInt(json.get("code").toString()) == 200) {
                    JSONObject object = new JSONObject(json.get("result").toString());
                    if (object.getInt("status") == 0) {
                        JSONObject content = new JSONObject();
                        content.put("nip", etNip.getText().toString());
                        content.put("name", etName.getText().toString());
                        content.put("password", etPassword.getText().toString());

                        new Transporter()
                                .id(RC_SUBMIT)
                                .context(this)
                                .listener(this)
                                .url("https://monitoring-api.herokuapp.com")
                                .route("/api/v1/user/register")
                                .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                                .body(new Body().json(content))
                                .post()
                                .execute();
                    } else {
                        int step = object.getInt("step");
                        int idUser = object.getInt("id");
                        String name = object.getString("name");
                        String nip = object.getString("nip");

                        Global.set(idUser, nip, name, step);
                        goToRegisterStep(step, idUser, name, nip);
                    }
                } else {
                    Toast.makeText(this, "Authorization Not Valid", Toast.LENGTH_SHORT).show();
                }
            } else if (id == RC_SUBMIT) {
                if (Integer.parseInt(json.get("code").toString()) == 200) {
                    JSONObject object = new JSONObject(json.get("result").toString());
                    int step = object.getInt("step");
                    int idUser = object.getInt("id");
                    String name = object.getString("name");
                    String nip = object.getString("nip");

                    goToRegisterStep(step, idUser, name, nip);
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

    @Override
    public void onClickButton(Dialog dialog) {
        dialog.dismiss();
    }
}
