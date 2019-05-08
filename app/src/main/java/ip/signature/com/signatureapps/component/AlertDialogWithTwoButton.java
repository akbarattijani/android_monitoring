package ip.signature.com.signatureapps.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.listener.AlertDialogListener;

public class AlertDialogWithTwoButton extends Dialog implements View.OnClickListener {
    public Context c;
    public Dialog d;

    private TextView content;
    private Button btnCancel;
    private Button btnOk;

    private AlertDialogListener.TwoButton listener;

    public AlertDialogWithTwoButton(@NonNull Context context) {
        super(context);
        this.c = context;

        init();
    }

    private void init() {
        show();
        dismiss();
    }

    public AlertDialogWithTwoButton setListener(AlertDialogListener.TwoButton listener) {
        this.listener = listener;
        return this;
    }

    public AlertDialogWithTwoButton setContent(String text) {
        content.setText(text);
        return this;
    }

    public AlertDialogWithTwoButton setContent(int text) {
        content.setText(text);
        return this;
    }

    public AlertDialogWithTwoButton setTextButtonLeft(String text) {
        btnCancel.setText(text);
        return this;
    }

    public AlertDialogWithTwoButton setTextButtonLeft(int text) {
        btnCancel.setText(text);
        return this;
    }

    public AlertDialogWithTwoButton setTextButtonRight(String text) {
        btnOk.setText(text);
        return this;
    }

    public AlertDialogWithTwoButton setTextButtonRight(int text) {
        btnOk.setText(text);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);

        content = (TextView) findViewById(R.id.content);
        btnCancel = (Button) findViewById(R.id.btn_batal);
        btnOk = (Button) findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            listener.onClickButtonLeft(this);
        } else if (v == btnOk) {
            listener.onClickButtonRight(this);
        }
    }
}
