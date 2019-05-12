package ip.signature.com.signatureapps.fragment.history;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.activity.AttendanceTrackActivity;
import ip.signature.com.signatureapps.activity.HistoryActivity;
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;

public class SearchHistoryFragment extends Fragment implements View.OnClickListener, TransportListener {
    private EditText etDate;
    private EditText etDateEnd;
    private android.widget.LinearLayout llPilih;
    private android.widget.LinearLayout llPilihEnd;
    private RelativeLayout rlCari;

    public SearchHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);

        etDate = (EditText) view.findViewById(R.id.etDate);
        etDateEnd = (EditText) view.findViewById(R.id.etDateEnd);
        llPilih = (android.widget.LinearLayout) view.findViewById(R.id.llPilih);
        llPilihEnd = (android.widget.LinearLayout) view.findViewById(R.id.llPilihEnd);
        rlCari = (RelativeLayout) view.findViewById(R.id.rlCari);

        llPilih.setOnClickListener(this);
        llPilihEnd.setOnClickListener(this);
        rlCari.setOnClickListener(this);

        return view;
    }

    private void showDateDialog(EditText editText){
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                editText.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == llPilih) {
            showDateDialog(etDate);
        } else if (v == llPilihEnd) {
            showDateDialog(etDateEnd);
        } else if (v == rlCari) {
            new Transporter()
                    .context(getActivity())
                    .listener(this)
                    .url("https://monitoring-api.herokuapp.com")
                    .route("/api/v1/attendance/history/" + Global.id + "/" + etDate.getText().toString() + "/" + etDateEnd.getText().toString())
                    .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                    .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                    .gets()
                    .execute();
        }
    }

    @Override
    public void onTransportDone(Object code, Object message, Object body, int id, Object... packet) {
        try {
            String result = String.valueOf(body);
            JSONObject json = new JSONObject(result);

            if (Integer.parseInt(json.get("code").toString()) == 200) {
                String data = json.getString("result");
                JSONArray array = new JSONArray(data);
                ((HistoryActivity) getContext()).toMaps(array);
            } else {
                new AlertDialogWithOneButton(getActivity())
                        .setContent("Data absensi tanggal " + etDate.getText().toString() + " tidak ditemukan")
                        .setListener(new AlertDialogListener.OneButton() {
                            @Override
                            public void onClickButton(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTransportFail(Object code, Object message, Object body, int id, Object... packet) {
        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
    }
}
