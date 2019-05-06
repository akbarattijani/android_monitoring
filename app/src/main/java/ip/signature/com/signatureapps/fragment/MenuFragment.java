package ip.signature.com.signatureapps.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ip.signature.com.signatureapps.R;
import ip.signature.com.signatureapps.activity.BreakInActivity;
import ip.signature.com.signatureapps.activity.BreakOutActivity;
import ip.signature.com.signatureapps.activity.EndAttendanceActivity;
import ip.signature.com.signatureapps.activity.AttendanceActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout llAbsen;
    private LinearLayout llEndAbsen;
    private LinearLayout llBreak;
    private LinearLayout llEndBreak;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        llAbsen = (LinearLayout) view.findViewById(R.id.llAbsen);
        llEndAbsen = (LinearLayout) view.findViewById(R.id.llEndAbsen);
        llBreak = (LinearLayout) view.findViewById(R.id.llBreak);
        llEndBreak = (LinearLayout) view.findViewById(R.id.llEndBreak);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            llAbsen.setOnClickListener(this);
            llEndAbsen.setOnClickListener(this);
            llBreak.setOnClickListener(this);
            llEndBreak.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == llAbsen) {
            intent = new Intent(getActivity(), AttendanceActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llEndAbsen) {
            intent = new Intent(getActivity(), EndAttendanceActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llBreak) {
            intent = new Intent(getActivity(), BreakOutActivity.class);
            intent.putExtra("bundle", this.getArguments());
        } else if (v == llEndBreak) {
            intent = new Intent(getActivity(), BreakInActivity.class);
            intent.putExtra("bundle", this.getArguments());
        }

        startActivity(intent);
    }
}
