package ip.signature.com.signatureapps.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TransportListener {

    private EditText etNip;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNip = (EditText) findViewById(R.id.etNip);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            try {
                JSONObject body = new JSONObject();
                body.put("nip", etNip.getText().toString());
                body.put("password", etPassword.getText().toString());

                new Transporter()
                        .context(this)
                        .listener(this)
                        .url("https://monitoring-api.herokuapp.com")
                        .route("/api/v1/user/login/" + etNip.getText().toString() + "/" + etPassword.getText().toString())
                        .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                        .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                        .gets()
                        .execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (view == btnRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
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
                intent = new Intent(this, MenuActivity.class);
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

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                JSONObject object = new JSONObject(json.get("result").toString());
                String nip = object.get("nip").toString();
                String name = object.get("name").toString();
                int idUser = Integer.parseInt(object.get("id").toString());
                int step = object.getInt("step");

                Global.set(idUser, nip, name, step);
                goToRegisterStep(step, idUser,name, nip);
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
