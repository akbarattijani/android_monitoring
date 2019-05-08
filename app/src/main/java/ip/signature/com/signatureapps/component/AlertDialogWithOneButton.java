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

public class AlertDialogWithOneButton extends Dialog implements View.OnClickListener {
    public Context c;
    public Dialog d;

    private TextView content;
    private Button btnOk;

    private AlertDialogListener.OneButton listener;

    public AlertDialogWithOneButton(@NonNull Context context) {
        super(context);
        this.c = context;

        init();
    }

    private void init() {
        show();
        dismiss();
    }

    public AlertDialogWithOneButton setListener(AlertDialogListener.OneButton listener) {
        this.listener = listener;
        return this;
    }

    public AlertDialogWithOneButton setContent(String text) {
        content.setText(text);
        return this;
    }

    public AlertDialogWithOneButton setContent(int text) {
        content.setText(text);
        return this;
    }

    public AlertDialogWithOneButton setTextButton(String text) {
        btnOk.setText(text);
        return this;
    }

    public AlertDialogWithOneButton setTextButton(int text) {
        btnOk.setText(text);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog_2);

        content = (TextView) findViewById(R.id.content);
        btnOk = (Button) findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            listener.onClickButton(this);
        }
    }
}
