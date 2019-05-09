package ip.signature.com.signatureapps.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.listener.InputDialogListener;

public class InputDialog extends Dialog implements View.OnClickListener {
    public Context c;
    public Dialog d;

    private TextView content;
    private EditText etContent;
    private Button btnCancel;
    private Button btnOk;

    private InputDialogListener listener;

    public InputDialog(@NonNull Context context) {
        super(context);
        this.c = context;

        init();
    }

    private void init() {
        show();
        dismiss();
    }

    public InputDialog setListener(InputDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public InputDialog setContent(String text) {
        content.setText(text);
        return this;
    }

    public InputDialog setContent(int text) {
        content.setText(text);
        return this;
    }

    public InputDialog setTextButtonLeft(String text) {
        btnCancel.setText(text);
        return this;
    }

    public InputDialog setTextButtonLeft(int text) {
        btnCancel.setText(text);
        return this;
    }

    public InputDialog setTextButtonRight(String text) {
        btnOk.setText(text);
        return this;
    }

    public InputDialog setTextButtonRight(int text) {
        btnOk.setText(text);
        return this;
    }

    public InputDialog setInputType(int inputType) {
        etContent.setInputType(inputType);
        return this;
    }

    public String getEditContent() {
        return etContent.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_input_dialog);

        content = (TextView) findViewById(R.id.content);
        etContent = (EditText) findViewById(R.id.etContent);
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
