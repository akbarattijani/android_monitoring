package ip.signature.com.signatureapps.fragment.attendance;

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
import ip.signature.com.signatureapps.component.AlertDialogWithOneButton;
import ip.signature.com.signatureapps.global.Global;
import ip.signature.com.signatureapps.listener.AlertDialogListener;
import ip.signature.com.signatureapps.listener.TransportListener;
import ip.signature.com.signatureapps.model.MediaType;
import ip.signature.com.signatureapps.transport.Transporter;
import ip.signature.com.signatureapps.transport.body.BodyBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SearchAttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchAttendanceFragment extends Fragment implements View.OnClickListener, TransportListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText etDate;
    private android.widget.LinearLayout llPilih;
    private RelativeLayout rlCari;

    public SearchAttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchAttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchAttendanceFragment newInstance(String param1, String param2) {
        SearchAttendanceFragment fragment = new SearchAttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_attendance, container, false);

        etDate = (EditText) view.findViewById(R.id.etDate);
        llPilih = (android.widget.LinearLayout) view.findViewById(R.id.llPilih);
        rlCari = (RelativeLayout) view.findViewById(R.id.rlCari);

        llPilih.setOnClickListener(this);
        rlCari.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == llPilih) {
            showDateDialog();
        } else if (v == rlCari) {
            try {
                new Transporter()
                        .context(getActivity())
                        .listener(this)
                        .url("https://monitoring-api.herokuapp.com")
                        .route("/api/v1/attendance/get/" + Global.id + "/" + etDate.getText().toString())
                        .header("Authorization", "ApiAuth api_key=DMA128256512AI")
                        .body(new BodyBuilder().setMediaType(MediaType.PLAIN.toString()))
                        .gets()
                        .execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                ((AttendanceTrackActivity) getContext()).toMaps(array);
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

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                etDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
