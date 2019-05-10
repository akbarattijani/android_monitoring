package ip.signature.com.signatureapps.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.adapter.AttendanceHistoryAdapter;

public class AttendanceHistoryFragment extends Fragment {
    private RecyclerView rvHistory;

    public AttendanceHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_history, container, false);

        try {
            AttendanceHistoryAdapter adapter = new AttendanceHistoryAdapter(getActivity(), new JSONArray(getArguments().getString("json")));

            rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
            rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvHistory.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
